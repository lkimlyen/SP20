package com.demo.sp19.manager;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.sp19.app.CoreApplication;

public class ResetDataManager {
    public int resetData = -1;
    private static ResetDataManager instance;

    public static ResetDataManager getInstance() {
        if (instance == null) {
            instance = new ResetDataManager();
        }
        return instance;
    }

    public int getResetData() {
        if (resetData == -1) {
            resetData = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getInt(Constants.RESET_DATA, resetData);
        }
        return resetData;
    }

    public void setResetData(int resetData) {
        this.resetData = resetData;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushInt(Constants.RESET_DATA, resetData);
    }
}
