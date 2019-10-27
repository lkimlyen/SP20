package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.BackgroundDialEntity;
import com.demo.architect.data.model.BrandEntitiy;
import com.demo.architect.data.model.BrandSetDetailEntity;
import com.demo.architect.data.model.BrandSetEntity;
import com.demo.architect.data.model.CurrentBrandSetEntity;
import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.data.model.GiftEntity;
import com.demo.architect.data.model.GiftMegaEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.NotificationEntity;
import com.demo.architect.data.model.POSMEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGiftEntity;
import com.demo.architect.data.model.ReasonEntity;
import com.demo.architect.data.model.offline.AttendanceModel;
import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CurrentGiftModel;
import com.demo.architect.data.model.offline.CustomerGiftMegaModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerImageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.DetailRequestGiftModel;
import com.demo.architect.data.model.offline.EmergencyModel;
import com.demo.architect.data.model.offline.GiftMegaModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.NoteStockModel;
import com.demo.architect.data.model.offline.NotificationModel;
import com.demo.architect.data.model.offline.POSMModel;
import com.demo.architect.data.model.offline.POSMReportModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.ReasonModel;
import com.demo.architect.data.model.offline.RequestGiftModel;
import com.demo.architect.data.model.offline.StockModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.data.model.offline.TotalRotationModel;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addNotification(final List<NotificationEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addNotficationItem(list);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<NotificationModel>> findAllNoti() {
        return Observable.create(new Observable.OnSubscribe<List<NotificationModel>>() {
            @Override
            public void call(Subscriber<? super List<NotificationModel>> subscriber) {
                try {
                    List<NotificationModel> models = databaseRealm.getListNoti();
                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<NotificationModel> deleteAllNotification() {
        return Observable.create(new Observable.OnSubscribe<NotificationModel>() {
            @Override
            public void call(Subscriber<? super NotificationModel> subscriber) {
                try {
                    Set<Integer> countersToDelete = new HashSet<>();

                    List<NotificationModel> list = databaseRealm.findAll(NotificationModel.class);
                    for (NotificationModel item : list) {
                        countersToDelete.add(item.getId());
                    }
                    databaseRealm.deleteItemsAsync(countersToDelete);
                    subscriber.onNext(new NotificationModel());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addAttendanceModel(final AttendanceModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addAttendanceModel(model);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Integer>> addImageModel(final List<ImageModel> list) {
        return Observable.create(new Observable.OnSubscribe<List<Integer>>() {
            @Override
            public void call(Subscriber<? super List<Integer>> subscriber) {
                try {
                    List<Integer> list1 = databaseRealm.addImage(list);

                    subscriber.onNext(list1);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addProductModel(final ProductEntity productEntity, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addProductModel(productEntity, path);

                    subscriber.onNext("Succces");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteProduct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteProduct();

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<ProductModel, StockModel>> getListStockByDate(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<ProductModel, StockModel>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<ProductModel, StockModel>> subscriber) {
                try {
                    LinkedHashMap<ProductModel, StockModel> list = databaseRealm.getListStockByDate(outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveListStock(final List<StockModel> list, final int outletId, final int type) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveListStock(list,outletId, type);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countNumberWaitingUpload(final int teamId, final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countNumberWaitingUpload(teamId, outletId);

                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<StockModel>> getListStockWaitingUpload(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<List<StockModel>>() {
            @Override
            public void call(Subscriber<? super List<StockModel>> subscriber) {
                try {
                    List<StockModel> list = databaseRealm.getListStockWaitingUpload(outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusStock(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusStock(outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateImageAttendance(final int attendaceId, final int imageId, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateImageAttendance(attendaceId, imageId, path);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<ProductModel, TakeOffVolumnModel>> getListTakeOffVolumnByDate(final int teamId, final int outletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<ProductModel, TakeOffVolumnModel>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<ProductModel, TakeOffVolumnModel>> subscriber) {
                try {
                    LinkedHashMap<ProductModel, TakeOffVolumnModel> list = databaseRealm.getListTakeOffVolumnByDate(teamId, outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveListTakeOffVolumn(final List<TakeOffVolumnModel> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveListTakeOffVolumn(list);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<TakeOffVolumnModel>> getListTakeOffWaitingUpload(final int teamOutletId, final int outletId) {
        return Observable.create(new Observable.OnSubscribe<List<TakeOffVolumnModel>>() {
            @Override
            public void call(Subscriber<? super List<TakeOffVolumnModel>> subscriber) {
                try {
                    List<TakeOffVolumnModel> list = databaseRealm.getListTakeOffWaitingUpload(teamOutletId, outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<String, String>> getListWaitingUpload(final int teamId, final int outletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<String, String>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<String, String>> subscriber) {
                try {
                    LinkedHashMap<String, String> list = databaseRealm.getListWaitingUpload(teamId, outletId);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusTakeOffVolumn(final int teamId, final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusTakeOffVolumn(teamId, outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addBrandSetDetail(final List<BrandSetDetailEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBrandSetDetail(list);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addBrand(final List<BrandEntitiy> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBrand(list);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGiftModel(final GiftEntity giftEntity, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGiftModel(giftEntity, path);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addBrandSet(final List<BrandSetEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBrandSet(list);

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteGift() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteGift();

                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ProductModel>> getListProductByBrandId(final List<Integer> idList) {
        return Observable.create(new Observable.OnSubscribe<List<ProductModel>>() {
            @Override
            public void call(Subscriber<? super List<ProductModel>> subscriber) {
                try {
                    List<ProductModel> list = databaseRealm.getListProductByBrandId(idList);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<BrandModel>> getListBrandById(final List<Integer> idList) {
        return Observable.create(new Observable.OnSubscribe<List<BrandModel>>() {
            @Override
            public void call(Subscriber<? super List<BrandModel>> subscriber) {
                try {
                    List<BrandModel> list = databaseRealm.getListBrandById(idList);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addBackgroundDial(final BackgroundDialEntity backgroundDialEntity, final String pathArrow, final String pathButton, final String pathLayout, final String pathCircle) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBackgroundDial(backgroundDialEntity, pathArrow, pathButton, pathLayout, pathCircle);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteAllBackgroundDial() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteAllBackgroundDial();

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<GiftModel>> getListGiftByBrandIdAndSetId(final int outletId, final int brandId) {
        return Observable.create(new Observable.OnSubscribe<List<GiftModel>>() {
            @Override
            public void call(Subscriber<? super List<GiftModel>> subscriber) {
                try {
                    List<GiftModel> list = databaseRealm.getListGiftByBrandIdAndSetId(outletId, brandId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<BackgroundDialModel> getBackgroundDialByBrandId(final int brandId) {
        return Observable.create(new Observable.OnSubscribe<BackgroundDialModel>() {
            @Override
            public void call(Subscriber<? super BackgroundDialModel> subscriber) {
                try {
                    BackgroundDialModel background = databaseRealm.getBackgroundDialByBrandId(brandId);

                    subscriber.onNext(background);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CurrentGiftModel>> getListCurrentGift(final int outletId, final int brandId) {
        return Observable.create(new Observable.OnSubscribe<List<CurrentGiftModel>>() {
            @Override
            public void call(Subscriber<? super List<CurrentGiftModel>> subscriber) {
                try {
                    List<CurrentGiftModel> list = databaseRealm.getListCurrentGift(outletId, brandId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> addCustomerModel(final CustomerModel customerModel) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer id = databaseRealm.addCustomerModel(customerModel);

                    subscriber.onNext(id);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCustomerProductModel(final List<CustomerProductModel> productModelList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCustomerProductModel(productModelList);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCustomerImageModel(final List<CustomerImageModel> customerImageModelList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCustomerImageModel(customerImageModelList);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCustomerGiftModel(final List<CustomerGiftModel> giftModelList, final int customerId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCustomerGiftModel(giftModelList, customerId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getListCustomerWaitingUpload(final int OutletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel, List<ImageModel>>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel, List<ImageModel>> list = databaseRealm.getListCustomerWaitingUpload(OutletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCustomerServerId(final int id, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCustomerServerId(id, serverId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCustomerImageServerId(final int cusId, final int imageId, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCustomerImageServerId(cusId, imageId, serverId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusCustomerProduct(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusCustomerProduct(outletId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusCustomerGift(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusCustomerGift(outletId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusCustomerImage(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusCustomerImage(outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<BrandModel> getBrandCurrent(final int brandId) {
        return Observable.create(new Observable.OnSubscribe<BrandModel>() {
            @Override
            public void call(Subscriber<? super BrandModel> subscriber) {
                try {
                    BrandModel brandModel = databaseRealm.getBrandCurrent(brandId);

                    subscriber.onNext(brandModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<GiftModel, DetailCurrentGiftModel>> getListGiftChangeByDate(final int outletID, final List<Integer> brandIdList) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<GiftModel, DetailCurrentGiftModel>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<GiftModel, DetailCurrentGiftModel>> subscriber) {
                try {
                    LinkedHashMap<GiftModel, DetailCurrentGiftModel> list = databaseRealm.getListGiftChangeByDate(outletID, brandIdList);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> getListCustomerGiftByDate(final String date) {
        //LinkedHashMap<String,List<CustomerGiftModel>>
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.getListCustomerGiftByDate(date);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addCurrentSetBrand(final List<CurrentBrandSetEntity> list, final int outletId, final int userId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addCurrentSetBrand(list, outletId, userId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addProductGift(final List<ProductGiftEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addProductGift(list);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<BrandModel, List<ProductGiftModel>>> getListGiftByProduct(final List<Integer> idProductList) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<BrandModel, List<ProductGiftModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<BrandModel, List<ProductGiftModel>>> subscriber) {
                try {
                    LinkedHashMap<BrandModel, List<ProductGiftModel>> list = databaseRealm.getListGiftByProduct(idProductList);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>> getListBrandSetDetailCurrent(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>> subscriber) {
                try {
                    LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>> list = databaseRealm.getListBrandSetDetailCurrent(outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkOrderCode(final String orderCode) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.checkOrderCode(orderCode);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkCustomerPhone(final String phone, final List<Integer> idListBrand) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.checkCustomerPhone(phone,idListBrand);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveRequestGift(final RequestGiftModel requestGiftModel, final List<DetailRequestGiftModel> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveRequestGift(requestGiftModel, list);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> updateCurrentGiftModel(final List<CurrentGiftModel> currentGiftModelList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateCurrentGiftModel(currentGiftModelList);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<RequestGiftModel>> getListConfirmReciever(final int outletID) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<RequestGiftModel>>() {
            @Override
            public void call(Subscriber<? super RealmResults<RequestGiftModel>> subscriber) {
                try {
                    RealmResults<RequestGiftModel> list = databaseRealm.getListConfirmReciever(outletID);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStateRequest(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStateRequest(id);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CurrentBrandModel>> getListBrandSetCurrent(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<List<CurrentBrandModel>>() {
            @Override
            public void call(Subscriber<? super List<CurrentBrandModel>> subscriber) {
                try {
                    List<CurrentBrandModel> list = databaseRealm.getListBrandSetCurrent(outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<BrandSetModel>> getListBrandSetByBrandId(final int brandId, final int outletType) {
        return Observable.create(new Observable.OnSubscribe<List<BrandSetModel>>() {
            @Override
            public void call(Subscriber<? super List<BrandSetModel>> subscriber) {
                try {
                    List<BrandSetModel> list = databaseRealm.getListBrandSetByBrandId(brandId, outletType);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateBrandSetCurrent(final int outletId, final int brandsetId, final int numberUsed) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateBrandSetCurrent(outletId, brandsetId,numberUsed);

                    subscriber.onNext("Successqaqqqqqqq");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addReason(final List<ReasonEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addReason(list);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ReasonModel>> getListReason() {
        return Observable.create(new Observable.OnSubscribe<List<ReasonModel>>() {
            @Override
            public void call(Subscriber<? super List<ReasonModel>> subscriber) {
                try {
                    List<ReasonModel> list = databaseRealm.getListReason();

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CurrentBrandModel> getListCurrentBrandSet(final int outletId, final int brandId) {
        return Observable.create(new Observable.OnSubscribe<CurrentBrandModel>() {
            @Override
            public void call(Subscriber<? super CurrentBrandModel> subscriber) {
                try {
                    CurrentBrandModel currentBrandModel = databaseRealm.getListCurrentBrandSet(outletId, brandId);

                    subscriber.onNext(currentBrandModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberCurrentBrand(final List<CurrentBrandModel> currentBrandModels) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberCurrentBrand(currentBrandModels);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addRotationToBackground(final int brandId, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addRotationToBackground(brandId, path);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addEmergency(final EmergencyModel emergencyModel) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addEmergency(emergencyModel);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<EmergencyModel>> getEmergencyNotFinished(final int userId) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<EmergencyModel>>() {
            @Override
            public void call(Subscriber<? super RealmResults<EmergencyModel>> subscriber) {
                try {
                    RealmResults<EmergencyModel> list = databaseRealm.getEmergencyNotFinished(userId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStateEmergency(final int emergencyId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStateEmergency(emergencyId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkConditionChangeBrand(final int brandId, final int brandSetIdSelect) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean b = databaseRealm.checkConditionChangeBrand(brandId, brandSetIdSelect);

                    subscriber.onNext(b);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusCurrentGift(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusCurrentGift(outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusCurrentBrand(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusCurrentBrand(outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deletePOSM() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deletePOSM();

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addPOSMModel(final POSMEntity posmEntity, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addPOSMModel(posmEntity, path);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>> getListPOSM(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>> subscriber) {
                try {
                    LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> list = databaseRealm.getListPOSM(outletId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveListPOSMReport(final List<POSMReportModel> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveListPOSMReport(list);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusPOSM(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusPOSM(outletId);

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>> getListBrandSetById() {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>> subscriber) {
                try {
                    LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> list = databaseRealm.getListBrandSetById();

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> clearImage() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.clearImage();

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<NoteStockModel> getNoteStock(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<NoteStockModel>() {
            @Override
            public void call(Subscriber<? super NoteStockModel> subscriber) {
                try {
                    NoteStockModel noteStockModel =  databaseRealm.getNoteStock(outletId);

                    subscriber.onNext(noteStockModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveNoteStock(final int outletId, final int type) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveNoteStock(outletId,type);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusNoteStock(final int outletId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusNoteStock(outletId);

                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerModel> getInfoCusByPhone(final String phone) {
        return Observable.create(new Observable.OnSubscribe<CustomerModel>() {
            @Override
            public void call(Subscriber<? super CustomerModel> subscriber) {
                try {
                    CustomerModel customerModel =   databaseRealm.getInfoCusByPhone(phone);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<BrandSetDetailModel>> getListBrandSetDetail(final int brandId) {
        return Observable.create(new Observable.OnSubscribe<List<BrandSetDetailModel>>() {
            @Override
            public void call(Subscriber<? super List<BrandSetDetailModel>> subscriber) {
                try {
                    List<BrandSetDetailModel> brandSetDetailModelList =   databaseRealm.getListBrandSetDetail(brandId);

                    subscriber.onNext(brandSetDetailModelList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<GiftModel>> getAllGift() {
        return Observable.create(new Observable.OnSubscribe<List<GiftModel>>() {
            @Override
            public void call(Subscriber<? super List<GiftModel>> subscriber) {
                try {
                    List<GiftModel> giftModelList =   databaseRealm.getAllGift();

                    subscriber.onNext(giftModelList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<BrandModel, Integer>> getListNumberGiftWithBrand(final String phone, final List<Integer> idListBrand) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<BrandModel, Integer>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<BrandModel, Integer>> subscriber) {
                try {
                    LinkedHashMap<BrandModel, Integer> list =   databaseRealm.getListNumberGiftWithBrand(phone,idListBrand);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteGiftMega() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteGiftMega();

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<GiftMegaModel>> addGiftMegaAndTime(final List<GiftMegaEntity> list) {
        return Observable.create(new Observable.OnSubscribe<List<GiftMegaModel>>() {
            @Override
            public void call(Subscriber<? super List<GiftMegaModel>> subscriber) {
                try {
                    List<GiftMegaModel> giftModelList =   databaseRealm.addGiftMegaAndTime(list);

                    subscriber.onNext(giftModelList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateFilePathGiftMega(final int id, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateFilePathGiftMega(id,path);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<TimeRotationModel> getListGiftMegaByDate() {
        return Observable.create(new Observable.OnSubscribe<TimeRotationModel>() {
            @Override
            public void call(Subscriber<? super TimeRotationModel> subscriber) {
                try {
                    TimeRotationModel timeRotationModel =   databaseRealm.getListGiftMegaByDate();

                    subscriber.onNext(timeRotationModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addImageRotaion(final int id, final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addImageRotaion(id,path);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel,TotalRotationModel>> getCustomerById(final int customerId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel,TotalRotationModel>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel,TotalRotationModel>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel,TotalRotationModel> cu =   databaseRealm.getCustomerById(customerId);

                    subscriber.onNext(cu);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveTotalRotaion(final int cusId, final int totalRotation, final int teamOutletId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveTotalRotaion(cusId,totalRotation,teamOutletId);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateStatusTotalRotation() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer integer = databaseRealm.updateStatusTotalRotation();

                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveInfoCusLucky(final CustomerGiftMegaModel customerGiftMegaModel) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveInfoCusLucky(customerGiftMegaModel);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getCusLuckyUploadServer(final int cusId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<CustomerModel, List<ImageModel>>> subscriber) {
                try {
                    LinkedHashMap<CustomerModel, List<ImageModel>> list = databaseRealm.getCusLuckyUploadServer(cusId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CustomerProductModel>> getListProductByCustomer(final int cusId) {
        return Observable.create(new Observable.OnSubscribe<List<CustomerProductModel>>() {
            @Override
            public void call(Subscriber<? super List<CustomerProductModel>> subscriber) {
                try {
                    List<CustomerProductModel>list = databaseRealm.getListProductByCustomer(cusId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CustomerImageModel>> getListImageByCustomer(final int cusId) {
        return Observable.create(new Observable.OnSubscribe<List<CustomerImageModel>>() {
            @Override
            public void call(Subscriber<? super List<CustomerImageModel>> subscriber) {
                try {
                    List<CustomerImageModel> list = databaseRealm.getListImageByCustomer(cusId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerGiftMegaModel> getCustomerGiftMega(final int cusId) {
        return Observable.create(new Observable.OnSubscribe<CustomerGiftMegaModel>() {
            @Override
            public void call(Subscriber<? super CustomerGiftMegaModel> subscriber) {
                try {
                    CustomerGiftMegaModel model = databaseRealm.getCustomerGiftMega(cusId);

                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> uploadStatusCustomerGiftMega() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusCustomerGiftMega();

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkUserCheckIn(final int teamOutletId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean aBoolean =  databaseRealm.checkUserCheckIn(teamOutletId);

                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveTotalRotaionWithServerId(final int cusId, final int cusIDServer, final int totalRotation, final int teamOutletId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveTotalRotaionWithServerId(cusId,cusIDServer,totalRotation,teamOutletId);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberTunedRotation(final int customerId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberTunedRotation(customerId);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerModel> getInfoCusNotFinished(final String orderCode, final String phone, final int createdBy, final boolean mega, final boolean SP) {
        return Observable.create(new Observable.OnSubscribe<CustomerModel>() {
            @Override
            public void call(Subscriber<? super CustomerModel> subscriber) {
                try {
                    CustomerModel customerModel = databaseRealm.getInfoCusNotFinished(orderCode, phone,createdBy,mega,SP);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CustomerProductModel>> getListProductByCustomerId(final int id) {
        return Observable.create(new Observable.OnSubscribe<List<CustomerProductModel>>() {
            @Override
            public void call(Subscriber<? super List<CustomerProductModel>> subscriber) {
                try {
                    List<CustomerProductModel> customerModel = databaseRealm.getListProductByCustomerId(id);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ImageModel>> getListImageByCustomerId(final int id) {
        return Observable.create(new Observable.OnSubscribe<List<ImageModel>>() {
            @Override
            public void call(Subscriber<? super List<ImageModel>> subscriber) {
                try {
                    List<ImageModel>customerModel = databaseRealm.getListImageByCustomerId(id);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveTotalRotaionWithBrand(final List<TotalRotationBrandModel> rotationBrandModelList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveTotalRotaionWithBrand(rotationBrandModelList);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerModel> getInfoCustomerById(final int customerId) {
        return Observable.create(new Observable.OnSubscribe<CustomerModel>() {
            @Override
            public void call(Subscriber<? super CustomerModel> subscriber) {
                try {
                    CustomerModel customerModel = databaseRealm.getInfoCustomerById(customerId);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<TotalRotationBrandModel>> getListTotalRotationBrand(final int customerId) {
        return Observable.create(new Observable.OnSubscribe< List<TotalRotationBrandModel>>() {
            @Override
            public void call(Subscriber<? super  List<TotalRotationBrandModel>> subscriber) {
                try {
                    List<TotalRotationBrandModel> list = databaseRealm.getListTotalRotationBrand(customerId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberTurnedAndSaveGift
            (final int id, final CustomerGiftModel model, final List<CurrentGift> currentGiftList, final CurrentBrandModel currentBrandModel, final boolean finish) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberTurnedAndSaveGift(id,model,currentGiftList,currentBrandModel,finish);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> finishRotationBrandCustomer(final int customerId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.finishRotationBrandCustomer(customerId);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveTotalProductChooseGift(final List<TotalChangeGiftModel> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveTotalProductChooseGift(list);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<TotalChangeGiftModel>> getListProductChooseGift(final int customerId) {
        return Observable.create(new Observable.OnSubscribe< List<TotalChangeGiftModel>>() {
            @Override
            public void call(Subscriber<? super  List<TotalChangeGiftModel>> subscriber) {
                try {
                    List<TotalChangeGiftModel> list = databaseRealm.getListProductChooseGift(customerId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<CustomerGiftModel>> getGiftByCustomerId(final int customerId) {
        return Observable.create(new Observable.OnSubscribe< List<CustomerGiftModel>>() {
            @Override
            public void call(Subscriber<? super  List<CustomerGiftModel>> subscriber) {
                try {
                    List<CustomerGiftModel> list = databaseRealm.getGiftByCustomerId(customerId);

                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusCustomerImageById(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusCustomerImageById(id);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusCustomer() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusCustomer();

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
