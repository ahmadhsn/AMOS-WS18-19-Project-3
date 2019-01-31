package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileBasicUserActivity extends AppCompatActivity implements ResponseHandler{

    Intent intent;
    Long userId;
    TextView user_fname, user_dob, user_gender;
    EditText user_lname, user_street, user_hnumber, user_pcode, user_city, user_state, user_country;

    Button edit_profile_page;
    Button save_edited_info;
    Button add_to_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_basic_user);

        edit_profile_page = findViewById(R.id.editProfilePage);
        save_edited_info = findViewById(R.id.saveEditedInfo);
        add_to_database = findViewById(R.id.addtodatabase);
        edit_profile_page.setVisibility(View.VISIBLE);
        save_edited_info.setVisibility(View.GONE);
        add_to_database.setVisibility(View.GONE);

        user_fname = findViewById(R.id.first_name);
        user_lname = findViewById(R.id.last_name);
        user_dob = findViewById(R.id.dob);
        user_gender = findViewById(R.id.user_gender);
        user_street = findViewById(R.id.user_street);
        user_hnumber = findViewById(R.id.hnumber);
        user_pcode = findViewById(R.id.user_postcode);
        user_city = findViewById(R.id.user_city);
        user_state = findViewById(R.id.user_state);
        user_country = findViewById(R.id.user_country);

        user_fname.setCursorVisible(false);

        intent = getIntent();
        userId = intent.getLongExtra("id", 0);

        if (userId == 0) {
            //intent from add profile page
            //use Roxanas code now
            onCreateAfterAddProfile();
        } else {
            onCreateAfterProfileId();
        }
    }

    private void onCreateAfterAddProfile() {
        Intent intent2 = getIntent();

        Bundle bundle = intent2.getExtras();
        String first = bundle.getString("first_string");
        String last = bundle.getString("last_string");
        String dob = bundle.getString("date_string");
        String gender = bundle.getString("gender_string");
        String street = bundle.getString("street_string");
        String hnum = bundle.getString("hnumber_string");
        String post = bundle.getString("postcode_string");
        String city = bundle.getString("city_string");
        String state = bundle.getString("state_string");
        String country = bundle.getString("country_string");

        user_fname.setText(first);
        user_lname.setText(last);
        user_dob.setText(dob);
        user_gender.setText(gender);
        user_street.setText(street);
        user_hnumber.setText("" + hnum);
        user_pcode.setText("" + post);
        user_city.setText("" + city);
        user_state.setText("" + state);
        user_country.setText(country);

    }

    private void getAdditionalUserInfo() {
        Requests.executeRequest(this, "GET", "getUserInfo/" + userId);
    }

    private void onCreateAfterProfileId() {
        add_to_database.setVisibility(View.GONE);

        //show edit button if it is own users profile
        if (userId == SaveSharedPreference.getUserID(this)) {
            edit_profile_page.setVisibility(View.VISIBLE);
        } else {
            edit_profile_page.setVisibility(View.GONE);
        }
        save_edited_info.setVisibility(View.GONE);

        getAdditionalUserInfo();
    }

    public void newInfo(View view) throws JSONException {
        //button visibility
        edit_profile_page.setVisibility(View.VISIBLE);
        save_edited_info.setVisibility(View.GONE);
        add_to_database.setVisibility(View.GONE);
    }

    public void editInfo(View view) {
        makeFieldsEditable();
        edit_profile_page.setVisibility(View.GONE);
        save_edited_info.setVisibility(View.VISIBLE);

        //makes all fields (except first_name, dob, gender) editable
        user_lname.setEnabled(true);
        user_lname.setFocusableInTouchMode(true);
        user_lname.setClickable(true);
        user_street.setEnabled(true);
        user_street.setFocusableInTouchMode(true);
        user_street.setClickable(true);
        user_hnumber.setEnabled(true);
        user_hnumber.setFocusableInTouchMode(true);
        user_hnumber.setClickable(true);
        user_pcode.setEnabled(true);
        user_pcode.setFocusableInTouchMode(true);
        user_pcode.setClickable(true);
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
        user_lname.setEnabled(false);
        user_lname.setFocusableInTouchMode(false);
        user_lname.setClickable(false);
        user_street.setEnabled(false);
        user_street.setFocusableInTouchMode(false);
        user_street.setClickable(false);
        user_hnumber.setEnabled(false);
        user_hnumber.setFocusableInTouchMode(false);
        user_hnumber.setClickable(false);
        user_pcode.setEnabled(false);
        user_pcode.setFocusableInTouchMode(false);
        user_pcode.setClickable(false);
        user_city.setEnabled(false);
        user_city.setFocusableInTouchMode(false);
        user_city.setClickable(false);
        user_state.setEnabled(false);
        user_state.setFocusableInTouchMode(false);
        user_state.setClickable(false);
        user_country.setEnabled(false);
        user_country.setFocusableInTouchMode(false);
        user_country.setClickable(false);

        validation();

        //JSON request (updating edited info in the database)
        //JSON request (updating edited info in the database)
        JSONObject json = new JSONObject();
        Context context = ProfileBasicUserActivity.this;
        json.put("user_id", SaveSharedPreference.getUserID(context));
        json.put("last_name", user_lname.getText().toString());
        json.put("street", user_street.getText().toString());
        json.put("housenumber", user_hnumber.getText().toString());
        json.put("postcode", user_pcode.getText().toString());
        json.put("city", user_city.getText().toString());
        json.put("state", user_state.getText().toString());
        json.put("country", user_country.getText().toString());

        Requests.executeRequest(this, "POST", "editUserInfo");


        makeFieldsUneditable();
    }

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }

    private void validation() {
        if (isTextEmpty(user_lname)) {
            Log.i("VALIDATIONUSER", "Last name is empty");
            user_lname.setError("Please enter last name");
            return;
        }
        if (isTextEmpty(user_street)) {
            Log.i("VALIDATIONUSER", "Street name is empty");
            user_street.setError("Please enter Street name");
            return;
        }
        if (isTextEmpty(user_hnumber)) {
            Log.i("VALIDATIONUSER", "House number is empty");
            user_hnumber.setError("Please enter house number");
            return;
        }
        if (isTextEmpty(user_pcode)) {
            Log.i("VALIDATIONUSER", "Postcode is empty");
            user_pcode.setError("Please enter postcode");
            return;
        }
        if (isTextEmpty(user_city)) {
            Log.i("VALIDATIONUSER", "City name is empty");
            user_city.setError("Please enter a City name");
            return;
        }
        if (isTextEmpty(user_state)) {
            Log.i("VALIDATIONUSER", "State name is empty");
            user_state.setError("Please enter State name");
            return;
        }
        if (isTextEmpty(user_country)) {
            Log.i("VALIDATIONUSER", "Country name is empty");
            user_country.setError("Please enter Country name");
            return;
        }
    }

    private void makeFieldsEditable(){
        user_lname.setBackgroundResource(android.R.drawable.edit_text);
        user_street.setBackgroundResource(android.R.drawable.edit_text);
        user_hnumber.setBackgroundResource(android.R.drawable.edit_text);
        user_pcode.setBackgroundResource(android.R.drawable.edit_text);
        user_city.setBackgroundResource(android.R.drawable.edit_text);
        user_country.setBackgroundResource(android.R.drawable.edit_text);
        user_state.setBackgroundResource(android.R.drawable.edit_text);
        user_country.setBackgroundResource(android.R.drawable.edit_text);
    }

    private void makeFieldsUneditable(){
        user_lname.setBackgroundResource(0);
        user_street.setBackgroundResource(0);
        user_hnumber.setBackgroundResource(0);
        user_pcode.setBackgroundResource(0);
        user_city.setBackgroundResource(0);
        user_country.setBackgroundResource(0);
        user_state.setBackgroundResource(0);
        user_country.setBackgroundResource(0);
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if(SocketUtility.hasSocketError(response)){
            Toast.makeText(getApplicationContext(), "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }


        //check for urlTail
        if(urlTail.equals("getUserInfo/" + userId)){
            onResponseUserInfo(response);
        }else if(urlTail.equals("editUserInfo")){
            onResponseSaveEditedInfo(response);
        }
    }

    /**
     * Response handling for service "getUserInfo".
     *
     * Updates edit text views for profile information.
     *
     * @param response JSONObject of response
     */
    private void onResponseUserInfo(JSONObject response){
        try {
            response = response.getJSONObject("UserInfo");
            user_fname.setText(response.getString("first_name"));
            user_lname.setText(response.getString("last_name"));
            user_dob.setText(response.getString("dob"));
            user_gender.setText(response.getString("gender"));

            JSONObject address = response.getJSONObject("address");
            user_street.setText(address.getString("street"));
            user_hnumber.setText(address.getString("housenumber"));
            user_pcode.setText(address.getString("postcode"));
            user_city.setText(address.getString("city"));
            user_state.setText(address.getString("state"));
            user_country.setText(address.getString("country"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onResponseSaveEditedInfo(JSONObject response){
        try {
            if(response.has("editUserInfo")){
                String statusReg = (String) response.get("editUserInfo");

                if(statusReg.equals("successfulUpdation")){
                    Log.i("EditUserInfoBasic","successfulUpdation");
                    Toast toast = Toast.makeText(this, "You have successfully saved your Profile after editing!", Toast.LENGTH_SHORT);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                    finish();
                }
            }
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }


    }
}