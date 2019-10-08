package com.demo.compass.data.repository.base.upload.remote;


import com.demo.compass.data.model.BaseResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface UploadApiInterface {

    @Multipart
    @POST("https://sp.imark.com.vn/WS/UploadImage/UploadFile")
    Call<BaseResponse<Integer>> uploadImage(
            @Part MultipartBody.Part file,
            @Part("pAppCode") RequestBody appCode,
            @Part("pImageCode") RequestBody sessionCode,
            @Part("pTeamOutletID") RequestBody userTeamID,
            @Part("pDeviceDateTime") RequestBody dateTimeDevice,
            @Part("pLatGPS") RequestBody latitude,
            @Part("pLongGPS") RequestBody longitude,
            @Part("pImageType") RequestBody imageType,
            @Part("pFileName") RequestBody fileName);

}
