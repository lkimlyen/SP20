package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BrandSetEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetBrandSetUsecase extends BaseUseCase<BaseListResponse<BrandSetEntity>> {
    private static final String TAG = GetBrandSetUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetBrandSetUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<BrandSetEntity>> buildUseCaseObservable() {
        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.getBrandSet(projectId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<BrandSetEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<BrandSetEntity>>() {
            @Override
            public void onNext(BaseListResponse<BrandSetEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<BrandSetEntity>  result= data.getData();
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
        private List<BrandSetEntity> list;

        public ResponseValue(List<BrandSetEntity> list) {
            this.list = list;
        }

        public List<BrandSetEntity> getList() {
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
