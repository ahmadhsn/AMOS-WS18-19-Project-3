package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.widget.Toast;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.ShowEventActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment {

    private EditText email, editTextOldPassword, editTextNewPassword, editTextRepeatPassword, country;
    private Button changePassword;
    public ChangePasswordFragment() { }

    public static ChangePasswordFragment newInstance() { return new ChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }


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

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }

    boolean checkEnteredData() {
        boolean isDataNotSet = false;
        if (isTextEmpty(editTextOldPassword)) {
            editTextOldPassword.setError("Please Enter your old password");
            isDataNotSet = true;
        }
        if (isTextEmpty(editTextNewPassword)) {
            editTextNewPassword.setError("Please Enter new password");
            isDataNotSet = true;
        }
        return isDataNotSet;
    }


    public void changePassword() throws JSONException {
        String np = editTextNewPassword.getText().toString();
        String rnp = editTextRepeatPassword.getText().toString();

        if (checkEnteredData()) {
            return;
        }


        if (!np.equals(rnp) || np.isEmpty() || rnp.isEmpty())   {
            Log.i("COMPAREPASSWORDS", "passwords are unequal");
            // this.setMessageOnScreen( "The passwords you entered do not match. Please try it again.",Color.RED);
            Toast.makeText(getActivity().getApplicationContext(), "The Repeat password you entered doesn't match with the New Password.", Toast.LENGTH_SHORT).show();
            editTextRepeatPassword.setText("");
            editTextRepeatPassword.setError("");
            return;
        }

        JSONObject json = new JSONObject();
        json.put("oldPassword", editTextOldPassword.getText().toString());
        json.put("newPassword", editTextNewPassword.getText().toString());
        json.put("repeatNewPassword", editTextRepeatPassword.getText().toString());
        json.put("user_id", SaveSharedPreference.getUserID(this.getContext()));
        if (np.equals(rnp)) {
            try {

                JSONObject response;

                FutureTask<String> task = new FutureTask(new Callable<String>() {
                    public String call() {
                        JSONObject threadResponse = Requests.getResponse("changePassword", json);
                        return threadResponse.toString();
                    }
                });
                new Thread(task).start();
                Log.i("Response", task.get());
                response = new JSONObject(task.get());

                //handle response
                if (response.has("passwordUpdated")) {
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
        } else {
            Toast.makeText(getContext(), "New Password does not match", Toast.LENGTH_LONG).show();
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
