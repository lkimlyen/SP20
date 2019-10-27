package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class ReadedNotificationSetGiftMegaUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = ReadedNotificationSetGiftMegaUsecase.class.getSimpleName();
    private final GiftRepository remoteRepository;

    public ReadedNotificationSetGiftMegaUsecase(GiftRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        int outletId = ((RequestValue) requestValues).outletId;
        int giftId = ((RequestValue) requestValues).giftId;
        int teamOutletId = ((RequestValue) requestValues).outletId;
        return remoteRepository.readedNotificationSetGiftMega(Constants.APP_CODE, outletId,giftId,teamOutletId);

    }

    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
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
        private final int giftId;
        private final int teamOutletId;

        public RequestValue(int outletId, int giftId, int teamOutletId) {
            this.outletId = outletId;
            this.giftId = giftId;
            this.teamOutletId = teamOutletId;
        }
    }

    public static final class ResponseValue implements ResponseValues {

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
