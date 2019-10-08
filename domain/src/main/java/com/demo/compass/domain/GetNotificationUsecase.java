package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.NotificationEntity;
import com.demo.compass.data.repository.base.notification.remote.NotificationRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetNotificationUsecase extends BaseUseCase<BaseListResponse<NotificationEntity>> {
    private static final String TAG = GetNotificationUsecase.class.getSimpleName();
    private final NotificationRepository remoteRepository;

    public GetNotificationUsecase(NotificationRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<NotificationEntity>> buildUseCaseObservable() {

        int projectId = ((RequestValue) requestValues).projectId;
        int outletType = ((RequestValue) requestValues).outletType;
        int outletId = ((RequestValue) requestValues).outletId;

        return remoteRepository.getNotification(projectId,outletType,outletId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<NotificationEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<NotificationEntity>>() {
            @Override
            public void onNext(BaseListResponse<NotificationEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<NotificationEntity> result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }


            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.getMessage()));
                }
            }
        };
    }
    public static final class RequestValue implements RequestValues {
        private final int projectId;
        private final int outletType;
        private final int outletId;

        public RequestValue(int projectId, int outletType, int outletId) {
            this.projectId = projectId;
            this.outletType = outletType;
            this.outletId = outletId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<NotificationEntity> entity;

        public ResponseValue(List<NotificationEntity> entity) {
            this.entity = entity;
        }

        public List<NotificationEntity> getEntity() {
            return entity;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private final String description;

        public ErrorValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
