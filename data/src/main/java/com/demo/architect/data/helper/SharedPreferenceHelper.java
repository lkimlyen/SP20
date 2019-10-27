package com.demo.architect.data.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.demo.architect.data.model.OutletDownloadEntity;
import com.demo.architect.data.model.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by uyminhduc on 4/5/17.
 */

public class SharedPreferenceHelper {
    private static final String PREFERENCE_MAIN = "com.demo.uyminhduc.MAIN";
    private static final String MY_PREFERENCE = "com.demo.uyminhduc.MAIN.MY_PREFERENCE";
    private static final String MEGA_GIFT = "MEGA_GIFT";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String REMOVE_MEGA = "REMOVE_MEGA";
    private static final String USER = "USER";
    private static final String DOWNLOAD = "DOWNLOAD";
    private static final String PROJECT_ID = "PROJECT_ID";
    private static final String OUTLET_BRAND = "OUTLET_BRAND";
    private static final String PRIZE = "PRIZE";
    private static final String CHANGE_GIFT = "CHANGE_GIFT";
    private static final String VERSION = "VERSION";
    private static final String WAS_STARTED = "WAS_STARTED";
    private static final String CUS_INFO = "CUS_INFO";
    private SharedPreferences sharedPreferences;

    private static SharedPreferenceHelper _instance;

    public static SharedPreferenceHelper getInstance(Context context) {
        if (_instance == null) {
            _instance = new SharedPreferenceHelper(context);
        }
        return _instance;
    }

    public SharedPreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    public void pushString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public void pushBoolean(String key, boolean bool) {
        sharedPreferences.edit().putBoolean(key, bool).apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public void pushWasStartedBoolean(boolean bool) {
        sharedPreferences.edit().putBoolean(WAS_STARTED, bool).apply();
    }

    public boolean wasStartedBoolean(boolean def) {
        return sharedPreferences.getBoolean(WAS_STARTED, def);
    }

    public boolean existKey(String key) {
        return sharedPreferences.contains(key);
    }

    public void pushUserObject(UserEntity object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(USER, json);
        prefsEditor.commit();
    }


    public UserEntity getUserObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER, "");
        UserEntity obj = null;
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson(json, UserEntity.class);
        }
        return obj;
    }

    public void pushMegaObject(String json) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(MEGA_GIFT, json);
        prefsEditor.remove(PRIZE);
        prefsEditor.commit();
    }


    public String getMegaGiftObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MEGA_GIFT, "");
        return json;
    }
    public void removeMegaGiftObject() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(MEGA_GIFT);
        prefsEditor.remove(PRIZE);
        prefsEditor.commit();

    }

    public void pushListPrizeObject(List<Integer> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(PRIZE, json);
        prefsEditor.commit();
    }


    public List<Integer> getListPrizeObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PRIZE, "");
        List<Integer> obj = null;
        if (!TextUtils.isEmpty(json)) {
            Type listType = new TypeToken<List<Integer>>() {
            }.getType();
            obj = gson.fromJson(json, listType);
        }
        return obj;
    }

    public void pushInfoCusChangeGift(LinkedHashMap<String, LinkedHashMap<String, String>> listInfo) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (listInfo != null) {
            Gson gson = new Gson();
            json = gson.toJson(listInfo);
        }
        prefsEditor.putString(CUS_INFO, json);
        prefsEditor.commit();
    }

    public void removeInfoCusChangeGift() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(CUS_INFO);
        prefsEditor.commit();
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>>  getInfoCusChangeGift() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CUS_INFO, "");
        LinkedHashMap<String, LinkedHashMap<String, String>>  obj = null;
        if (!TextUtils.isEmpty(json)) {
            Type listType = new TypeToken<LinkedHashMap<String, LinkedHashMap<String, String>>>() {
            }.getType();
            obj = gson.fromJson(json, listType);
        }
        return obj;
    }


    public void pushOutletDownloadObject(List<OutletDownloadEntity> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(DOWNLOAD, json);
        prefsEditor.commit();
    }


    public List<OutletDownloadEntity> getOutletDownloadObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DOWNLOAD, "");
        List<OutletDownloadEntity> obj = null;
        if (!TextUtils.isEmpty(json)) {
            Type listType = new TypeToken<List<OutletDownloadEntity>>() {
            }.getType();
            obj = gson.fromJson(json, listType);
        }
        return obj;
    }

    public void pushProjectDown(int projectId) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(PROJECT_ID, projectId);
        prefsEditor.commit();
    }

    public int getProjectDown() {
        int projectId = sharedPreferences.getInt(PROJECT_ID, 0);
        return projectId;
    }

    public void pushVersionAppProject(String version) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(VERSION, version);
        prefsEditor.commit();
    }

    public String getVersionAppProject() {
        String version = sharedPreferences.getString(VERSION, "");
        return version;
    }


    public void pushChangeSetGift(String json) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(CHANGE_GIFT, json);

        prefsEditor.commit();
    }

    public String getChangeSetGift() {
        String json = sharedPreferences.getString(CHANGE_GIFT, "");
        return json;
    }

    public void pushOutletBrandObject(LinkedHashMap<Integer, List<Integer>> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(OUTLET_BRAND, json);
        prefsEditor.commit();
    }


    public LinkedHashMap<Integer, List<Integer>> getOutletObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(OUTLET_BRAND, "");
        LinkedHashMap<Integer, List<Integer>> obj = null;
        if (!TextUtils.isEmpty(json)) {
            Type listType = new TypeToken<LinkedHashMap<Integer, List<Integer>>>() {
            }.getType();
            obj = gson.fromJson(json, listType);
        }
        return obj;
    }

    public boolean getRemoveMegaPromotion() {
        boolean removeMega = sharedPreferences.getBoolean(REMOVE_MEGA, false);
        return removeMega;
    }

    public void addRemoveMegaPromotion(){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(REMOVE_MEGA, true);

        prefsEditor.commit();
    }
    public boolean getNotificationNew() {
        boolean isNew = sharedPreferences.getBoolean(NOTIFICATION, false);
        return isNew;
    }

    public void addNotificationNew(boolean isNew){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(NOTIFICATION, isNew);
        prefsEditor.commit();
    }
}
