package com.e.firedamper.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by surya on 1/3/18.
 */

public class PreferencesManager {

    //app login variables
    private static final String PREF_NAME = "com.udlife.PREF_MLK";
    private static final String NORMAL_R1_RADIO_TYPE = "normal_radio_r1_type";
    private static final String NORMAL_R2_RADIO_TYPE = "normal_radio_r2_type";
    private static final String FIRE_R1_RADIO_TYPE = "fire_radio_r1_type";
    private static final String FIRE_R2_RADIO_TYPE = "fire_radio_r2_type";


    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //for fragment
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    //for getting instance
    public static synchronized PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public boolean clear() {
        return mPref.edit().clear().commit();
    }

    public void setNormalR1RadioType(String value) {
        mPref.edit().putString(NORMAL_R1_RADIO_TYPE, value).commit();
    }

    public String getNormalR1RadioType() {
        return mPref.getString(NORMAL_R1_RADIO_TYPE, "");
    }

    public void setNormalR2RadioType(String value) {
        mPref.edit().putString(NORMAL_R2_RADIO_TYPE, value).commit();
    }

    public String getNormalR2RadioType() {
        return mPref.getString(NORMAL_R2_RADIO_TYPE, "");
    }


    public void setFireR1RadioType(String value) {
        mPref.edit().putString(FIRE_R1_RADIO_TYPE, value).commit();
    }

    public String getFireR1RadioType() {
        return mPref.getString(FIRE_R1_RADIO_TYPE, "");
    }


    public void setFireR2RadioType(String value) {
        mPref.edit().putString(FIRE_R2_RADIO_TYPE, value).commit();
    }

    public String getFireR2RadioType() {
        return mPref.getString(FIRE_R2_RADIO_TYPE, "");
    }

//    public <T> void setList(String key, List<T> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//
//        set(key, json);
//    }
//
//    public void set(String key, String value) {
//        mPref.edit().putString(key, value);
//        mPref.edit().commit();
//    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        if (mPref != null) {
            SharedPreferences.Editor prefsEditor = mPref.edit();
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        }
    }

    public List<String> getList(String key) {
//        if (setSharedPreferences != null) {

            Gson gson = new Gson();
            List<String> companyList;

            String string = mPref.getString(key, null);
            Type type = new TypeToken<List<String>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
//        }
//        return null;
    }

}
