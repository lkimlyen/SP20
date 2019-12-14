package com.demo.sp19.screen.request_gift;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.DetailRequestGiftModel;
import com.demo.architect.data.model.offline.RequestGiftModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddWarehouseRequirementSetUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.SendRequestGiftUsecase;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.TokenManager;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class RequestGiftPresenter implements RequestGiftContract.Presenter {

    private final String TAG = RequestGiftPresenter.class.getName();
    private final RequestGiftContract.View view;
    private final AddWarehouseRequirementSetUsecase addWarehouseRequirementSetUsecase;
    private final SendRequestGiftUsecase sendRequestGiftUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    RequestGiftPresenter(@NonNull RequestGiftContract.View view, AddWarehouseRequirementSetUsecase addWarehouseRequirementSetUsecase, SendRequestGiftUsecase sendRequestGiftUsecase) {
        this.view = view;
        this.addWarehouseRequirementSetUsecase = addWarehouseRequirementSetUsecase;
        this.sendRequestGiftUsecase = sendRequestGiftUsecase;
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
    public void getListBrandSetDetailCurrent() {
        //lấy danh sác set quà hiện tại của outlet
        localRepository.getListBrandSetDetailCurrent( UserManager.getInstance().getUser().getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>>() {
            @Override
            public void call(LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>> brandModelListLinkedHashMap) {
                view.showListBrandSetDetailCurrent(brandModelListLinkedHashMap);
            }
        });
    }

    @Override
    public void saveRequestGift(LinkedHashMap<Integer, Integer> linkedHashMap) {
        UserEntity user = UserManager.getInstance().getUser();
        List<DetailRequestGiftModel> list = new ArrayList<>();
        String code = ConvertUtils.getCodeGenerationByTime();
        String dateRequest = ConvertUtils.getDateTimeCurrent();
        for (Map.Entry<Integer, Integer> map : linkedHashMap.entrySet()) {
            DetailRequestGiftModel detailRequestGiftModel = new DetailRequestGiftModel(user.getOutlet().getOutletId(),
                    code, map.getKey(), map.getValue(), dateRequest, user.getTeamOutletId(), TokenManager.getInstance().getToken());
            list.add(detailRequestGiftModel);
        }
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        view.showProgressBar();
        sendRequestGiftUsecase.executeIO(new SendRequestGiftUsecase.RequestValue(UserManager.getInstance().getUser().getUserId(), linkedHashMap),
                new BaseUseCase.UseCaseCallback<SendRequestGiftUsecase.ResponseValue,
                        SendRequestGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(SendRequestGiftUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        RequestGiftModel requestGiftModel = new RequestGiftModel(successResponse.getDescription().getId(),
                                code,user.getOutlet().getOutletId(),user.getTeamOutletId(),dateRequest, Constants.UNCONFIRMED);
                        localRepository.saveRequestGift(requestGiftModel,list).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_send_request_success));
                            }
                        });
                    }

                    @Override
                    public void onError(SendRequestGiftUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.hideProgressBar();
                    }
                });

    }

}
