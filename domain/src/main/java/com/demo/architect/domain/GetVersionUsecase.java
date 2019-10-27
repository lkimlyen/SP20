package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetVersionUsecase extends BaseUseCase<BaseResponse>  {
    private static final String TAG = GetVersionUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetVersionUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse>  buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        String version = ((RequestValue) requestValues).version;
        return remoteRepository.getVersion(Constants.APP_CODE, outletId, version);
    }

    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
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
        private final String version;

        public RequestValue(int outletId, String version) {

            this.outletId = outletId;
            this.version = version;
        }
    }

    public static final class ResponseValue implements ResponseValues {

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
