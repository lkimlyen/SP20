package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


public class SendTopupCardUsecase extends BaseUseCase<BaseResponse>  {
    private static final String TAG = SendTopupCardUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public SendTopupCardUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse>  buildUseCaseObservable() {
        String phone = ((RequestValue)requestValues).phone;
        String phoneType = ((RequestValue)requestValues).phoneType;
        int outletId = ((RequestValue)requestValues).outletId;
        int userId = ((RequestValue)requestValues).userId;
        String phoneChangeGift = ((RequestValue)requestValues).phoneChangeGift;
        return remoteRepository.sendTopupCard(phone,phoneType,outletId,userId, "sp"
                ,"ciub6yO3YDiEsP5FkTki4R1unEeesjNcOyKWm-QLsSvzd1z0A1MeRET_n4a6_ZeNF5b_UduMleNz7Yfr76R_6K2YRH94UDNxXA2GjYG8njVEduXjdnV5TUtEZaaUcO",
                phoneChangeGift);
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
        private final String phone;
        private final String phoneType;
        private final int outletId;
        private final int userId;
        private final String phoneChangeGift;

        public RequestValue(String phone, String phoneType, int outletId, int userId, String phoneChangeGift) {
            this.phone = phone;
            this.phoneType = phoneType;
            this.outletId = outletId;
            this.userId = userId;
            this.phoneChangeGift = phoneChangeGift;
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
