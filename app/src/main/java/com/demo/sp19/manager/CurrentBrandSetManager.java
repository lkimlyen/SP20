package com.demo.sp19.manager;

import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.utils.view.ConvertUtils;

import java.util.List;

public class CurrentBrandSetManager {
    private CurrentBrandModel currentBrandModel;
    private static CurrentBrandSetManager instance;

    public static CurrentBrandSetManager getInstance() {
        if (instance == null) {
            instance = new CurrentBrandSetManager();
        }
        return instance;
    }

    public CurrentBrandModel getCurrentBrandModel() {
        return currentBrandModel;
    }

    public void setCurrentBrandModel(CurrentBrandModel currentBrandModel) {
        this.currentBrandModel = currentBrandModel;
    }

    public void updateNumberUsed() {
        currentBrandModel.setCreatedBy(UserManager.getInstance().getUser().getTeamOutletId());
        currentBrandModel.setDateTimeCreate(ConvertUtils.getDateTimeCurrent());
        currentBrandModel.setNumberUsed(currentBrandModel.getNumberUsed() + 1);
    }
}
