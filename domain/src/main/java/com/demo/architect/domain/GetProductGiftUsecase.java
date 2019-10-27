package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductGiftEntity;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetProductGiftUsecase extends BaseUseCase<BaseListResponse<ProductGiftEntity>> {
    private static final String TAG = GetProductGiftUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public GetProductGiftUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ProductGiftEntity>> buildUseCaseObservable() {
        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.getProductGift(projectId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<ProductGiftEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ProductGiftEntity>>() {
            @Override
            public void onNext(BaseListResponse<ProductGiftEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ProductGiftEntity> result = data.getData();
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
        private final int projectId;

        public RequestValue(int projectId) {
            this.projectId = projectId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductGiftEntity> list;

        public ResponseValue(List<ProductGiftEntity> list) {
            this.list = list;
        }

        public List<ProductGiftEntity> getList() {
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
