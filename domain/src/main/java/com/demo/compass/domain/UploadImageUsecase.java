package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.repository.base.upload.remote.UploadRepository;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by ACER on 7/26/2017.
 */

public class UploadImageUsecase extends BaseUseCase <BaseResponse<Integer>>{
    private static final String TAG = UploadImageUsecase.class.getSimpleName();
    private final UploadRepository uploadRepository;

    public UploadImageUsecase(UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>> buildUseCaseObservable() {
        File file = ((RequestValue) requestValues).file;
        String sessionCode = ((RequestValue) requestValues).sessionCode;
        int userTeamID = ((RequestValue) requestValues).userTeamID;
        String dateTimeDevice = ((RequestValue) requestValues).dateTimeDevice;
        double latitude = ((RequestValue) requestValues).latitude;
        double longitude = ((RequestValue) requestValues).longitude;
        int imageType = ((RequestValue) requestValues).imageType;
        String fileName = ((RequestValue) requestValues).fileName;
        return uploadRepository.uploadImage(file, Constants.APP_CODE, sessionCode, userTeamID,
                dateTimeDevice,  latitude, longitude,imageType, fileName);
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
        private final File file;
        private final String sessionCode;
        private final int userTeamID;
        private final String dateTimeDevice;
        private final double latitude;
        private final double longitude;
        private final int imageType;
        private final String fileName;

        public RequestValue(File file, String sessionCode, int userTeamID, String dateTimeDevice,  double latitude, double longitude, int imageType, String fileName) {
            this.file = file;
            this.sessionCode = sessionCode;
            this.userTeamID = userTeamID;
            this.dateTimeDevice = dateTimeDevice;
            this.imageType = imageType;
            this.latitude = latitude;
            this.longitude = longitude;
            this.fileName = fileName;
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
