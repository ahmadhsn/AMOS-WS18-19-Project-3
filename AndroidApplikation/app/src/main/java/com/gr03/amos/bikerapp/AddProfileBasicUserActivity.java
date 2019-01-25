package com.gr03.amos.bikerapp;
        import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
        import android.view.Gravity;
        import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

;

public class AddProfileBasicUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText FName, LName, Dob, Street, HNumber, Postcode, City, State, Country;
    TextView choose_gender;
    RadioGroup radio;
    RadioButton male, female;
    Button add;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_basic_user2);

        FName = findViewById(R.id.fname);
        LName = findViewById(R.id.lname);
        Dob = findViewById(R.id.dob);
        Street = findViewById(R.id.street);
        HNumber = findViewById(R.id.hnumber);
        Postcode = findViewById(R.id.postcode);
        City = findViewById(R.id.city);
        State = findViewById(R.id.bstate);
        Country = findViewById(R.id.country);

        choose_gender = findViewById(R.id.choose_gender);
        radio = findViewById(R.id.radio);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        add = findViewById(R.id.add);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        Dob.setOnClickListener(v -> {
            showDatePicker(1980, 0, 1);
        });

        add.setOnClickListener(v -> {

            //Validation, can't be extracted to an own method, causes problems with intents
            if (isTextEmpty(FName)) {
                Log.i("VALIDATIONUSER", "First name is empty");
                FName.setError("Please enter a First name");
                return;
            }
            if (isTextEmpty(LName)) {
                Log.i("VALIDATIONUSER", "last name is empty");
                LName.setError("Please enter a Last name");
                return;
            }
            if (isTextEmpty(Dob)) {
                Log.i("VALIDATIONUSER", "Date of birth is empty");
                Dob.setError("Please enter date of birth");
                return;
            }
            if (isTextEmpty(Street)) {
                Log.i("VALIDATIONUSER", "Street name is empty");
                Street.setError("Please enter Street name");
                return;
            }
            if (isTextEmpty(HNumber)) {
                Log.i("VALIDATIONUSER", "House number is empty");
                HNumber.setError("Please enter House num");
                return;
            }
            if (isTextEmpty(Postcode)) {
                Log.i("VALIDATIONUSER", "Postcode is empty");
                Postcode.setError("Please enter Post code");
                return;
            }
            if (isTextEmpty(City)) {
                Log.i("VALIDATIONUSER", "City name is empty");
                City.setError("Please enter a City name");
                return;
            }
            if (isTextEmpty(State)) {
                Log.i("VALIDATIONEVENT", "State name is empty");
                State.setError("Please enter State name");
                return;
            }
            if (isTextEmpty(Country)) {
                Log.i("VALIDATIONUSER", "Country name is empty");
                Country.setError("Please enter Country name");
                return;
            }
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            String DateOfBirth = Dob.getText().toString();
            //String system_Date = (year + "/" + (month+1) + "/" + day).toString();

            if (Integer.parseInt(DateOfBirth.split("/")[0])> year-16 ){
                Log.i("VALIDATIONUSER", "User is younger than 16");
                Dob.setError("You must be at least 16 years old to use this app.");
                Toast.makeText(getApplicationContext(),"You must be at least 16 years old to use this app.",Toast.LENGTH_LONG).show();
                return;
            }
            if (Integer.parseInt(DateOfBirth.split("/")[0]) == year-16){
                if (Integer.parseInt(DateOfBirth.split("/")[1]) > month+1){
                    Log.i("VALIDATIONUSER", "User is younger than 16");
                    Dob.setError("You must be at least 16 years old to use this app.");
                    Toast.makeText(getApplicationContext(),"You must be at least 16 years old to use this app.",Toast.LENGTH_LONG).show();
                    return;
                }
                if (Integer.parseInt(DateOfBirth.split("/")[1]) == month+1){
                    if (Integer.parseInt(DateOfBirth.split("/")[2]) > day){
                        Log.i("VALIDATIONUSER", "User is younger than 16");
                        Dob.setError("You must be at least 16 years old to use this app.");
                        Toast.makeText(getApplicationContext(),"You must be at least 16 years old to use this app.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            try {
                newInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            intentHandling();

        });
    }

    public void newInfo() throws JSONException{

        String Gender;

        if(male.isChecked()) {
            Gender = "M";
        } else {
            Gender = "F";
        }
        //JSON request (first time insertion of user information to database)
        JSONObject json = new JSONObject();
        Context context = AddProfileBasicUserActivity.this;
        json.put("user_id", SaveSharedPreference.getUserID(context));
        json.put("first_name", FName.getText().toString());
        json.put("last_name", LName.getText().toString());
        json.put("dob", Dob.getText().toString());
        json.put("gender", Gender);
        json.put("street", Street.getText().toString());
        json.put("housenumber", HNumber.getText().toString());
        json.put("postcode", Postcode.getText().toString());
        json.put("city", City.getText().toString());
        json.put("state", State.getText().toString());
        json.put("country", Country.getText().toString());

        try {
            JSONObject response = Requests.getJSONResponse("addUserBasic", json, "POST");

            if(response.has("addUserBasic")){
                String statusReg = (String) response.get("addUserBasic");

                if(statusReg.equals("successfullCreation")){
                    Log.i("AddProfileBasicUser","successfullCreation");
                    Toast toast = Toast.makeText(this, "You have successfully added a Profile!", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                    finish();
                }
            }
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }

    private void intentHandling(){
        Intent intent = new Intent(AddProfileBasicUserActivity.this, ShowEventActivity.class);
        startActivity(intent);

        Intent intent1 = new Intent(AddProfileBasicUserActivity.this , ProfileBasicUserActivity.class);
        intent1.putExtra("first_string",FName.getText().toString());
        intent1.putExtra("last_string",LName.getText().toString());
        intent1.putExtra("date_string",Dob.getText().toString());
        intent1.putExtra("street_string",Street.getText().toString());
        intent1.putExtra("hnumber_string",HNumber.getText().toString());
        intent1.putExtra("postcode_string",Postcode.getText().toString());
        intent1.putExtra("city_string",City.getText().toString());
        intent1.putExtra("state_string",State.getText().toString());
        intent1.putExtra("country_string",Country.getText().toString());
        if(male.isChecked()) {
            intent1.putExtra("gender_string", "M");
        } else {
            intent1.putExtra("gender_string", "F");
        }
        startActivityForResult(intent1,1);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Dob.setText(year+"/"+(month +1)+"/"+day);
    }

    public void showDatePicker(int year, int month, int day) {
        new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day)
                .show();
    }

    boolean isTextEmpty(EditText text){
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }
}
