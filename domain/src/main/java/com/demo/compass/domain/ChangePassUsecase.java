package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.repository.base.account.remote.AccountRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class ChangePassUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = ChangePassUsecase.class.getSimpleName();
    private final AccountRepository remoteRepository;

    public ChangePassUsecase(AccountRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        int teamOutletId = ((RequestValue) requestValues).teamOutletId;
        String passOld = ((RequestValue) requestValues).passNew;
        String passNew = ((RequestValue) requestValues).passOld;
        return remoteRepository.changePass(Constants.APP_CODE, teamOutletId,passOld, passNew);
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
        private final int teamOutletId;
        private final String passNew;
        private final String passOld;

        public RequestValue(int teamOutletId, String passNew, String passOld) {
            this.teamOutletId = teamOutletId;
            this.passNew = passNew;
            this.passOld = passOld;
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
