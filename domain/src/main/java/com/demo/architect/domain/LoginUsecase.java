package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.repository.base.account.remote.AccountRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class LoginUsecase extends BaseUseCase<BaseResponse<UserEntity>> {
    private static final String TAG = LoginUsecase.class.getSimpleName();
    private final AccountRepository remoteRepository;

    public LoginUsecase(AccountRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<UserEntity>> buildUseCaseObservable() {
        String userName = ((RequestValue) requestValues).userName;
        String password = ((RequestValue) requestValues).password;
        String devideToken =  ((RequestValue) requestValues).deviceToken;

        String deviceId =  ((RequestValue) requestValues).deviceId;
        return remoteRepository.login(Constants.APP_CODE, Constants.USER_TYPE,userName, password,devideToken,deviceId);
    }

    @Override
    protected DisposableObserver<BaseResponse<UserEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<UserEntity>>() {
            @Override
            public void onNext(BaseResponse<UserEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));

                    if (useCaseCallback != null) {
                        UserEntity result = data.getData();
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
                    useCaseCallback.onError(new AddProfileEmergencyUsecase.ErrorValue(e.getMessage()));
                }
            }
        };
    }



    public static final class RequestValue implements RequestValues {
        public final String userName;
        public final String password;
        public final String deviceToken;
        private final String deviceId;
        public RequestValue(String userName, String password, String deviceToken, String deviceId) {
            this.userName = userName;
            this.password = password;
            this.deviceToken = deviceToken;
            this.deviceId = deviceId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private UserEntity entity;

        public ResponseValue(UserEntity entity) {
            this.entity = entity;
        }

        public UserEntity getEntity() {
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
