package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;

public class SaveSharedPreference {
    private static final String PREF_USER_EMAIL = "email";
    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_USER_TYPE = "user_type_id";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void saveUserInforamtion(Context ctx, String userEmail, int user_id, int user_type_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.putInt(PREF_USER_ID, user_id);
        editor.putInt(PREF_USER_TYPE, user_type_id);
        editor.apply();
    }

    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static int getUserID(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_USER_ID, -999);
    }

    public static int getUserType(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_USER_TYPE, -999);
    }


    public static void clearSharedPrefrences(Context ctx) {
        SharedPreferences preferences = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        //delete realm data
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

}
