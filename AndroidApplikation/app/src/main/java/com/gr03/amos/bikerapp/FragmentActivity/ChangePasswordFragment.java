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
import com.gr03.amos.bikerapp.ShowEventActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/
    public void changePassword() throws JSONException {
        String np = editTextNewPassword.getText().toString();
        String rnp = editTextRepeatPassword.getText().toString();
        JSONObject json = new JSONObject();
        json.put("email", email.getText().toString());
        json.put("oldPassword", editTextOldPassword.getText().toString());
        json.put("newPassword", editTextNewPassword.getText().toString());
        json.put("repeatNewPassword", editTextRepeatPassword.getText().toString());
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
                        Toast.makeText(getContext(), "Wrong Email or Old Password", Toast.LENGTH_LONG).show();
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
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
