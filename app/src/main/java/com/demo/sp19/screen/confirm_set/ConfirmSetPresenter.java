package com.demo.sp19.screen.confirm_set;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ConfirmWarehouseRequirementSetUsecase;
import com.demo.architect.domain.GetWarehouseRequirementUsecase;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmSetPresenter implements ConfirmSetContract.Presenter {

    private final String TAG = ConfirmSetPresenter.class.getName();
    private final ConfirmSetContract.View view;
    private final ConfirmWarehouseRequirementSetUsecase confirmWarehouseRequirementSetUsecase;
    private final GetWarehouseRequirementUsecase getWarehouseRequirementUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmSetPresenter(@NonNull ConfirmSetContract.View view, ConfirmWarehouseRequirementSetUsecase confirmWarehouseRequirementSetUsecase, GetWarehouseRequirementUsecase getWarehouseRequirementUsecase) {
        this.view = view;
        this.confirmWarehouseRequirementSetUsecase = confirmWarehouseRequirementSetUsecase;
        this.getWarehouseRequirementUsecase = getWarehouseRequirementUsecase;
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
    public void getListConfirmReciever() {
//        UserEntity userEntity = UserManager.getInstance().getUser();
//        localRepository.getListConfirmReciever(OutletManager.getInstance().getOutletEntiy().getOutletId()).subscribe(new Action1<RealmResults<RequestGiftModel>>() {
//            @Override
//            public void call(RealmResults<RequestGiftModel> requestGiftModels) {
//                view.showListConfirm(requestGiftModels);
//            }
//        });
    }

    @Override
    public void confirmSet(Map<String, Object> params) {
        view.showProgressBar();
        confirmWarehouseRequirementSetUsecase.executeIO(new ConfirmWarehouseRequirementSetUsecase.RequestValue(params),
                new BaseUseCase.UseCaseCallback<ConfirmWarehouseRequirementSetUsecase.ResponseValue,
                        ConfirmWarehouseRequirementSetUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ConfirmWarehouseRequirementSetUsecase.ResponseValue successResponse) {

//                        localRepository.updateStateRequest(id).subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String s) {
                                getListConfirmRequestGift(true);
//                            }
//                        });

                    }

                    @Override
                    public void onError(ConfirmWarehouseRequirementSetUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    @Override
    public void getListConfirmRequestGift(boolean upload) {
        view.showProgressBar();
        //lấy ds yêu cầu set quà
        getWarehouseRequirementUsecase.executeIO(new GetWarehouseRequirementUsecase.RequestValue(UserManager.getInstance().getUser().getUserId()),
                new BaseUseCase.UseCaseCallback<GetWarehouseRequirementUsecase.ResponseValue,
                        GetWarehouseRequirementUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetWarehouseRequirementUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                       // lấy ds set quà đag chạy tại outlet
                       localRepository.getListBrandSetById().subscribe(new Action1< LinkedHashMap<Integer, LinkedHashMap<BrandModel,BrandSetModel>>>() {
                           @Override
                           public void call( LinkedHashMap<Integer, LinkedHashMap<BrandModel,BrandSetModel>> integerBrandSetModelLinkedHashMap) {
                               view.showListConfirm(successResponse.getList(),integerBrandSetModelLinkedHashMap);
                               if (upload){
                                   view.showSuccess(CoreApplication.getInstance().getString( R.string.text_confirm_success));
                               }else {
                                   if (successResponse.getList().size() == 0){
                                       view.showError(CoreApplication.getInstance().getString(R.string.text_no_data));
                                   }
                               }
                           }
                       });

                    }

                    @Override
                    public void onError(GetWarehouseRequirementUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }
}
