package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gr03.amos.bikerapp.FragmentActivity.ChangePasswordFragment;
import com.gr03.amos.bikerapp.FragmentActivity.CreateEventFragment;
import com.gr03.amos.bikerapp.FragmentActivity.MyEventListFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowEventsFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowFriendsFragment;
import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.Event;

import io.realm.Realm;

public class ShowEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        if (SaveSharedPreference.getUserEmail(this).length() == 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Feed");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuItem = navigationView.getMenu().findItem(R.id.show_events);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);

    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            getSupportActionBar().setTitle("Events");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SaveSharedPreference.clearSharedPrefrences(this);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        if (id == R.id.show_profile) {
            Intent intent = new Intent(this, AddProfileBasicUserActivity.class);
            startActivity(intent);
        }

        if (id== R.id.add_friend) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ShowFriendsFragment())
                    .addToBackStack("FRIEND_LIST_FRAGMENT")
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_events) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ShowEventsFragment())
                    .commit();
        } else if (id == R.id.my_event_list) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new MyEventListFragment())
                    .commit();
        } else if (id == R.id.settings) {
            //TODO implement settings
        } else if (id == R.id.change_password) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ChangePasswordFragment())
                    .addToBackStack("CHANGE_PASSWORD_FRAGMENT")
                    .commit();
        } else if (id == R.id.add_profile) {
            Intent intent = new Intent(this, AddProfileBasicUserActivity.class);
            startActivity(intent);
        } else if (id == R.id.add_route) {
            Intent intent = new Intent(this, AddRoute.class);
            startActivity(intent);
        } else if (id == R.id.action_add_event) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new CreateEventFragment())
                    .addToBackStack("CREATE_EVENT_FRAGMENT")
                    .commit();
        } else if (id == R.id.show_friends) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ShowFriendsFragment())
                    .addToBackStack("FRIEND_LIST_FRAGMENT")
                    .commit();
        } else if (id == R.id.sidebar_logout) {
            SaveSharedPreference.clearSharedPrefrences(this);
            Intent intent = new Intent( this, HomeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
