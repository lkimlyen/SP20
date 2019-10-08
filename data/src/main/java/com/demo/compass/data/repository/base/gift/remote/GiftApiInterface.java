package com.demo.compass.data.repository.base.gift.remote;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.ConfirmSetEntity;
import com.demo.compass.data.model.CurrentBrandSetEntity;
import com.demo.compass.data.model.GiftMegaEntity;
import com.demo.compass.data.model.ProductGiftEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface GiftApiInterface {

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerWS")
    Call<BaseResponse<Integer>> addCustomer(@Field("pAppCode") String appCode,
                                            @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerProduct")
    Call<BaseResponse> addCustomerProduct(@Field("pAppCode") String appCode,
                                          @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerImageWS")
    Call<BaseResponse> addCustomerImage(@Field("pAppCode") String appCode,
                                        @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerGiftWS")
    Call<BaseResponse> addCustomerGift(@Field("pAppCode") String appCode,
                                       @Field("pData") String data);

    @GET("https://sp.imark.com.vn/WS/api/GetProductGift?pAppCode=IDS")
    Call<BaseListResponse<ProductGiftEntity>> getProductGift(@Query("pProjectID") int projectId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/GetDataLuckyMega")
    Call<BaseListResponse<GiftMegaEntity>> getDataLuckyMega(@Field("pAppCode") String appCode);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/GetCurrentSet")
    Call<BaseListResponse<CurrentBrandSetEntity>> getCurrentSet(@Field("pAppCode") String appCode,
                                                                @Field("pOutletID") int outletId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddWarehouseRequirementSetWS")
    Call<BaseResponse<Integer>> addWarehouseRequirementSet(@Field("pAppCode") String appCode,
                                                           @Field("pData") String data);


    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/UpdateCurrentGiftWS")
    Call<BaseResponse> updateCurrentGift(@Field("pAppCode") String appCode,
                                         @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/SupConfirmWarehouseRequirementSetWS")
    Call<BaseResponse> confirmWarehouseRequirementSet(@Field("pAppCode") String appCode,
                                                      @Field("pWarehouseRequirementID") int id,
                                                      @Field("pUserID") int userId);


    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/UpdateChangeSetWS")
    Call<BaseResponse> updateChangeSet(@Field("pAppCode") String appCode,
                                       @Field("pOutletID") int outletId,
                                       @Field("pRequimentChangeSetID") int requimentChangeSetID
    );

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddBrandSetUsed")
    Call<BaseResponse> addBrandSetUsed(@Field("pAppCode") String appCode,
                                       @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/GetWarehouseRequirementWS")
    Call<BaseListResponse<ConfirmSetEntity>> getWarehouseRequirement(@Field("pAppCode") String appCode,
                                                                     @Field("pOutletID") int outletId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerTotalDialMega")
    Call<BaseResponse> addCustomerTotalDialMega(@Field("pAppCode") String appCode,
                                                @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddCustomerGiftMega")
    Call<BaseResponse> addCustomerGiftMega(@Field("pAppCode") String appCode,
                                           @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/ReadedNotificationSetGiftMega")
    Call<BaseResponse> readedNotificationSetGiftMega(@Field("pAppCode") String appCode,
                                                     @Field("pOutletID") int outletId,
                                                     @Field("pGiftID") int giftId,
                                                     @Field("pTeamOutletID") int teamOutletId);

}
