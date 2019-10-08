package com.demo.compass.data.repository.base.notification.remote;


import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.NotificationEntity;


import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface NotificationRepository {
    Observable<BaseListResponse<NotificationEntity>> getNotification(int projectId, int outletType, int outletId);
}
