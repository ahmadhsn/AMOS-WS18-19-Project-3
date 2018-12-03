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

        import com.gr03.amos.bikerapp.Models.Event;
        import com.gr03.amos.bikerapp.Models.ProfileBasic;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.concurrent.Callable;
        import java.util.concurrent.FutureTask;

        import io.realm.Realm;

public class ProfileBasicUserActivity extends AppCompatActivity {

    Intent intent;
    Long userId;
    TextView first_name, last_name, date_of_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_basic_user);

        intent = getIntent();
        userId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final ProfileBasic basic_user = realm.where(ProfileBasic.class).equalTo("id_user", userId).findFirst();

        first_name = (TextView) findViewById(R.id.fname);
        last_name = (TextView) findViewById(R.id.lname);
        date_of_birth = (TextView) findViewById(R.id.dob);
        /*user_gender = (TextView) findViewById(R.id.choose_gender);
        user_street = (TextView) findViewById(R.id.street);
        hnumber = (TextView) findViewById(R.id.hnumber);
        user_postcode = (TextView) findViewById(R.id.postcode);
        user_city = (TextView) findViewById(R.id.city);
        user_country = (TextView) findViewById(R.id.country);*/

        first_name.setText(basic_user.getFirstName());
        last_name.setText(basic_user.getLastName());
        date_of_birth.setText(basic_user.getDateOfBirth());
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

}