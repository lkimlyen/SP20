package com.demo.sp19.screen.statistical_gift;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class StatisticalGiftPresenter implements StatisticalGiftContract.Presenter {

    private final String TAG = StatisticalGiftPresenter.class.getName();
    private final StatisticalGiftContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    StatisticalGiftPresenter(@NonNull StatisticalGiftContract.View view) {
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
    public void getListCustomerGiftByDate(String date) {
        localRepository.getListCustomerGiftByDate(date).subscribe(new Action1<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> stringListLinkedHashMap) {
                view.showListCustomerGift(stringListLinkedHashMap);
            }
        });
    }
}
