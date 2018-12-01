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

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.concurrent.Callable;
        import java.util.concurrent.FutureTask;

        import io.realm.Realm;

public class ProfileBasicUserActivity extends AppCompatActivity {

    Intent intent;
    Long userId;
    TextView first_name, last_name, date_of_birth, user_gender, user_street, hnumber, user_postcode, user_city, user_state, user_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_basic_user);

        intent = getIntent();
        userId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final ProfileB basic_user = realm.where(ProfileB.class).equalTo("id_user", userId).findFirst();

        first_name = (TextView) findViewById(R.id.event_name);
        last_name = (TextView) findViewById(R.id.event_description);
        date_of_birth = (TextView) findViewById(R.id.event_date);
        user_gender = (TextView) findViewById(R.id.event_time);
        user_street = (TextView) findViewById(R.id.user_street);
        hnumber = (TextView) findViewById(R.id.hnumber);
        user_postcode = (TextView) findViewById(R.id.user_postcode);
        user_city = (TextView) findViewById(R.id.user_city);
        user_country = (TextView) findViewById(R.id.user_country);



        first_name.setText(basic_user.getFirstName());
        last_name.setText(basic_user.getLastName());
        date_of_birth.setText(basic_user.getDateOfBirth());
        user_gender.setText(basic_user.getGender());
        user_street.setText(basic_user.getstreet());
        hnumber.setText(basic_user.getstreet());
        hnumber.setText(basic_user.getstreet());
        user_postcode.setText(basic_user.getstreet());
        user_city.setText(basic_user.getstreet());
        user_country.setText(basic_user.getstreet());

        final ImageButton button1 = findViewById(R.id.editProfilePage);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditEventActivity.class);
            intent.putExtra("id", userId);
            this.startActivity(intent);
        });


    }

}