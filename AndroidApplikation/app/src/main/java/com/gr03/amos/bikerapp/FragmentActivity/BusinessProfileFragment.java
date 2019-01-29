package com.gr03.amos.bikerapp.FragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessProfileFragment extends Fragment implements ResponseHandler {

    Long userId;

    EditText editBusinessName;
    EditText editBusinessDescr;
    EditText editBusinessStreet;
    EditText editBusinessHnumber;
    EditText editBusinessPostcode;
    EditText editBusinessCity;
    EditText editBusinessState;
    EditText editBusinessCountry;

    Button editProfile;
    Button saveProfile;

    KeyListener listenerName;
    KeyListener listenerDescr;
    KeyListener listenerStreet;
    KeyListener listenerHnumber;
    KeyListener listenerPostcode;
    KeyListener listenerCity;
    KeyListener listenerState;
    KeyListener listenerCountry;


    public BusinessProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getLong("id", 0);
        }

        editBusinessName = view.findViewById(R.id.business_name);
        editBusinessDescr = view.findViewById(R.id.business_descr);
        editBusinessStreet = view.findViewById(R.id.business_street);
        editBusinessHnumber = view.findViewById(R.id.business_hnumber);
        editBusinessPostcode = view.findViewById(R.id.business_postcode);
        editBusinessCity = view.findViewById(R.id.business_city);
        editBusinessState = view.findViewById(R.id.business_state);
        editBusinessCountry = view.findViewById(R.id.business_country);

        editProfile = view.findViewById(R.id.editBusinessProfile);
        saveProfile = view.findViewById(R.id.saveBusinessProfile);
        //set onClickListener
        editProfile.setOnClickListener(v -> editInfo());
        saveProfile.setOnClickListener(v -> saveEditedInfo());

        initKeyListener();

        if (userId == SaveSharedPreference.getUserID(getContext())) {
            editProfile.setVisibility(View.VISIBLE);

            Activity currActivity = getActivity();
            //Set Title to My Profile
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("My profile");
        }

        loadProfile();
        makeFieldsUneditable();

        return view;
    }

    private void loadProfile() {
        Requests.executeRequest(this, "GET", "getBusinessProfile/" + userId);

    }

    public void editInfo() {
        makeFieldsEditable();
        editProfile.setVisibility(View.GONE);
        saveProfile.setVisibility(View.VISIBLE);
    }

    public void saveEditedInfo() {
        boolean validated = validation();
        if (!validated) {
            return;
        }

        //JSON request (updating edited info in the database)
        JSONObject json = new JSONObject();
        try {
            json.put("user_id", SaveSharedPreference.getUserID(this.getContext()));
            json.put("business_name", editBusinessName.getText().toString());
            json.put("business_description", editBusinessDescr.getText().toString());
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("street", editBusinessStreet.getText().toString());
            jsonAddress.put("housenumber", editBusinessHnumber.getText().toString());
            jsonAddress.put("postcode", editBusinessPostcode.getText().toString());
            jsonAddress.put("city", editBusinessCity.getText().toString());
            jsonAddress.put("state", editBusinessState.getText().toString());
            jsonAddress.put("country", editBusinessCountry.getText().toString());
            json.put("business_address", jsonAddress);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        Requests.executeRequest(this, "POST", "editBusinessProfile", json);

        makeFieldsUneditable();
        editProfile.setVisibility(View.VISIBLE);
        saveProfile.setVisibility(View.GONE);
    }

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }

    private boolean validation() {
        if (isTextEmpty(editBusinessName)) {
            Log.i("VALIDATIONBUSINESSUSER", "Business Name is empty");
            editBusinessName.setError("Please enter business name");
            return false;
        }
        if (isTextEmpty(editBusinessDescr)) {
            Log.i("VALIDATIONBUSINESSUSER", "Business Description is empty");
            editBusinessDescr.setError("Please enter a description");
            return false;
        }
        if (isTextEmpty(editBusinessStreet)) {
            Log.i("VALIDATIONBUSINESSUSER", "Street name is empty");
            editBusinessStreet.setError("Please enter Street name");
            return false;
        }
        if (isTextEmpty(editBusinessHnumber)) {
            Log.i("VALIDATIONBUSINESSUSER", "House number is empty");
            editBusinessHnumber.setError("Please enter house number");
            return false;
        }
        if (isTextEmpty(editBusinessPostcode)) {
            Log.i("VALIDATIONBUSINESSUSER", "Postcode is empty");
            editBusinessPostcode.setError("Please enter postcode");
            return false;
        }
        if (isTextEmpty(editBusinessCity)) {
            Log.i("VALIDATIONBUSINESSUSER", "City name is empty");
            editBusinessCity.setError("Please enter a City name");
            return false;
        }
        if (isTextEmpty(editBusinessState)) {
            Log.i("VALIDATIONBUSINESSUSER", "State name is empty");
            editBusinessState.setError("Please enter State name");
            return false;
        }
        if (isTextEmpty(editBusinessCountry)) {
            Log.i("VALIDATIONBUSINESSUSER", "Country name is empty");
            editBusinessCountry.setError("Please enter Country name");
            return false;
        }

        return true;
    }


    private void makeFieldsEditable() {

        editBusinessCountry.setKeyListener(listenerCountry);
        editBusinessCity.setKeyListener(listenerCity);
        editBusinessState.setKeyListener(listenerState);
        editBusinessPostcode.setKeyListener(listenerPostcode);
        editBusinessHnumber.setKeyListener(listenerHnumber);
        editBusinessStreet.setKeyListener(listenerStreet);
        editBusinessDescr.setKeyListener(listenerDescr);
        editBusinessName.setKeyListener(listenerName);

        //makes all fields editable
//        editBusinessName.setEnabled(true);
//        editBusinessName.setFocusableInTouchMode(true);
//        editBusinessName.setClickable(true);
//        editBusinessDescr.setEnabled(true);
//        editBusinessDescr.setFocusableInTouchMode(true);
//        editBusinessDescr.setClickable(true);
//        editBusinessStreet.setEnabled(true);
//        editBusinessStreet.setFocusableInTouchMode(true);
//        editBusinessStreet.setClickable(true);
//        editBusinessHnumber.setEnabled(true);
//        editBusinessHnumber.setFocusableInTouchMode(true);
//        editBusinessHnumber.setClickable(true);
//        editBusinessPostcode.setEnabled(true);
//        editBusinessPostcode.setFocusableInTouchMode(true);
//        editBusinessPostcode.setClickable(true);
//        editBusinessCity.setEnabled(true);
//        editBusinessCity.setFocusableInTouchMode(true);
//        editBusinessCity.setClickable(true);
//        editBusinessState.setEnabled(true);
//        editBusinessState.setFocusableInTouchMode(true);
//        editBusinessState.setClickable(true);
//        editBusinessCountry.setEnabled(true);
//        editBusinessCountry.setFocusableInTouchMode(true);
//        editBusinessCountry.setClickable(true);

        editBusinessName.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessDescr.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessStreet.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessHnumber.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessPostcode.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessCity.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessCountry.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessState.setBackgroundResource(android.R.drawable.edit_text);
        editBusinessName.setHint("Business Name");
        editBusinessDescr.setHint("Describe your business");
        editBusinessStreet.setHint("street");
        editBusinessHnumber.setHint("Housenumber");
        editBusinessPostcode.setHint("Postcode");
        editBusinessCity.setHint("City");
        editBusinessCountry.setHint("Country");
        editBusinessState.setHint("State");
    }

    private void makeFieldsUneditable() {
        //makes all fields non-editable again

        editBusinessCountry.setKeyListener(null);
        editBusinessCity.setKeyListener(null);
        editBusinessState.setKeyListener(null);
        editBusinessPostcode.setKeyListener(null);
        editBusinessHnumber.setKeyListener(null);
        editBusinessStreet.setKeyListener(null);
        editBusinessDescr.setKeyListener(null);
        editBusinessName.setKeyListener(null);

//        editBusinessName.setEnabled(false);
//        editBusinessName.setFocusableInTouchMode(false);
//        editBusinessName.setClickable(false);
//        editBusinessDescr.setEnabled(false);
//        editBusinessDescr.setFocusableInTouchMode(false);
//        editBusinessDescr.setClickable(false);
//        editBusinessStreet.setEnabled(false);
//        editBusinessStreet.setFocusableInTouchMode(false);
//        editBusinessStreet.setClickable(false);
//        editBusinessHnumber.setEnabled(false);
//        editBusinessHnumber.setFocusableInTouchMode(false);
//        editBusinessHnumber.setClickable(false);
//        editBusinessPostcode.setEnabled(false);
//        editBusinessPostcode.setFocusableInTouchMode(false);
//        editBusinessPostcode.setClickable(false);
//        editBusinessCity.setEnabled(false);
//        editBusinessCity.setFocusableInTouchMode(false);
//        editBusinessCity.setClickable(false);
//        editBusinessState.setEnabled(false);
//        editBusinessState.setFocusableInTouchMode(false);
//        editBusinessState.setClickable(false);
//        editBusinessCountry.setEnabled(false);
//        editBusinessCountry.setFocusableInTouchMode(false);
//        editBusinessCountry.setClickable(false);

        editBusinessName.setBackgroundResource(0);
        editBusinessDescr.setBackgroundResource(0);
        editBusinessStreet.setBackgroundResource(0);
        editBusinessHnumber.setBackgroundResource(0);
        editBusinessPostcode.setBackgroundResource(0);
        editBusinessCity.setBackgroundResource(0);
        editBusinessCountry.setBackgroundResource(0);
        editBusinessState.setBackgroundResource(0);
    }

    private void initKeyListener() {
        listenerCity = editBusinessCity.getKeyListener();
        listenerCountry = editBusinessCountry.getKeyListener();
        listenerDescr = editBusinessDescr.getKeyListener();
        listenerHnumber = editBusinessHnumber.getKeyListener();
        listenerName = editBusinessName.getKeyListener();
        listenerPostcode = editBusinessPostcode.getKeyListener();
        listenerState = editBusinessState.getKeyListener();
        listenerStreet = editBusinessStreet.getKeyListener();
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.hasSocketError(response)) {
            Toast.makeText(getContext(), "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }

        if (urlTail.equals("getBusinessProfile/" + userId)) {
            try {

                if (response.get("business_profile").equals("no_profile")) {
                    //no profile exists
                    Toast.makeText(getContext(), "This profile does not exist.", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject responseProfile = response.getJSONObject("business_profile");
                    editBusinessName.setText(responseProfile.getString("business_name"));
                    editBusinessDescr.setText(responseProfile.getString("business_descr"));
                    JSONObject address = responseProfile.getJSONObject("business_address");
                    editBusinessStreet.setText(address.optString("street"));
                    editBusinessHnumber.setText(address.optString("housenumber"));
                    editBusinessPostcode.setText(address.optString("postcode"));
                    editBusinessCity.setText(address.optString("city"));
                    editBusinessCountry.setText(address.optString("country"));
                    editBusinessState.setText(address.optString("state"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
