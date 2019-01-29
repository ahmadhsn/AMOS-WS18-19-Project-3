package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements ResponseHandler {
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    public void login(View view) throws JSONException {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        JSONObject json = new JSONObject();
        json.put("email", email.getText().toString());
        json.put("password", password.getText().toString());

        Requests.executeRequest(this, "POST", "checkUser", json);

    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if(SocketUtility.hasSocketError(response)){
            Toast.makeText(this, "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }
        if (response != null && response.has("success")) {

            try {
                String eml = response.getString("email");
                int userId = response.getInt("id_user");
                int userType = response.getInt("id_user_type");
                SaveSharedPreference.saveUserInforamtion(this, eml, userId, userType);
                Toast.makeText(getApplicationContext(), "You are logged in now!", Toast.LENGTH_LONG).show();


                Realm.init(this);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.createOrUpdateObjectFromJson(User.class, response);
                realm.commitTransaction();
                realm.close();

                User user = realm.where(User.class).findFirst();

                if (user.getId_user_type() == 2) {
                    Intent intent = new Intent(this, BusinessUserMainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ShowEventActivity.class);
                    startActivity(intent);
                }
            }catch(JSONException ex){
                Log.i("Exception --- not requested", ex.toString());
                ex.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Wrong user credentials.", Toast.LENGTH_LONG);
        }
    }
}