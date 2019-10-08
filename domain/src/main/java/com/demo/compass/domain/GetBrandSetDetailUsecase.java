package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;




public class GetBrandSetDetailUsecase extends BaseUseCase<BaseListResponse<BrandSetDetailEntity>> {
    private static final String TAG = GetBrandSetDetailUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetBrandSetDetailUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<BrandSetDetailEntity>> buildUseCaseObservable() {
        int projectId = ((RequestValue) requestValues).projectId;
        return remoteRepository.getBrandSetDetail(projectId);
    }
    @Override
    protected DisposableObserver<BaseListResponse<BrandSetDetailEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<BrandSetDetailEntity>>() {
            @Override
            public void onNext(BaseListResponse<BrandSetDetailEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<BrandSetDetailEntity>  result= data.getData();
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
        private List<BrandSetDetailEntity> list;

        public ResponseValue(List<BrandSetDetailEntity> list) {
            this.list = list;
        }

        public List<BrandSetDetailEntity> getList() {
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
