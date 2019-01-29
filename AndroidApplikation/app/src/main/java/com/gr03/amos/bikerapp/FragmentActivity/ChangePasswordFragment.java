package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.util.Log;

import android.widget.Toast;

import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.ShowEventActivity;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment implements ResponseHandler {

    private EditText email, editTextOldPassword, editTextNewPassword, editTextRepeatPassword, country;
    private Button changePassword;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        email = view.findViewById(R.id.email);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextRepeatPassword = view.findViewById(R.id.editTextRepeatPassword);
        changePassword = view.findViewById(R.id.changePassword);
        // Inflate the layout for this fragment
        changePassword.setOnClickListener(v -> {
            try {
                changePassword();
            } catch (JSONException ignored) {
            }
        });
        return view;
    }

    public void changePassword() throws JSONException {
        String np = editTextNewPassword.getText().toString();
        String rnp = editTextRepeatPassword.getText().toString();
        JSONObject json = new JSONObject();
        json.put("oldPassword", editTextOldPassword.getText().toString());
        json.put("newPassword", editTextNewPassword.getText().toString());
        json.put("repeatNewPassword", editTextRepeatPassword.getText().toString());
        json.put("user_id", SaveSharedPreference.getUserID(this.getContext()));
        if (np.equals(rnp)) {
            Requests.executeRequest(this, "POST", "changePassword", json);

        } else {
            Toast.makeText(getContext(), "New Password does not match", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.hasSocketError(response)) {
            Toast.makeText(getContext(), "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }

        switch (urlTail) {
            case "changePassword":
                try {
                    //handle response
                    if (response != null && response.has("passwordUpdated")) {
                        String loginResponse = response.getString("passwordUpdated");

                        if (loginResponse.equals("successfullUpdation")) {
                            Toast.makeText(getContext(), "Password Successfully Changed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), ShowEventActivity.class);
                            startActivity(intent);
//                    return;
                        } else {
                            Toast.makeText(getContext(), "Wrong Old Password", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                } catch (Exception e) {

                    Log.i("Exception --- not requested", e.toString());
                }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
