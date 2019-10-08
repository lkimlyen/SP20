package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.POSMEntity;
import com.demo.compass.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;



public class GetPOSMUsecase extends BaseUseCase<BaseListResponse<POSMEntity>> {
    private static final String TAG = GetPOSMUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetPOSMUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<POSMEntity>> buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.getPOSM(outletId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<POSMEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<POSMEntity>>() {
            @Override
            public void onNext(BaseListResponse<POSMEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<POSMEntity> result = data.getData();
                    if (data.getStatus() == 1) {
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
        private final int outletId;

        public RequestValue(int outletId) {
            this.outletId = outletId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<POSMEntity> list;

        public ResponseValue(List<POSMEntity> list) {
            this.list = list;
        }

        public List<POSMEntity> getList() {
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
