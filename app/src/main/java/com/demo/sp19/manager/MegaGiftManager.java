package com.demo.sp19.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.PrizeEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.app.CoreApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MegaGiftManager {
    private PrizeEntity prizeEntity;
    private List<Integer> list;
    private static MegaGiftManager instance;

    public static MegaGiftManager getInstance() {
        if (instance == null) {
            instance = new MegaGiftManager();
        }
        return instance;
    }

    public PrizeEntity getMegaGiftObject() {
        prizeEntity = new Gson().fromJson(SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getMegaGiftObject(), PrizeEntity.class);
        if (prizeEntity != null) {
            if (prizeEntity.getOutletId() == UserManager.getInstance().getUser().getOutlet().getOutletId() && prizeEntity.getDateTrue().equals(ConvertUtils.getDateTimeCurrentShort())) {
                return prizeEntity;
            }
        }
        return null;
    }

    public List<Integer> getListPrize(int outletId) {
        prizeEntity = new Gson().fromJson(SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getMegaGiftObject(), PrizeEntity.class);
        if (prizeEntity != null) {
            if (prizeEntity.getOutletId() == outletId && prizeEntity.getDateTrue().equals(ConvertUtils.getDateTimeCurrentShort())) {
                list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPrizeObject();
            }
        }
        return list;
    }

    public void updateListPrize(int ouletId, List<Integer> list) {
        prizeEntity = new Gson().fromJson(SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getMegaGiftObject(), PrizeEntity.class);
        if (prizeEntity != null) {
            if (prizeEntity.getOutletId() == ouletId) {
               this.list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPrizeObject();
               if (this.list.size() > list.size()){
                   SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPrizeObject(list);
               }
            }
        }

    }

    public void removeListPrize() {
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).removeMegaGiftObject();
    }

}
