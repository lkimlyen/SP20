package com.demo.architect.data.repository.base.gift.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.CurrentBrandSetEntity;
import com.demo.architect.data.model.GiftMegaEntity;
import com.demo.architect.data.model.ProductGiftEntity;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class GiftRepositoryImpl implements GiftRepository {

    private final static String TAG = GiftRepositoryImpl.class.getName();

    private GiftApiInterface mRemoteApiInterface;

    public GiftRepositoryImpl(GiftApiInterface mGiftApiInterface) {
        this.mRemoteApiInterface = mGiftApiInterface;
    }

    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleConfirmSetResponse(Call<BaseResponse<ConfirmSetEntity>> call, ObservableEmitter<BaseResponse<ConfirmSetEntity>> emitter) {
        try {
            BaseResponse<ConfirmSetEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleCurrentBrandSetResponse(Call<BaseListResponse<CurrentBrandSetEntity>> call, ObservableEmitter<BaseListResponse<CurrentBrandSetEntity>> emitter) {
        try {
            BaseListResponse<CurrentBrandSetEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleConfirmResponse(Call<BaseListResponse<ConfirmSetEntity>> call, ObservableEmitter<BaseListResponse<ConfirmSetEntity>> emitter) {
        try {
            BaseListResponse<ConfirmSetEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleGiftResponse(Call<BaseListResponse<GiftMegaEntity>> call, ObservableEmitter<BaseListResponse<GiftMegaEntity>> emitter) {
        try {
            BaseListResponse<GiftMegaEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleProductGiftResponse(Call<BaseListResponse<ProductGiftEntity>> call, ObservableEmitter<BaseListResponse<ProductGiftEntity>> emitter) {
        try {
            BaseListResponse<ProductGiftEntity> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }


    @Override
    public Observable<BaseResponse<Integer>> addCustomer(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.addCustomer(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addCustomerProduct(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addCustomerProduct(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addCustomerImage(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addCustomerImage(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addCustomerGift(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addCustomerGift(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ProductGiftEntity>> getProductGift(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductGiftEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductGiftEntity>> emitter) throws Exception {
                handleProductGiftResponse(mRemoteApiInterface.getProductGift(projectId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<CurrentBrandSetEntity>> getCurrentSet(final String appCode, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<CurrentBrandSetEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<CurrentBrandSetEntity>> emitter) throws Exception {
                handleCurrentBrandSetResponse(mRemoteApiInterface.getCurrentSet(appCode, outletId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<Integer>> addWarehouseRequirementSet(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.addWarehouseRequirementSet(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> updateCurrentGift(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateCurrentGift(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> confirmWarehouseRequirementSet(final Map<String, Object> params) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

                handleBaseResponse(mRemoteApiInterface.confirmWarehouseRequirementSet(System.currentTimeMillis()/1000,body), emitter);
            }
        });

    }


    @Override
    public Observable<BaseResponse> updateChangeSet(final String appCode, final int outletId, final int requimentChangeSetID) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateChangeSet(appCode, outletId, requimentChangeSetID), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addBrandSetUsed(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addBrandSetUsed(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ConfirmSetEntity>> getWarehouseRequirement(final String tokenBearer,final long token, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ConfirmSetEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ConfirmSetEntity>> emitter) throws Exception {
                handleConfirmResponse(mRemoteApiInterface.getWarehouseRequirement(tokenBearer,outletId, token), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<GiftMegaEntity>> getDataLuckyMega(final String appCode) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<GiftMegaEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<GiftMegaEntity>> emitter) throws Exception {
                handleGiftResponse(mRemoteApiInterface.getDataLuckyMega(appCode), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addCustomerTotalDialMega(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addCustomerTotalDialMega(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> addCustomerGiftMega(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addCustomerGiftMega(appCode, data), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse> readedNotificationSetGiftMega(final String appCode, final int outletId, final int giftId, final int teamOutletID) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.readedNotificationSetGiftMega(appCode, outletId, giftId, teamOutletID), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<ConfirmSetEntity>> sendRequestGift(final Map<String, Object> bradnsetList) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<ConfirmSetEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ConfirmSetEntity>> emitter) throws Exception {

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(bradnsetList)).toString());
                long queryToken = System.currentTimeMillis()/1000;
                handleConfirmSetResponse(mRemoteApiInterface.sendRequestGift(queryToken,body), emitter);
            }
        });
    }
}


