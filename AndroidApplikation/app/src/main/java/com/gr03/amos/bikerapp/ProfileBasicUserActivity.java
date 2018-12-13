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
        import com.gr03.amos.bikerapp.Models.Friend;
        import com.gr03.amos.bikerapp.Models.ProfileBasic;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.concurrent.Callable;
        import java.util.concurrent.FutureTask;

        import io.realm.Realm;

public class ProfileBasicUserActivity extends AppCompatActivity {

    Intent intent;
    Long userId;
    TextView first_name, date_of_birth, user_gender;
    EditText last_name, user_street, hnumber, user_postcode, user_city, user_state, user_country;

    private void onCreateAfterAddProfile(Bundle savedInstanceState){
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

    private JSONObject getAdditionalUserInfo(){
        JSONObject userInfo;
        try {
            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("getUserInfo/" + userId, null, "GET");
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            userInfo = new JSONObject(task.get());
            userInfo = userInfo.getJSONObject("UserInfo");
            date_of_birth.setText(userInfo.getString("dob"));
            user_gender.setText(userInfo.getString("gender"));

            JSONObject address = userInfo.getJSONObject("address");
            user_street.setText(address.getString("street"));
            hnumber.setText(address.getString("housenumber"));
            user_postcode.setText(address.getString("postcode"));
            user_city.setText(address.getString("city"));
            user_state.setText(address.getString("state"));
            user_country.setText(address.getString("country"));
        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
            return null;
        }

        return userInfo;
    }

    private void onCreateAfterProfileId(){
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Friend friend = realm.where(Friend.class).equalTo("id", userId).findFirst();

        first_name.setText(friend.getFirst_name());
        last_name.setText(friend.getLast_name());

        Button btDatabase = findViewById(R.id.addtodatabase);
        btDatabase.setVisibility(View.INVISIBLE);
        //TODO if logged in user show edit button
        Button btEdit = findViewById(R.id.editProfilePage);
        btEdit.setVisibility(View.INVISIBLE);
        Button btSave = findViewById(R.id.saveEditedInfo);
        btSave.setVisibility(View.INVISIBLE);

        JSONObject userInfo = getAdditionalUserInfo();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mt("create activity2");
        setContentView(R.layout.activity_profile_basic_user);

        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        date_of_birth = (TextView) findViewById(R.id.dob);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_street = (EditText) findViewById(R.id.user_street);
        hnumber = (EditText) findViewById(R.id.hnumber);
        user_postcode = (EditText) findViewById(R.id.user_postcode);
        user_city = (EditText) findViewById(R.id.user_city);
        user_state = (EditText) findViewById(R.id.user_state);
        user_country = (EditText) findViewById(R.id.user_country);

        intent = getIntent();
        userId = intent.getLongExtra("id", 0);

        if(userId == 0){
            //intent from add profile page
            //use Roxanas code now
            onCreateAfterAddProfile(savedInstanceState);
        }else{
            onCreateAfterProfileId();
        }

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
        //button visibility
        Button edit_profile_page=(Button)findViewById(R.id.editProfilePage);
        Button save_edited_info=(Button)findViewById(R.id.saveEditedInfo);
        Button add_to_database=(Button)findViewById(R.id.addtodatabase);
        edit_profile_page.setVisibility(View.VISIBLE);
        save_edited_info.setVisibility(View.VISIBLE);
        add_to_database.setVisibility(View.INVISIBLE);

        //JSON request (first time insertion of user information to database)
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

        //makes all fields (except first_name, dob, gender) editable
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

        //makes all fields non-editable again
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

        //JSON request (updating edited info in the database)
        JSONObject json = new JSONObject();
        json.put("last_name", last_name.getText().toString());
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