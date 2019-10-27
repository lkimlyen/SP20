package com.demo.sp19.manager;

import android.content.Intent;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.OutletDownloadEntity;
import com.demo.sp19.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

public class OutletDownloadManager {
    private List<OutletDownloadEntity> outletDownloadEntityList = new ArrayList<>();
    private static OutletDownloadManager instance;

    public static OutletDownloadManager getInstance() {
        if (instance == null) {
            instance = new OutletDownloadManager();
        }
        return instance;
    }

    public void addOutletDownloadEntity(int outletId, int projectId) {
        if (outletDownloadEntityList.size() == 0 && SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletDownloadObject() != null) {
            outletDownloadEntityList.addAll(SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletDownloadObject());
        }
        OutletDownloadEntity outletDownloadEntity = new OutletDownloadEntity(outletId,projectId);
        outletDownloadEntityList.add(outletDownloadEntity);
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushOutletDownloadObject(outletDownloadEntityList);
    }


    public boolean checkDownload(int outletId, int project) {
        if (outletDownloadEntityList.size() == 0 && SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletDownloadObject() != null) {
            outletDownloadEntityList.addAll(SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getOutletDownloadObject());
        }
        for (OutletDownloadEntity outletDownloadEntity : outletDownloadEntityList) {
            if (outletDownloadEntity.getOutletId() == outletId && outletDownloadEntity.getProjectId() == project) {
                return false;
            }

        }
        return true;
    }

}
