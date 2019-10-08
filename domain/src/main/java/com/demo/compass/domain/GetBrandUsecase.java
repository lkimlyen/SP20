package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetBrandUsecase extends BaseUseCase<BaseListResponse<BrandEntitiy>> {
    private static final String TAG = GetBrandUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetBrandUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<BrandEntitiy>> buildUseCaseObservable() {
        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.getBrand(projectId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<BrandEntitiy>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<BrandEntitiy>>() {
            @Override
            public void onNext(BaseListResponse<BrandEntitiy> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<BrandEntitiy>  result= data.getData();
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
        private List<BrandEntitiy> list;

        public ResponseValue(List<BrandEntitiy> list) {
            this.list = list;
        }

        public List<BrandEntitiy> getList() {
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
