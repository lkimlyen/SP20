package com.demo.sp19.screen.posm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.POSMModel;
import com.demo.architect.data.model.offline.POSMReportModel;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class POSMPresenter implements POSMContract.Presenter {

    private final String TAG = POSMPresenter.class.getName();
    private final POSMContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    POSMPresenter(@NonNull POSMContract.View view) {
        this.view = view;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListPOSM() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        //lấy ds report posm theo brand và posm
        localRepository.getListPOSM(userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>>() {
            @Override
            public void call(LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> brandModelLinkedHashMapLinkedHashMap) {
                view.showListPOSM(brandModelLinkedHashMapLinkedHashMap);
            }
        });
    }

    @Override
    public void saveListPOSMReport(List<POSMReportModel> list) {
        localRepository.saveListPOSMReport(list).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }
}
