package com.demo.sp19.manager;

import android.text.TextUtils;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ChooseSetEntitiy;
import com.demo.architect.data.model.OutletDownloadEntity;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.app.CoreApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ChooseSetManager {
    private List<ChooseSetEntitiy> chooseSetEntitiyList;
    private static ChooseSetManager instance;

    public static ChooseSetManager getInstance() {
        if (instance == null) {
            instance = new ChooseSetManager();
        }
        return instance;
    }

    public List<ChooseSetEntitiy> getListChooseSet() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ChooseSetEntitiy>>() {
        }.getType();
        chooseSetEntitiyList = null;
        String json = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getChangeSetGift();
        if (!TextUtils.isEmpty(json)) {
            chooseSetEntitiyList = gson.fromJson(json, listType);
        }
        return chooseSetEntitiyList;

    }

    public ChooseSetEntitiy getChooseSetByBrandSetId(int brandId, int brandSetId) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ChooseSetEntitiy>>() {
        }.getType();
        ChooseSetEntitiy chooseSetEntitiy = null;
        String json = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getChangeSetGift();
        if (!TextUtils.isEmpty(json)) {
            chooseSetEntitiyList = gson.fromJson(json, listType);
            for (ChooseSetEntitiy chooseSetEntitiy1 : chooseSetEntitiyList) {
                if (chooseSetEntitiy1.getBrandId() == brandId && chooseSetEntitiy1.getBrandSetId() == brandSetId) {
                    chooseSetEntitiy = chooseSetEntitiy1;
                    break;
                }
            }
        }
        return chooseSetEntitiy;

    }

    public void updateSetGift(ChooseSetEntitiy chooseSetEntitiy) {
        Gson gson = new Gson();
        chooseSetEntitiyList.remove(chooseSetEntitiy);
        String json = gson.toJson(chooseSetEntitiyList);
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushChangeSetGift(json);

    }
}
