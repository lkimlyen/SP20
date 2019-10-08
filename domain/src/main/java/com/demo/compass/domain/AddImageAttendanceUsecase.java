package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.repository.base.account.remote.AccountRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by ACER on 7/26/2017.
 */

public class AddImageAttendanceUsecase extends BaseUseCase<BaseResponse<Integer>> {
    private static final String TAG = AddImageAttendanceUsecase.class.getSimpleName();
    private final AccountRepository uploadRepository;

    public AddImageAttendanceUsecase(AccountRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>> buildUseCaseObservable() {
        int attendanceId = ((RequestValue) requestValues).attendanceId;
        int imageId = ((RequestValue) requestValues).imageId;

        return uploadRepository.addImageForAttenadence(Constants.APP_CODE, attendanceId,
                imageId);
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
        private final int attendanceId;
        private final int imageId;

        public RequestValue( int attendanceId, int imageId) {
            this.attendanceId = attendanceId;
            this.imageId = imageId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private final int id;

        public ResponseValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static final class ErrorValue implements ErrorValues {

        private final String Description;

        public ErrorValue(String description) {
            Description = description;
        }

        public String getDescription() {
            return Description;
        }
    }

}
