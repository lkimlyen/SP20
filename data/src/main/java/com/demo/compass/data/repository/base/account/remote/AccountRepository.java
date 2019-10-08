package com.demo.compass.data.repository.base.account.remote;


import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.UserEntity;


import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface AccountRepository {
    Observable<BaseResponse<UserEntity>> login(String appCode, String userType, String username, String password, String deviceToken, String deviceId);
    Observable<BaseResponse> changePass(String appCode, int teamOutletId, String passOld, String passNew);

    Observable<BaseResponse<Integer>> attendanceTracking(String appCode, String sessionCode, int teamId,
                                                         String type, double latitude, double longitude,
                                                         int number, String dateTime);

    Observable<BaseResponse<Integer>> addImageForAttenadence(String appCode, int attendanceId, int imageId);

    Observable<BaseResponse> logout(String appCode, int outletId);
}
