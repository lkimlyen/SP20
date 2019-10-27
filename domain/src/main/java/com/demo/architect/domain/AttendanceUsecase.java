package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.account.remote.AccountRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class AttendanceUsecase extends BaseUseCase<BaseResponse<Integer>> {
    private static final String TAG = AttendanceUsecase.class.getSimpleName();
    private final AccountRepository remoteRepository;

    public AttendanceUsecase(AccountRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>> buildUseCaseObservable() {
        String sessionCode = ((RequestValue) requestValues).sessionCode;
        int userTeamId = ((RequestValue) requestValues).userTeamId;
        String type = ((RequestValue) requestValues).type;
        double latitude = ((RequestValue) requestValues).latitude;
        double longitude = ((RequestValue) requestValues).longitude;
        int number = ((RequestValue) requestValues).number;
        String dateServer = ((RequestValue) requestValues).dateServer;
        return remoteRepository.attendanceTracking(Constants.APP_CODE, sessionCode,
                userTeamId,  type, latitude, longitude, number, dateServer);
    }
    @Override
    protected DisposableObserver<BaseResponse<Integer>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<Integer>>() {
            @Override
            public void onNext(BaseResponse<Integer> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    int result = data.getData();
                    if (result > 0 && data.getStatus() == 1) {
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
        private final String sessionCode;
        private final int userTeamId;
        private final String type;
        private final double latitude;
        private final double longitude;
        private final int number;
        private final String dateServer;


        public RequestValue(String sessionCode, int userTeamId, String type, double latitude, double longitude, int number, String dateServer) {
            this.sessionCode = sessionCode;
            this.userTeamId = userTeamId;
            this.type = type;
            this.latitude = latitude;
            this.longitude = longitude;
            this.number = number;
            this.dateServer = dateServer;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private int id;

        public ResponseValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
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
