package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class CompleteProfileEmergencyUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = CompleteProfileEmergencyUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public CompleteProfileEmergencyUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        int emergencyId = ((RequestValue) requestValues).emergencyId;
        int teamOutletId = ((RequestValue) requestValues).teamOutletId;
        return remoteRepository.completeProfileEmergency(Constants.APP_CODE, emergencyId, teamOutletId);
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
        private final int emergencyId;
        private final int teamOutletId;

        public RequestValue(int emergencyId, int teamOutletId) {

            this.emergencyId = emergencyId;
            this.teamOutletId = teamOutletId;
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
