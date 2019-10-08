package com.demo.compass.domain;

import android.util.Log;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetOutletBrandUsecase extends BaseUseCase<BaseListResponse<Integer>> {
    private static final String TAG = GetOutletBrandUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetOutletBrandUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<Integer>> buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.getOutletBrand(Constants.APP_CODE, outletId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<Integer>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<Integer>>() {
            @Override
            public void onNext(BaseListResponse<Integer> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    if (useCaseCallback != null) {
                        List<Integer> result = data.getData();
                        if (result != null && data.getStatus() == 1) {
                            useCaseCallback.onSuccess(new ResponseValue(result));
                        } else {
                            useCaseCallback.onError(new ErrorValue(data.getDescription()));
                        }
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
        private List<Integer> list;

        public ResponseValue(List<Integer> list) {
            this.list = list;
        }

        public List<Integer> getList() {
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
