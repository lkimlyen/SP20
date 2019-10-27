package com.demo.sp19.manager;

import android.text.TextUtils;

import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private String token;
    private static TokenManager instance;

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
