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

        //Intent intent = new Intent(this, ProfileBasicUserActivity.class);


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
                Intent intent1 = new Intent(AddProfileBasicUserActivity.this , ProfileBasicUserActivity.class);
                 intent1.putExtra("first_string",fName.getText().toString());
                intent1.putExtra("last_string",lName.getText().toString());
                intent1.putExtra("date_string",Dob.getText().toString());
                //intent1.putExtra("male_string",male.getText().toString());
                //intent1.putExtra("female_string",female.getText().toString());
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
    //protected void onResume() {
      //  super.onResume();
        //mt("resume activity");
    //}
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

    public void newInfo(View view) throws JSONException {
        //TODO check all values are valid
        JSONObject json = new JSONObject();
        json.put("first_name", fName.getText().toString());
        json.put("last_name", lName.getText().toString());
        json.put("dob", Dob.getText().toString());
        json.put("street", Street.getText().toString());
        json.put("housenumber", HNumber.getText().toString());
        json.put("postcode", PostCode.getText().toString());
        json.put("city", City.getText().toString());
        json.put("state", BState.getText().toString());
        json.put("country", Country.getText().toString());

        if(male.isChecked()) {
            json.put("gender", "M");
        } else {
            json.put("gender", "F");
        }

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
}
