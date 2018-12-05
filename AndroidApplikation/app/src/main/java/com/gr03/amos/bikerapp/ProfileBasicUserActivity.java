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

        //intent = getIntent();
        //userId = intent.getLongExtra("id", 0);

        //Realm.init(this);
        //Realm realm = Realm.getDefaultInstance();

        //final ProfileBasic basic_user = realm.where(ProfileBasic.class).equalTo("id_user", userId).findFirst();

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
       // String ugenderm = bundle.getString("male_string");
        // String ugenderf = bundle.getString("female_string");
        String ustreet = bundle.getString("street_string");
        String uhnum = bundle.getString("hnumber_string");
        String upost = bundle.getString("postcode_string");
        String ucity = bundle.getString("city_string");
        String ustate = bundle.getString("state_string");
        String ucountry = bundle.getString("country_string");


        first_name.setText(": "+first.toString());
        last_name.setText(": "+last.toString());
        date_of_birth.setText(": "+dob.toString());
        //user_gender.setText(": "+ugenderm.toString());
        user_street.setText(": "+ustreet.toString());
        hnumber.setText(": "+uhnum.toString());
        user_postcode.setText(": "+upost.toString());
        user_city.setText(": "+ucity.toString());
        user_state.setText(": "+ustate.toString());
        user_country.setText(": "+ucountry.toString());




        /*user_gender = (TextView) findViewById(R.id.choose_gender);
        user_street = (TextView) findViewById(R.id.street);
        hnumber = (TextView) findViewById(R.id.hnumber);
        user_postcode = (TextView) findViewById(R.id.postcode);
        user_city = (TextView) findViewById(R.id.city);
        user_country = (TextView) findViewById(R.id.country);*/

        //first_name.setText(basic_user.getFirstName());
        //last_name.setText(basic_user.getLastName());
        //date_of_birth.setText(basic_user.getDateOfBirth());
       /* user_gender.setText(basic_user.getGender());
        user_street.setText(basic_user.getStreet());
        hnumber.setText(basic_user.getNumber());
        user_postcode.setText(basic_user.getPostcode());
        user_city.setText(basic_user.getCity());
        user_country.setText(basic_user.getCountry());*/

        /*final ImageButton button1 = findViewById(R.id.editProfilePage);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddProfileBasicUserActivity.class);
            intent.putExtra("id", userId);
            this.startActivity(intent);
        });*/


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
    //protected void onResume() {
    //  super.onResume();
    //mt("resume activity");
    //}
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

}