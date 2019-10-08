package com.demo.compass.data.repository.base.other.remote;


import com.demo.compass.data.model.BackgroundDialEntity;
import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.POSMEntity;
import com.demo.compass.data.model.ReasonEntity;


import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface OtherRepository {
    Observable<BaseResponse> updateStock(String appCode, String data);
    Observable<BaseResponse> updateTypeStock(String appCode, String data);
    Observable<BaseResponse> addTakeOffVolume(String appCode, String data);
    Observable<BaseListResponse<BackgroundDialEntity>> getBackground(String appCode, int projectId);
    Observable<BaseListResponse<ReasonEntity>> getEmergency(int projectId);
    Observable<BaseResponse<String>> getUpdateVersion(int projectId);
    Observable<BaseResponse<Integer>> addProfileEmergency(String appCode, String code, int teamOutletId,
                                                          String employeeId, int reasonId, int outletId, String startDateTime, int projectId);
    Observable<BaseResponse> completeProfileEmergency(String appCode, int emergencyId, int teamOutletId);

    Observable<BaseListResponse<POSMEntity>> getPOSM(int outletId);
    Observable<BaseResponse> addPOSM(String appCode, String data);
    Observable<BaseResponse> getVersion(String appCode, int outletId, String version);

}
