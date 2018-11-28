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

public class EventDetailsActivity extends AppCompatActivity {

    Intent intent;
    Long eventId;
    TextView event_name, event_description, event_date, event_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        intent = getIntent();
        eventId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Event event = realm.where(Event.class).equalTo("id_event", eventId).findFirst();

        event_name = (TextView) findViewById(R.id.event_name);
        event_description = (TextView) findViewById(R.id.event_description);
        event_date = (TextView) findViewById(R.id.event_date);
        event_time = (TextView) findViewById(R.id.event_time);

        event_name.setText(event.getName());
        event_description.setText(event.getDescription());
        event_date.setText(event.getDate());
        event_time.setText(event.getTime());

        final ImageButton button1 = findViewById(R.id.editEventButton);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditEventActivity.class);
            intent.putExtra("id", eventId);
            this.startActivity(intent);
        });

        final ImageButton button2 = findViewById(R.id.DeleteEventButton);
        button2.setOnClickListener(view -> {
            try {
                deleteEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Realm realmDelete = Realm.getDefaultInstance();

            final Event event1 = realmDelete.where(Event.class).equalTo("id_event", eventId).findFirst();
            Log.i("Before Deletion from Realm 1", event1.getName()+  " Date : " + event1.getDate() + " Time : " + event1.getDate()  );

            realmDelete.beginTransaction();
            event1.deleteFromRealm();
            Log.i("After Transaction from Realm 1", "Deleted" );
            realmDelete.commitTransaction();
            realmDelete.close();
            this.finish();
        });
    }


    public void deleteEvent() throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id_event", eventId);

        String ewe = eventId.toString();
        Log.i("Delete Event Activity", ewe );

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                JSONObject threadResponse = Requests.getResponse("deleteEvent", json);
                return threadResponse.toString();
            });

            new Thread(task).start();
            Log.i("Response", task.get());
            Intent intent = new Intent(this, ShowEventActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }
}