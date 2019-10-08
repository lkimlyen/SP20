package com.demo.compass.data.repository.base.notification.remote;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.NotificationEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class NotificationRepositoryImpl implements NotificationRepository {

    private final static String TAG = NotificationRepositoryImpl.class.getName();

    private NotificationApiInterface mRemoteApiInterface;

    public NotificationRepositoryImpl(NotificationApiInterface mNotificationApiInterface) {
        this.mRemoteApiInterface = mNotificationApiInterface;
    }

    private void handleNotificationResponse(Call<BaseListResponse<NotificationEntity>> call, ObservableEmitter<BaseListResponse<NotificationEntity>> emitter) {
        try {
            BaseListResponse<NotificationEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }




    @Override
    public Observable<BaseListResponse<NotificationEntity>> getNotification(final int projectId, final int outletType,final  int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<NotificationEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<NotificationEntity>> emitter) throws Exception {
                handleNotificationResponse(mRemoteApiInterface.getNotification(projectId, outletType,outletId), emitter);

            }
        });
    }
}


