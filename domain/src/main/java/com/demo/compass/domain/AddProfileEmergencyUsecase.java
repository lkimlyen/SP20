package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;




public class AddProfileEmergencyUsecase extends BaseUseCase <BaseResponse<Integer>>{
    private static final String TAG = AddProfileEmergencyUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public AddProfileEmergencyUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>> buildUseCaseObservable() {
        String code = ((RequestValue) requestValues).code;
        int teamOutletId = ((RequestValue) requestValues).teamOutletId;
        String employeeId = ((RequestValue) requestValues).employeeId;
        int reasonId = ((RequestValue) requestValues).reasonId;
        int outletId = ((RequestValue) requestValues).outletId;
        String startDateTime = ((RequestValue) requestValues).startDateTime;
        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.addProfileEmergency(Constants.APP_CODE, code, teamOutletId, employeeId, reasonId, outletId, startDateTime,projectId);
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

        private final String code;
        private final int teamOutletId;
        private final String employeeId;
        private final int reasonId;
        private final int outletId;
        private final String startDateTime;
        private final int projectId;

        public RequestValue(String code, int teamOutletId, String employeeId, int reasonId, int outletId, String startDateTime, int projectId) {
            this.code = code;
            this.teamOutletId = teamOutletId;
            this.employeeId = employeeId;
            this.reasonId = reasonId;
            this.outletId = outletId;
            this.startDateTime = startDateTime;
            this.projectId = projectId;
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
