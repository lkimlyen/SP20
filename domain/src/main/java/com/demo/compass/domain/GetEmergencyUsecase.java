package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.ReasonEntity;
import com.demo.compass.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetEmergencyUsecase extends BaseUseCase<BaseListResponse<ReasonEntity>>  {
    private static final String TAG = GetEmergencyUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetEmergencyUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ReasonEntity>>  buildUseCaseObservable() {

        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.getEmergency(projectId);
    }



    @Override
    protected DisposableObserver<BaseListResponse<ReasonEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ReasonEntity>>() {
            @Override
            public void onNext(BaseListResponse<ReasonEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<ReasonEntity> result = data.getData();
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

        public RequestValue(int projectId) {
            this.projectId = projectId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ReasonEntity> list;

        public ResponseValue(List<ReasonEntity> list) {
            this.list = list;
        }

        public List<ReasonEntity> getList() {
            return list;
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
