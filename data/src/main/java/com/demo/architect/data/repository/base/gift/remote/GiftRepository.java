package com.demo.architect.data.repository.base.gift.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.CurrentBrandSetEntity;
import com.demo.architect.data.model.GiftMegaEntity;
import com.demo.architect.data.model.ProductGiftEntity;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface GiftRepository {
    Observable<BaseResponse<Integer>> addCustomer(String appCode, String data);

    Observable<BaseResponse> addCustomerProduct(String appCode, String data);

    Observable<BaseResponse> addCustomerImage(String appCode, String data);

    Observable<BaseResponse> addCustomerGift(String appCode, String data);

    Observable<BaseListResponse<ProductGiftEntity>> getProductGift(int projetcId);

    Observable<BaseListResponse<CurrentBrandSetEntity>> getCurrentSet(String appCode, int outletId);

    Observable<BaseResponse<Integer>> addWarehouseRequirementSet(String appCode, String data);

    Observable<BaseResponse> updateCurrentGift(String appCode, String data);

    Observable<BaseResponse> confirmWarehouseRequirementSet(Map<String, Object> params);

    Observable<BaseResponse> updateChangeSet(String appCode, int outletId,
                                             int requimentChangeSetID);

    Observable<BaseResponse> addBrandSetUsed(String appCode, String data);


    Observable<BaseListResponse<ConfirmSetEntity>> getWarehouseRequirement(String tokenBearer,long token, int outletId);
    Observable<BaseListResponse<GiftMegaEntity>> getDataLuckyMega(String appCode);

    Observable<BaseResponse> addCustomerTotalDialMega(String appCode, String data);

    Observable<BaseResponse> addCustomerGiftMega(String appCode, String data);
    Observable<BaseResponse> readedNotificationSetGiftMega(String appCode, int outletId, int giftId, int teamOutletID);
    Observable<BaseResponse<ConfirmSetEntity>> sendRequestGift(final Map<String, Object> bradnsetList);
}
