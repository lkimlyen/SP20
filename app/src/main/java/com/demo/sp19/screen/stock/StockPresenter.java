package com.demo.sp19.screen.stock;

import android.util.Log;

import androidx.annotation.NonNull;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.OutletEntiy;
import com.demo.architect.data.model.offline.NoteStockModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.StockModel;
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

public class StockPresenter implements StockContract.Presenter {

    private final String TAG = StockPresenter.class.getName();
    private final StockContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    StockPresenter(@NonNull StockContract.View view) {
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
    public void getListStock() {
        localRepository.getListStockByDate(UserManager.getInstance().getUser().getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<ProductModel, StockModel>>() {
            @Override
            public void call(LinkedHashMap<ProductModel, StockModel> productModelStockModelLinkedHashMap) {
                view.showListStock(productModelStockModelLinkedHashMap);
            }
        });
    }

    @Override
    public void saveListStock(List<StockModel> list, int type) {
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        localRepository.saveListStock(list, outletEntiy.getOutletId(), type).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (type != -1) {
                    saveNoteStock(type,true);
                } else {
                    getListStock();
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
                }
            }
        });
    }

    @Override
    public void getNoteStock() {
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        localRepository.getNoteStock(outletEntiy.getOutletId()).subscribe(new Action1<NoteStockModel>() {
            @Override
            public void call(NoteStockModel s) {
                view.showNoteStock(s);
            }
        });
    }

    @Override
    public void saveNoteStock(int type, boolean saveStock) {
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        localRepository.saveNoteStock(outletEntiy.getOutletId(), type).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (saveStock){

                    getListStock();
                }
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }
}
