package com.demo.sp19.manager;

import android.util.Log;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.CurrentGift;
import com.demo.sp19.app.CoreApplication;

import java.util.List;

public class CurrentGiftManager {
    private List<CurrentGift> currentGiftList;
    private static CurrentGiftManager instance;
    public boolean isChangeSet = false;

    public static CurrentGiftManager getInstance() {
        if (instance == null) {
            instance = new CurrentGiftManager();
        }
        return instance;
    }

    public void setCurrentGift(List<CurrentGift> user, boolean isChangeSet) {
        this.isChangeSet = isChangeSet;
        currentGiftList = user;
    }

    public List<CurrentGift> getCurrentGiftList() {
        return currentGiftList;
    }

    public boolean checkCoditionGetGift(int giftId) {
        for (CurrentGift currentGift : currentGiftList) {
            if (currentGift.getGiftID() == giftId) {
                if (currentGift.getNumberUsed() == currentGift.getNumberTotal()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void updateNumberGift(int giftId) {
        for (CurrentGift currentGift : currentGiftList) {
            if (currentGift.getGiftID() == giftId) {
                currentGift.setNumberUsed(currentGift.getNumberUsed() + 1);
            }
        }
        resetData();
    }

    private void resetData() {
        int totalGift = 0;
        int totalGiftUsed = 0;
        for (CurrentGift currentGift : currentGiftList) {
            totalGift += currentGift.getNumberTotal();
            totalGiftUsed += currentGift.getNumberUsed();
        }

        if (totalGift == totalGiftUsed) {
            for (CurrentGift currentGift : currentGiftList) {
                currentGift.setNumberUsed(0);
                isChangeSet = true;
            }

            CurrentBrandSetManager.getInstance().updateNumberUsed();
        }
    }
}
