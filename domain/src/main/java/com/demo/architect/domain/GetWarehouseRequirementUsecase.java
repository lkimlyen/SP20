package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetWarehouseRequirementUsecase extends BaseUseCase<BaseListResponse<ConfirmSetEntity>> {
    private static final String TAG = GetWarehouseRequirementUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public GetWarehouseRequirementUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ConfirmSetEntity>> buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.getWarehouseRequirement(Constants.APP_CODE, outletId);

    }
    @Override
    protected DisposableObserver<BaseListResponse<ConfirmSetEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ConfirmSetEntity>>() {
            @Override
            public void onNext(BaseListResponse<ConfirmSetEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ConfirmSetEntity> result = data.getData();
                    if ( data.getStatus() == 1) {
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
        private List<ConfirmSetEntity> list;

        public ResponseValue(List<ConfirmSetEntity> list) {
            this.list = list;
        }

        public List<ConfirmSetEntity> getList() {
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
