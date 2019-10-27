package com.demo.sp19.screen.take_off_volumn;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.OutletEntiy;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
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

public class TakeOffVolumnPresenter implements TakeOffVolumnContract.Presenter {

    private final String TAG = TakeOffVolumnPresenter.class.getName();
    private final TakeOffVolumnContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    TakeOffVolumnPresenter(@NonNull TakeOffVolumnContract.View view) {
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
    public void getListTakeOffVolumn() {
        UserEntity user = UserManager.getInstance().getUser();
        localRepository.getListTakeOffVolumnByDate(user.getTeamOutletId(), user.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<ProductModel, TakeOffVolumnModel>>() {
            @Override
            public void call(LinkedHashMap<ProductModel, TakeOffVolumnModel> productModelStockModelLinkedHashMap) {
                view.showListTakeOffVolumn(productModelStockModelLinkedHashMap);
            }
        });
    }

    @Override
    public void saveListTakeOffVolumn(List<TakeOffVolumnModel> list) {
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        localRepository.saveListTakeOffVolumn(list).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                getListTakeOffVolumn();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }


}
