package com.gr03.amos.bikerapp;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.gr03.amos.bikerapp.Models.Event;
        import com.gr03.amos.bikerapp.Models.ProfileBasic;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.concurrent.Callable;
        import java.util.concurrent.FutureTask;

        import io.realm.Realm;

public class ProfileBasicUserActivity extends AppCompatActivity {

    //Intent intent;
    //Long userId;
    TextView first_name, last_name, date_of_birth, user_gender, user_street, hnumber, user_postcode, user_city, user_state, user_country  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mt("create activity2");
        setContentView(R.layout.activity_profile_basic_user);

        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);
        date_of_birth = (TextView) findViewById(R.id.dob);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_street = (TextView) findViewById(R.id.user_street);
        hnumber = (TextView) findViewById(R.id.hnumber);
        user_postcode = (TextView) findViewById(R.id.user_postcode);
        user_city = (TextView) findViewById(R.id.user_city);
        user_state = (TextView) findViewById(R.id.user_state);
        user_country = (TextView) findViewById(R.id.user_country);
        Intent intent2 = getIntent();

        Bundle bundle = intent2.getExtras();
        String first = bundle.getString("first_string");
        String last = bundle.getString("last_string");
        String dob = bundle.getString("date_string");
        String ugender = bundle.getString("gender_string");
        String ustreet = bundle.getString("street_string");
        String uhnum = bundle.getString("hnumber_string");
        String upost = bundle.getString("postcode_string");
        String ucity = bundle.getString("city_string");
        String ustate = bundle.getString("state_string");
        String ucountry = bundle.getString("country_string");


        first_name.setText(first.toString());
        last_name.setText(last.toString());
        date_of_birth.setText(dob.toString());
        user_gender.setText(ugender.toString());
        user_street.setText(ustreet.toString());
        hnumber.setText(""+uhnum.toString());
        user_postcode.setText(""+upost.toString());
        user_city.setText(""+ucity.toString());
        user_state.setText(""+ustate.toString());
        user_country.setText(ucountry.toString());
    }
    @Override
    protected void onResume() {
        super.onResume();
        mt("resume activity2");
    }
    protected void onPause() {
        super.onPause();
        mt("pause activity2");
    }
    protected void onRestart() {
        super.onRestart();
        mt("restart activity2");
    }
    protected void onStart() {
        super.onStart();
        mt("start activity2");
    }
    protected void onStop() {
        super.onStop();
        mt("stop activity2");
    }
    protected void onDestroy() {
        super.onDestroy();
        mt("destroy activity2");
    }
    public void mt(String string){
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    public void newInfo(View view) throws JSONException {
        //TODO check all values are valid
        JSONObject json = new JSONObject();
        json.put("first_name", first_name.getText().toString());
        json.put("last_name", last_name.getText().toString());
        json.put("dob", date_of_birth.getText().toString());
        json.put("gender",user_gender.getText().toString());
        json.put("street", user_street.getText().toString());
        json.put("housenumber", hnumber.getText().toString());
        json.put("postcode", user_postcode.getText().toString());
        json.put("city", user_city.getText().toString());
        json.put("state", user_state.getText().toString());
        json.put("country", user_country.getText().toString());
        try {
            JSONObject response;
            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("addUserBasic", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }
    public void editInfo(View view){
        //THIS METHOD ONLY MAKES ALL FIELDS EXCEPT DOB EDITABLE

        //first_name.setEnabled(true);
        //first_name.setFocusableInTouchMode(true);
        //first_name.setClickable(true);
        last_name.setEnabled(true);
        last_name.setFocusableInTouchMode(true);
        last_name.setClickable(true);
        user_street.setEnabled(true);
        user_street.setFocusableInTouchMode(true);
        user_street.setClickable(true);
        hnumber.setEnabled(true);
        hnumber.setFocusableInTouchMode(true);
        hnumber.setClickable(true);
        user_postcode.setEnabled(true);
        user_postcode.setFocusableInTouchMode(true);
        user_postcode.setClickable(true);
        user_city.setEnabled(true);
        user_city.setFocusableInTouchMode(true);
        user_city.setClickable(true);
        user_state.setEnabled(true);
        user_state.setFocusableInTouchMode(true);
        user_state.setClickable(true);
        user_country.setEnabled(true);
        user_country.setFocusableInTouchMode(true);
        user_country.setClickable(true);
    }

    public void saveEditedInfo(View view) throws JSONException {
        last_name.setEnabled(false);
        last_name.setFocusableInTouchMode(false);
        last_name.setClickable(false);
        user_street.setEnabled(false);
        user_street.setFocusableInTouchMode(false);
        user_street.setClickable(false);
        hnumber.setEnabled(false);
        hnumber.setFocusableInTouchMode(false);
        hnumber.setClickable(false);
        user_postcode.setEnabled(false);
        user_postcode.setFocusableInTouchMode(false);
        user_postcode.setClickable(false);
        user_city.setEnabled(false);
        user_city.setFocusableInTouchMode(false);
        user_city.setClickable(false);
        user_state.setEnabled(false);
        user_state.setFocusableInTouchMode(false);
        user_state.setClickable(false);
        user_country.setEnabled(false);
        user_country.setFocusableInTouchMode(false);
        user_country.setClickable(false);


        JSONObject json = new JSONObject();
        json.put("first_name", first_name.getText().toString());
        json.put("last_name", last_name.getText().toString());
        json.put("dob", date_of_birth.getText().toString());
        json.put("gender",user_gender.getText().toString());
        json.put("street", user_street.getText().toString());
        json.put("housenumber", hnumber.getText().toString());
        json.put("postcode", user_postcode.getText().toString());
        json.put("city", user_city.getText().toString());
        json.put("state", user_state.getText().toString());
        json.put("country", user_country.getText().toString());
        try {
            JSONObject response;
            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("editUserInfo", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }

}