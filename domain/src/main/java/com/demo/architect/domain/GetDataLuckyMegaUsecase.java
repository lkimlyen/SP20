package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.GiftMegaEntity;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class GetDataLuckyMegaUsecase extends BaseUseCase<BaseListResponse<GiftMegaEntity>> {
    private static final String TAG = GetDataLuckyMegaUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public GetDataLuckyMegaUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<GiftMegaEntity>> buildUseCaseObservable() {
        return remoteRepository.getDataLuckyMega(Constants.APP_CODE);
    }


    @Override
    protected DisposableObserver<BaseListResponse<GiftMegaEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<GiftMegaEntity>>() {
            @Override
            public void onNext(BaseListResponse<GiftMegaEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<GiftMegaEntity> result = data.getData();
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
    }

    public static final class ResponseValue implements ResponseValues {
        private List<GiftMegaEntity> list;

        public ResponseValue(List<GiftMegaEntity> list) {
            this.list = list;
        }

        public List<GiftMegaEntity> getList() {
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
