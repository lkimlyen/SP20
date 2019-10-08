package com.demo.compass.data.repository.base.other.remote;


import com.demo.compass.data.model.BackgroundDialEntity;
import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.POSMEntity;
import com.demo.compass.data.model.ReasonEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface OtherApiInterface {

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/UpdateStockWS")
    Call<BaseResponse> updateStock(@Field("pAppCode") String appCode,
                                   @Field("pData") String data);
    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/UpdateTypeStock")
    Call<BaseResponse> updateTypeStock(@Field("pAppCode") String appCode,
                                       @Field("pData") String data);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddTakeOffVolumn")
    Call<BaseResponse> addTakeOffVolume(@Field("pAppCode") String appCode,
                                        @Field("pData") String data);

    @GET("https://sp.imark.com.vn/WS/api/GetBackgroundWS")
    Call<BaseListResponse<BackgroundDialEntity>> getBackground(@Query("pAppCode") String appCode,
                                                               @Query("pProjectID") int projectId);

    @GET("https://sp.imark.com.vn/WS/api/GetEmergencyWS?pAppCode=IDS")
    Call<BaseListResponse<ReasonEntity>> getEmergency(@Query("pProjectID") int projectId);

    @GET("https://sp.imark.com.vn/WS/api/GetUpdateVersion?pAppCode=IDS")
    Call<BaseResponse<String>> getUpdateVersion(@Query("pProjectID") int projectId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddProfileEmergency")
    Call<BaseResponse<Integer>> addProfileEmergency(@Field("pAppCode") String appCode,
                                                    @Field("pProflieEmergencyCode") String code,
                                                    @Field("pTeamOutletID") int teamOutletId,
                                                    @Field("pProfileCode") String employeeId,
                                                    @Field("pEmergencyID") int reasonId,
                                                    @Field("pOutletID") int outletId,
                                                    @Field("pStartDateTime") String startDateTime,
                                                    @Field("pProjectID") int projectId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/CompleteProfileEmergency")
    Call<BaseResponse> completeProfileEmergency(@Field("pAppCode") String appCode,
                                                @Field("pProfileEmergencyID") int emergencyId,
                                                @Field("pTeamOutletID") int teamOutletId);

    @GET("https://sp.imark.com.vn/WS/api/GetPOSMWS?pAppCode=IDS")
    Call<BaseListResponse<POSMEntity>> getPOSM(@Query("pOutletID") int outletId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/AddPOSMWS")
    Call<BaseResponse> addPOSM(@Field("pAppCode") String appCode,
                               @Field("pData") String data);
    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/GetVersion")
    Call<BaseResponse> getVersion(@Field("pAppCode") String appCode,
                                  @Field("pOutletID") int outletId,
                                  @Field("pVersion") String version);
}
