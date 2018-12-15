package com.gr03.amos.bikerapp;
        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.Toast;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Button;
        import android.widget.Spinner;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.GregorianCalendar;
        import java.util.Locale;
        import java.util.concurrent.Callable;
        import java.util.concurrent.FutureTask;
        import  android.text.TextUtils;
        import static android.view.View.VISIBLE;

public class AddProfileBasicUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText fName;
    EditText lName;
    EditText Dob;
    TextView choose_gender;
    RadioGroup radio;
    RadioButton male;
    RadioButton female;
    EditText Street;
    EditText HNumber;
    EditText PostCode;
    EditText City;
    EditText BState;
    EditText Country;
    Button add;
    TextView textView;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mt("create activity1");
        setContentView(R.layout.activity_add_profile_basic_user2);

        fName = findViewById(R.id.fname);
        lName = findViewById(R.id.lname);
        Dob = findViewById(R.id.dob);
        choose_gender = findViewById(R.id.choose_gender);
        radio = findViewById(R.id.radio);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        Street = findViewById(R.id.street);
        HNumber = findViewById(R.id.hnumber);
        PostCode = findViewById(R.id.postcode);
        City = findViewById(R.id.city);
        BState = findViewById(R.id.bstate);
        Country = findViewById(R.id.country);
        add = findViewById(R.id.add);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Dob.setOnClickListener(v -> {
            showDatePicker(1980, 0, 1);
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTextEmpty(fName)) {
                    Log.i("VALIDATIONEVENT", "First name is empty");
                    fName.setError("Please enter a First name");
                    return;
                }
                if (isTextEmpty(lName)) {
                    Log.i("VALIDATIONEVENT", "last name is empty");
                    lName.setError("Please enter a Last name");
                    return;
                }
                if (isTextEmpty(Dob)) {
                    Log.i("VALIDATIONEVENT", "Date of birth is empty");
                    Dob.setError("Please enter date of birth");
                    return;
                }
                if (isTextEmpty(Street)) {
                    Log.i("VALIDATIONEVENT", "Street name is empty");
                    Street.setError("Please enter Street name");
                    return;
                }
                if (isTextEmpty(HNumber)) {
                    Log.i("VALIDATIONEVENT", "House number is empty");
                    HNumber.setError("Please enter House num");
                    return;
                }
                if (isTextEmpty(PostCode)) {
                    Log.i("VALIDATIONEVENT", "Postcode is empty");
                    PostCode.setError("Please enter Post code");
                    return;
                }
                if (isTextEmpty(City)) {
                    Log.i("VALIDATIONEVENT", "City name is empty");
                    PostCode.setError("Please enter a City name");
                    return;
                }
                if (isTextEmpty(BState)) {
                    Log.i("VALIDATIONEVENT", "State name is empty");
                    PostCode.setError("Please enter State name");
                    return;
                }
                if (isTextEmpty(Country)) {
                    Log.i("VALIDATIONEVENT", "Country name is empty");
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

                Intent intent = new Intent(AddProfileBasicUserActivity.this, ShowEventActivity.class);
                startActivity(intent);

                Intent intent1 = new Intent(AddProfileBasicUserActivity.this , ProfileBasicUserActivity.class);
                 intent1.putExtra("first_string",fName.getText().toString());
                intent1.putExtra("last_string",lName.getText().toString());
                intent1.putExtra("date_string",Dob.getText().toString());
                if(male.isChecked()) {
                    intent1.putExtra("gender_string", "M");
                } else {
                    intent1.putExtra("gender_string", "F");
                }
                intent1.putExtra("street_string",Street.getText().toString());
                intent1.putExtra("hnumber_string",HNumber.getText().toString());
                intent1.putExtra("postcode_string",PostCode.getText().toString());
                intent1.putExtra("city_string",City.getText().toString());
                intent1.putExtra("state_string",BState.getText().toString());
                intent1.putExtra("country_string",Country.getText().toString());
                startActivityForResult(intent1,1);

            }
        });
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Dob.setText(year+"/"+(month +1)+"/"+day);
    }
    public void showDatePicker(int year, int month, int day) {
        new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day)
                .show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     textView.setText(data.getStringExtra("confirmation"));
    }

    boolean isTextEmpty(EditText text){
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }
    protected void onResume() {
        super.onResume();
        mt("resume activity");
    }
    protected void onPause() {
        super.onPause();
        mt("pause activity");
    }
    protected void onRestart() {
        super.onRestart();
        mt("restart activity");
    }
    protected void onStart() {
        super.onStart();
        mt("start activity");
    }
    protected void onStop() {
        super.onStop();
        mt("stop activity");
    }
    protected void onDestroy() {
        super.onDestroy();
        mt("destroy activity");
    }
    public void mt(String string){
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
