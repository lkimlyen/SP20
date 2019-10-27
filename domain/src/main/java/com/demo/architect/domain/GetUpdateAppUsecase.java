package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetUpdateAppUsecase extends BaseUseCase<BaseResponse<String>>  {
    private static final String TAG = GetUpdateAppUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetUpdateAppUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<String>>  buildUseCaseObservable() {
        int projectId = ((RequestValue)requestValues).projectId;
        return remoteRepository.getUpdateVersion(projectId);
    }

    @Override
    protected DisposableObserver<BaseResponse<String>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<String>>() {
            @Override
            public void onNext(BaseResponse<String> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    String result = data.getData();
                    if (data != null) {
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
        private String link;

        public ResponseValue(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
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
