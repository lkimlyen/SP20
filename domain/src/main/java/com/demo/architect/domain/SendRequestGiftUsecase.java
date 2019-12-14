package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class SendRequestGiftUsecase extends BaseUseCase<BaseResponse<ConfirmSetEntity>> {
    private static final String TAG = SendRequestGiftUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public SendRequestGiftUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<ConfirmSetEntity>> buildUseCaseObservable() {
       int spId = ((RequestValue)requestValues).spId;
        LinkedHashMap<Integer, Integer> brandSetId = ((RequestValue)requestValues).bransetId;
        return remoteRepository.sendRequestGift(spId, brandSetId);
    }


    @Override
    protected DisposableObserver<BaseResponse<ConfirmSetEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<ConfirmSetEntity>>() {
            @Override
            public void onNext(BaseResponse<ConfirmSetEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {

                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getData()));
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
      private final int spId;
      private final LinkedHashMap<Integer, Integer> bransetId;


        public RequestValue(int spId, LinkedHashMap<Integer, Integer> bransetId) {
            this.spId = spId;
            this.bransetId = bransetId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private ConfirmSetEntity confirmSetEntity;

        public ResponseValue(ConfirmSetEntity confirmSetEntity) {
            this.confirmSetEntity = confirmSetEntity;
        }

        public ConfirmSetEntity getDescription() {
            return confirmSetEntity;
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
