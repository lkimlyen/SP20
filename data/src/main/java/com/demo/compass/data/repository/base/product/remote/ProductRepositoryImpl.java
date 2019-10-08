package com.demo.compass.data.repository.base.product.remote;


import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.model.BrandSetEntity;
import com.demo.compass.data.model.GiftEntity;
import com.demo.compass.data.model.ProductEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class ProductRepositoryImpl implements ProductRepository {

    private final static String TAG = ProductRepositoryImpl.class.getName();

    private ProductApiInterface mRemoteApiInterface;

    public ProductRepositoryImpl(ProductApiInterface mProductApiInterface) {
        this.mRemoteApiInterface = mProductApiInterface;
    }

    private void handleProductResponse(Call<BaseListResponse<ProductEntity>> call, ObservableEmitter<BaseListResponse<ProductEntity>> emitter) {
        try {
            BaseListResponse<ProductEntity> response = call.execute().body();

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

    private void handleIntegerResponse(Call<BaseListResponse<Integer>> call, ObservableEmitter<BaseListResponse<Integer>> emitter) {
        try {
            BaseListResponse<Integer> response = call.execute().body();

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
    private void handleBrandResponse(Call<BaseListResponse<BrandEntitiy>> call, ObservableEmitter<BaseListResponse<BrandEntitiy>> emitter) {
        try {
            BaseListResponse<BrandEntitiy> response = call.execute().body();

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
    private void handleBrandSetResponse(Call<BaseListResponse<BrandSetEntity>> call, ObservableEmitter<BaseListResponse<BrandSetEntity>> emitter) {
        try {
            BaseListResponse<BrandSetEntity> response = call.execute().body();

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


    private void handleBrandSetDetailResponse(Call<BaseListResponse<BrandSetDetailEntity>> call, ObservableEmitter<BaseListResponse<BrandSetDetailEntity>> emitter) {
        try {
            BaseListResponse<BrandSetDetailEntity> response = call.execute().body();

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

    private void handleGiftResponse(Call<BaseListResponse<GiftEntity>> call, ObservableEmitter<BaseListResponse<GiftEntity>> emitter) {
        try {
            BaseListResponse<GiftEntity> response = call.execute().body();

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
    public Observable<BaseListResponse<ProductEntity>> getProduct(final int projectId,final int outletId) {

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductEntity>> emitter) throws Exception {
                handleProductResponse(mRemoteApiInterface.getProduct(projectId,outletId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<BrandEntitiy>> getBrand(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<BrandEntitiy>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<BrandEntitiy>> emitter) throws Exception {
                handleBrandResponse(mRemoteApiInterface.getBrand(projectId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<BrandSetEntity>> getBrandSet(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<BrandSetEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<BrandSetEntity>> emitter) throws Exception {
                handleBrandSetResponse(mRemoteApiInterface.getBrandSet(projectId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<BrandSetDetailEntity>> getBrandSetDetail(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<BrandSetDetailEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<BrandSetDetailEntity>> emitter) throws Exception {
                handleBrandSetDetailResponse(mRemoteApiInterface.getBrandSetDetail(projectId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<GiftEntity>> getGift(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<GiftEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<GiftEntity>> emitter) throws Exception {
                handleGiftResponse(mRemoteApiInterface.getGift(projectId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<Integer>> getOutletBrand(final String appCode, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.getOutletBrand(appCode,outletId), emitter);
            }
        });
    }
}


