package com.gr03.amos.bikerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gr03.amos.bikerapp.FragmentActivity.ChangePasswordFragment;
import com.gr03.amos.bikerapp.FragmentActivity.CreateEventFragment;
import com.gr03.amos.bikerapp.FragmentActivity.MyChatListFragment;
import com.gr03.amos.bikerapp.FragmentActivity.MyEventListFragment;
import com.gr03.amos.bikerapp.FragmentActivity.MyRouteListFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowEventsFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowFriendsFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowRoutesFragment;

import java.net.URL;
import java.net.URLConnection;

public class ShowEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem menuItem;
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        //checks server connecetion
        if (isConnectedToServer("http://localhost:8080/RESTfulWebserver/", 4000) == false) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No Server Connection");
                final AlertDialog dialog = builder.show();
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                    @SuppressLint("WrongThread")
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        dialog.dismiss();
                    }

                };
                task.execute();
                System.out.println("No server connection, we are in the show dialog branch");
            }
            catch(Exception e){
               System.out.println("No server connection, but doesnt show dialog");
            }
        }

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

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_event);
//        toolbar.setTitle("Shop");

        //set email and username in navigation drawer
        NavigationView navView = findViewById(R.id.nav_view);
        View navHeader = navView.getHeaderView(0);
        TextView txt_email = (TextView) navHeader.findViewById(R.id.txt_email);
        txt_email.setText(SaveSharedPreference.getUserEmail(this));

    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            getSupportActionBar().setTitle("Events");
            if (navigation.getVisibility() == View.GONE) {
                navigation.setVisibility(View.VISIBLE);
            }
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
            Intent intent = new Intent(this, ProfileBasicUserActivity.class);
            intent.putExtra("id", (long) SaveSharedPreference.getUserID(this));
            startActivity(intent);
        }

        if (id == R.id.add_friend) {
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

        if (id == R.id.settings) {
            //TODO implement settings
        } else if (id == R.id.my_event_list) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new MyEventListFragment())
                    .commit();
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }
            
        } else if (id == R.id.my_chat_list) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new MyChatListFragment())
                    .commit();
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }

        } else if (id == R.id.my_route_list) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new MyRouteListFragment())
                    .commit();
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }
            
        } else if (id == R.id.change_password) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ChangePasswordFragment())
                    .addToBackStack("CHANGE_PASSWORD_FRAGMENT")
                    .commit();
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }

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
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }

        } else if (id == R.id.show_friends) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ShowFriendsFragment())
                    .addToBackStack("FRIEND_LIST_FRAGMENT")
                    .commit();
            if (navigation.getVisibility() == View.VISIBLE) {
                navigation.setVisibility(View.GONE);
            }
        } else if (id == R.id.sidebar_logout) {
            SaveSharedPreference.clearSharedPrefrences(this);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.home){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new ShowEventsFragment())
                    .addToBackStack("HOME_FRAGMENT")
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //    bottom


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_event:
//                    toolbar.setTitle("Event Feed");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.create_event_fragment, new ShowEventsFragment())
                            .commit();
                    getSupportActionBar().setTitle("Event Feed");
                    return true;
                case R.id.navigation_route:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.create_event_fragment, new ShowRoutesFragment())
                            .commit();
                    getSupportActionBar().setTitle("Route Feed");
//                    toolbar.setTitle("Route Feed");
                    return true;
            }
            return false;
        }
    };



    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }



}
