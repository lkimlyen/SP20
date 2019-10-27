package com.demo.sp19.screen.manager_gift;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.sp19.manager.OutletBrandManager;
import com.demo.sp19.manager.UserManager;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ManagerGiftPresenter implements ManagerGiftContract.Presenter {

    private final String TAG = ManagerGiftPresenter.class.getName();
    private final ManagerGiftContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    ManagerGiftPresenter(@NonNull ManagerGiftContract.View view) {
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
    public void getListGiftChange() {
        int outletId = UserManager.getInstance().getUser().getOutlet().getOutletId();
        List<Integer> idList = OutletBrandManager.getInstance().getOutletBrandList(outletId);
        localRepository.getListGiftChangeByDate(outletId, idList).subscribe(new Action1<LinkedHashMap<GiftModel, DetailCurrentGiftModel>>() {
            @Override
            public void call(LinkedHashMap<GiftModel, DetailCurrentGiftModel> giftModelDetailCurrentGiftModelLinkedHashMap) {
                view.showListGiftChange(giftModelDetailCurrentGiftModelLinkedHashMap);
            }
        });
    }
}
