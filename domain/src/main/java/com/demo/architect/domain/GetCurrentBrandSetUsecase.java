package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CurrentBrandSetEntity;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetCurrentBrandSetUsecase extends BaseUseCase<BaseListResponse<CurrentBrandSetEntity>> {
    private static final String TAG = GetCurrentBrandSetUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public GetCurrentBrandSetUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<CurrentBrandSetEntity>> buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.getCurrentSet(Constants.APP_CODE, outletId);

    }

    @Override
    protected DisposableObserver<BaseListResponse<CurrentBrandSetEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<CurrentBrandSetEntity>>() {
            @Override
            public void onNext(BaseListResponse<CurrentBrandSetEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<CurrentBrandSetEntity> result = data.getData();
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
        private List<CurrentBrandSetEntity> list;

        public ResponseValue(List<CurrentBrandSetEntity> list) {
            this.list = list;
        }

        public List<CurrentBrandSetEntity> getList() {
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
