package com.demo.architect.data.repository.base.notification.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.NotificationEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface NotificationApiInterface {

    @GET("https://sp20.imark.com.vn/ws/api/GetNotificationWS?pAppCode=IDS")
    Call<BaseListResponse<NotificationEntity>> getNotification(@Query("pProjectID") int projectId,
                                                               @Query("pOutletTypeID") int outletType,
                                                               @Query("pOutletID") int outletId);
}
