package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;



public class GetProductUsecase extends BaseUseCase<BaseListResponse<ProductEntity>>  {
    private static final String TAG = GetProductUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetProductUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ProductEntity>>  buildUseCaseObservable() {
        int projectId = ((RequestValue) requestValues).projectId;

        int outletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.getProduct(projectId, outletId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<ProductEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ProductEntity>>() {
            @Override
            public void onNext(BaseListResponse<ProductEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ProductEntity> result = data.getData();
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
        private final int outletId;

        public RequestValue(int projectId, int outletId) {
            this.projectId = projectId;
            this.outletId = outletId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductEntity> list;

        public ResponseValue(List<ProductEntity> list) {
            this.list = list;
        }

        public List<ProductEntity> getList() {
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
