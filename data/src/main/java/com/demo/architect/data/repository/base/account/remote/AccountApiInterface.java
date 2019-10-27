package com.demo.architect.data.repository.base.account.remote;


import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UserEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface AccountApiInterface {

    @FormUrlEncoded
    @POST("https://sp20.imark.com.vn/ws/api/loginWS")
    Call<BaseResponse<UserEntity>> login(@Field("pAppCode") String appCode,
                                         @Field("pUserType") String userType,
                                         @Field("pUserName") String userName,
                                         @Field("pPassWord") String password,
                                         @Field("pDeviceToken") String deviceToken,
                                         @Field("pDeviceID") String deviceId);

    @FormUrlEncoded
    @POST("https://sp20.imark.com.vn/WS/api/ChangePassWS")
    Call<BaseResponse> changePass(@Field("pAppCode") String appCode,
                                  @Field("pUserID") int userId,
                                  @Field("pOldPassword") String passOld,
                                  @Field("pNewPassword") String passwNew);

    @FormUrlEncoded
    @POST("https://sp20.imark.com.vn/ws/api/AddAttendanceTrackingWS")
    Call<BaseResponse<Integer>> attendanceTracking(@Field("pAppCode") String appCode,
                                                   @Field("pAttendanceTrackingCode") String sessionCode,
                                                   @Field("pTeamOutletID") int teamId,
                                                   @Field("pTimePointType") String type,
                                                   @Field("pLatGPS") double latitude,
                                                   @Field("pLongGPS") double longitude,
                                                   @Field("pNumberPG") int number,
                                                   @Field("pDeviceDateTime") String dateTime);

    @FormUrlEncoded
    @POST("https://sp20.imark.com.vn/WS/api/AddAttendanceTrackingImageWS")
    Call<BaseResponse<Integer>> addImageForAttenadence(@Field("pAppCode") String appCode,
                                                       @Field("pAttendanceTrackingID") int attendanceId,
                                                       @Field("pImageID") int imageId);

    @FormUrlEncoded
    @POST("https://sp20.imark.com.vn/WS/api/LogOutWS")
    Call<BaseResponse> logout(@Field("pAppCode") String appCode,
                              @Field("pOutletID") int outletId);
}
