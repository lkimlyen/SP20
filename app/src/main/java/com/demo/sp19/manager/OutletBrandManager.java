package com.demo.sp19.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.sp19.app.CoreApplication;

import java.util.LinkedHashMap;
import java.util.List;

public class OutletBrandManager {
    private LinkedHashMap<Integer, List<Integer>> integerList;
    private static OutletBrandManager instance;

    public static OutletBrandManager getInstance() {
        if (instance == null) {
            instance = new OutletBrandManager();
        }
        return instance;
    }

    public void setOutletBrandList(LinkedHashMap<Integer, List<Integer>> integerList) {
            this.integerList = integerList;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushOutletBrandObject(integerList);
    }

    public List<Integer> getOutletBrandList(int outletId) {
        if (integerList == null) {
            integerList = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletObject();
        }
        return integerList.get(outletId);
    }
    public LinkedHashMap<Integer, List<Integer>> getAllOutletBrandList() {
        if (integerList == null) {
            integerList = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletObject();
        }
        return integerList;
    }

}
