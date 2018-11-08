package com.gr03.amos.bikerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gr03.amos.bikerapp.Requests;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity {

    EditText businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        businessName = findViewById(R.id.business_name);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rider:
                if (checked)
                    businessName.setVisibility(View.GONE);
                break;
            case R.id.business_user:
                if (checked)
                    businessName.setVisibility(VISIBLE);
                break;
        }
    }

    public void userSignUp(View view) throws JSONException{
        //TODO check all values are valid

        EditText password = findViewById(R.id.password);
        EditText confirm_pw = findViewById(R.id.confirm_password);

        String pw = password.getText().toString();

        if (!pw.equals(confirm_pw.getText().toString())) {
            //TODO inform user & clear values
            Log.i("COMPAREPASSWORDS", "FAILED........................");
            return;
        }

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);

        Boolean businessUser = false;
        if (businessName.VISIBLE == 0) businessUser = true;

        JSONObject json = new JSONObject();
        json.put("name", name.getText().toString());
        json.put("email", email.getText().toString());
        json.put("password", pw);
        json.put("isBusinessUser", businessUser.booleanValue());
        if (businessUser) json.put("business_name", businessName.getText().toString());

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("userRegistration", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }
}