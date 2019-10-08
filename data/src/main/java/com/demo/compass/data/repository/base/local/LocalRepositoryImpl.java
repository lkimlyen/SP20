package com.demo.compass.data.repository.base.local;

import android.content.Context;

import com.demo.compass.data.model.BackgroundDialEntity;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.model.BrandSetEntity;
import com.demo.compass.data.model.CurrentBrandSetEntity;
import com.demo.compass.data.model.CurrentGift;
import com.demo.compass.data.model.GiftEntity;
import com.demo.compass.data.model.GiftMegaEntity;
import com.demo.compass.data.model.MessageModel;
import com.demo.compass.data.model.NotificationEntity;
import com.demo.compass.data.model.POSMEntity;
import com.demo.compass.data.model.ProductEntity;
import com.demo.compass.data.model.ProductGiftEntity;
import com.demo.compass.data.model.ReasonEntity;
import com.demo.compass.data.model.offline.AttendanceModel;
import com.demo.compass.data.model.offline.BackgroundDialModel;
import com.demo.compass.data.model.offline.BrandModel;
import com.demo.compass.data.model.offline.BrandSetDetailModel;
import com.demo.compass.data.model.offline.BrandSetModel;
import com.demo.compass.data.model.offline.CurrentBrandModel;
import com.demo.compass.data.model.offline.CurrentGiftModel;
import com.demo.compass.data.model.offline.CustomerGiftMegaModel;
import com.demo.compass.data.model.offline.CustomerGiftModel;
import com.demo.compass.data.model.offline.CustomerImageModel;
import com.demo.compass.data.model.offline.CustomerModel;
import com.demo.compass.data.model.offline.CustomerProductModel;
import com.demo.compass.data.model.offline.DetailCurrentGiftModel;
import com.demo.compass.data.model.offline.DetailRequestGiftModel;
import com.demo.compass.data.model.offline.EmergencyModel;
import com.demo.compass.data.model.offline.GiftMegaModel;
import com.demo.compass.data.model.offline.GiftModel;
import com.demo.compass.data.model.offline.ImageModel;
import com.demo.compass.data.model.offline.NoteStockModel;
import com.demo.compass.data.model.offline.NotificationModel;
import com.demo.compass.data.model.offline.POSMModel;
import com.demo.compass.data.model.offline.POSMReportModel;
import com.demo.compass.data.model.offline.ProductGiftModel;
import com.demo.compass.data.model.offline.ProductModel;
import com.demo.compass.data.model.offline.ReasonModel;
import com.demo.compass.data.model.offline.RequestGiftModel;
import com.demo.compass.data.model.offline.StockModel;
import com.demo.compass.data.model.offline.TakeOffVolumnModel;
import com.demo.compass.data.model.offline.TimeRotationModel;
import com.demo.compass.data.model.offline.TotalChangeGiftModel;
import com.demo.compass.data.model.offline.TotalRotationBrandModel;
import com.demo.compass.data.model.offline.TotalRotationModel;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmResults;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
               
                    databaseRealm.add(model);
                    emitter.onNext(model.getUuid());
                    emitter.onComplete();
                
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new ObservableOnSubscribe<List<MessageModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MessageModel>> emitter) throws Exception {

                List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                emitter.onNext(models);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addNotification(final List<NotificationEntity> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addNotficationItem(list);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<NotificationModel>> findAllNoti() {

        return Observable.create(new ObservableOnSubscribe<List<NotificationModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NotificationModel>> emitter) throws Exception {
                List<NotificationModel> models = databaseRealm.getListNoti();
                emitter.onNext(models);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<NotificationModel> deleteAllNotification() {

        return Observable.create(new ObservableOnSubscribe<NotificationModel>() {
            @Override
            public void subscribe(ObservableEmitter<NotificationModel> emitter) throws Exception {
                Set<Integer> countersToDelete = new HashSet<>();

                List<NotificationModel> list = databaseRealm.findAll(NotificationModel.class);
                for (NotificationModel item : list) {
                    countersToDelete.add(item.getId());
                }
                databaseRealm.deleteItemsAsync(countersToDelete);
                emitter.onNext(new NotificationModel());
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addAttendanceModel(final AttendanceModel model) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addAttendanceModel(model);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<Integer>> addImageModel(final List<ImageModel> list) {

        return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Integer>> emitter) throws Exception {
                List<Integer> list1 = databaseRealm.addImage(list);

                emitter.onNext(list1);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addProductModel(final ProductEntity productEntity, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addProductModel(productEntity, path);

                emitter.onNext("Succces");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> deleteProduct() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.deleteProduct();

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<ProductModel, StockModel>> getListStockByDate(final int outletId) {

        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<ProductModel, StockModel>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<ProductModel, StockModel>> emitter) throws Exception {

                LinkedHashMap<ProductModel, StockModel> list = databaseRealm.getListStockByDate(outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> saveListStock(final List<StockModel> list, final int outletId, final int type) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.saveListStock(list,outletId, type);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Integer> countNumberWaitingUpload(final int teamId, final int outletId) {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                int count = databaseRealm.countNumberWaitingUpload(teamId, outletId);

                emitter.onNext(count);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<StockModel>> getListStockWaitingUpload(final int outletId) {

        return Observable.create(new ObservableOnSubscribe<List<StockModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<StockModel>> emitter) throws Exception {

                List<StockModel> list = databaseRealm.getListStockWaitingUpload(outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<Integer> updateStatusStock(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Integer integer = databaseRealm.updateStatusStock(outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> updateImageAttendance(final int attendaceId, final int imageId, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateImageAttendance(attendaceId, imageId, path);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<LinkedHashMap<ProductModel, TakeOffVolumnModel>> getListTakeOffVolumnByDate(final int teamId, final int outletId) {

        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<ProductModel, TakeOffVolumnModel>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<ProductModel, TakeOffVolumnModel>> emitter) throws Exception {
                LinkedHashMap<ProductModel, TakeOffVolumnModel> list = databaseRealm.getListTakeOffVolumnByDate(teamId, outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> saveListTakeOffVolumn(final List<TakeOffVolumnModel> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.saveListTakeOffVolumn(list);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<TakeOffVolumnModel>> getListTakeOffWaitingUpload(final int teamOutletId, final int outletId) {

        return Observable.create(new ObservableOnSubscribe<List<TakeOffVolumnModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TakeOffVolumnModel>> emitter) throws Exception {
                List<TakeOffVolumnModel> list = databaseRealm.getListTakeOffWaitingUpload(teamOutletId, outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<LinkedHashMap<String, String>> getListWaitingUpload(final int teamId, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<String, String>> emitter) throws Exception {
                LinkedHashMap<String, String> list = databaseRealm.getListWaitingUpload(teamId, outletId);
                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<Integer> updateStatusTakeOffVolumn(final int teamId, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Integer integer = databaseRealm.updateStatusTakeOffVolumn(teamId, outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addBrandSetDetail(final List<BrandSetDetailEntity> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addBrandSetDetail(list);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addBrand(final List<BrandEntitiy> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addBrand(list);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addGiftModel(final GiftEntity giftEntity, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addGiftModel(giftEntity, path);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addBrandSet(final List<BrandSetEntity> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addBrandSet(list);

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> deleteGift() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.deleteGift();

                emitter.onNext("success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<ProductModel>> getListProductByBrandId(final List<Integer> idList) {
        return Observable.create(new ObservableOnSubscribe<List<ProductModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ProductModel>> emitter) throws Exception {

                List<ProductModel> list = databaseRealm.getListProductByBrandId(idList);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<BrandModel>> getListBrandById(final List<Integer> idList) {
        return Observable.create(new ObservableOnSubscribe<List<BrandModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BrandModel>> emitter) throws Exception {

                List<BrandModel> list = databaseRealm.getListBrandById(idList);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addBackgroundDial(final BackgroundDialEntity backgroundDialEntity, final String pathArrow, final String pathButton, final String pathLayout, final String pathCircle) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addBackgroundDial(backgroundDialEntity, pathArrow, pathButton, pathLayout, pathCircle);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> deleteAllBackgroundDial() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.deleteAllBackgroundDial();

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<GiftModel>> getListGiftByBrandIdAndSetId(final int outletId, final int brandId) {
        return Observable.create(new ObservableOnSubscribe<List<GiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<GiftModel>> emitter) throws Exception {
                List<GiftModel> list = databaseRealm.getListGiftByBrandIdAndSetId(outletId, brandId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<BackgroundDialModel> getBackgroundDialByBrandId(final int brandId) {
        return Observable.create(new ObservableOnSubscribe<BackgroundDialModel>() {
            @Override
            public void subscribe(ObservableEmitter<BackgroundDialModel> emitter) throws Exception {
                BackgroundDialModel background = databaseRealm.getBackgroundDialByBrandId(brandId);

                emitter.onNext(background);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<CurrentGiftModel>> getListCurrentGift(final int outletId, final int brandId) {
        return Observable.create(new ObservableOnSubscribe<List<CurrentGiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CurrentGiftModel>> emitter) throws Exception {
                List<CurrentGiftModel> list = databaseRealm.getListCurrentGift(outletId, brandId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });



    }

    @Override
    public Observable<Integer> addCustomerModel(final CustomerModel customerModel) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Integer id = databaseRealm.addCustomerModel(customerModel);

                emitter.onNext(id);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<String> addCustomerProductModel(final List<CustomerProductModel> productModelList) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addCustomerProductModel(productModelList);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addCustomerImageModel(final List<CustomerImageModel> customerImageModelList) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {


                databaseRealm.addCustomerImageModel(customerImageModelList);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addCustomerGiftModel(final List<CustomerGiftModel> giftModelList, final int customerId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addCustomerGiftModel(giftModelList, customerId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getListCustomerWaitingUpload(final int OutletId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel, List<ImageModel>>> emitter) throws Exception {
                LinkedHashMap<CustomerModel, List<ImageModel>> list = databaseRealm.getListCustomerWaitingUpload(OutletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addCustomerServerId(final int id, final int serverId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addCustomerServerId(id, serverId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addCustomerImageServerId(final int cusId, final int imageId, final int serverId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addCustomerImageServerId(cusId, imageId, serverId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> updateStatusCustomerProduct(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.updateStatusCustomerProduct(outletId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateStatusCustomerGift(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStatusCustomerGift(outletId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Integer> updateStatusCustomerImage(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Integer integer = databaseRealm.updateStatusCustomerImage(outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<BrandModel> getBrandCurrent(final int brandId) {
        return Observable.create(new ObservableOnSubscribe<BrandModel>() {
            @Override
            public void subscribe(ObservableEmitter<BrandModel> emitter) throws Exception {
                BrandModel brandModel = databaseRealm.getBrandCurrent(brandId);

                emitter.onNext(brandModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<GiftModel, DetailCurrentGiftModel>> getListGiftChangeByDate(final int outletID, final List<Integer> brandIdList) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<GiftModel, DetailCurrentGiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<GiftModel, DetailCurrentGiftModel>> emitter) throws Exception {
                LinkedHashMap<GiftModel, DetailCurrentGiftModel> list = databaseRealm.getListGiftChangeByDate(outletID, brandIdList);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> getListCustomerGiftByDate(final String date) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> emitter) throws Exception {
                LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.getListCustomerGiftByDate(date);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> addCurrentSetBrand(final List<CurrentBrandSetEntity> list, final int outletId, final int userId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addCurrentSetBrand(list, outletId, userId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addProductGift(final List<ProductGiftEntity> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addProductGift(list);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<BrandModel, List<ProductGiftModel>>> getListGiftByProduct(final List<Integer> idProductList) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<BrandModel, List<ProductGiftModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<BrandModel, List<ProductGiftModel>>> emitter) throws Exception {
                LinkedHashMap<BrandModel, List<ProductGiftModel>> list = databaseRealm.getListGiftByProduct(idProductList);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>> getListBrandSetDetailCurrent(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>> emitter) throws Exception {
                LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>> list = databaseRealm.getListBrandSetDetailCurrent(outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkOrderCode(final String orderCode) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> emitter) throws Exception {
                LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.checkOrderCode(orderCode);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkCustomerPhone(final String phone, final List<Integer> idListBrand) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> emitter) throws Exception {
                LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = databaseRealm.checkCustomerPhone(phone,idListBrand);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> saveRequestGift(final RequestGiftModel requestGiftModel, final List<DetailRequestGiftModel> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.saveRequestGift(requestGiftModel, list);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }


    @Override
    public Observable<String> updateCurrentGiftModel(final List<CurrentGiftModel> currentGiftModelList) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.updateCurrentGiftModel(currentGiftModelList);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<RealmResults<RequestGiftModel>> getListConfirmReciever(final int outletID) {
        return Observable.create(new ObservableOnSubscribe<RealmResults<RequestGiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<RealmResults<RequestGiftModel>> emitter) throws Exception {


                RealmResults<RequestGiftModel> list = databaseRealm.getListConfirmReciever(outletID);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> updateStateRequest(final int id) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {


                databaseRealm.updateStateRequest(id);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<CurrentBrandModel>> getListBrandSetCurrent(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<List<CurrentBrandModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CurrentBrandModel>> emitter) throws Exception {

                List<CurrentBrandModel> list = databaseRealm.getListBrandSetCurrent(outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<BrandSetModel>> getListBrandSetByBrandId(final int brandId, final int outletType) {
        return Observable.create(new ObservableOnSubscribe<List<BrandSetModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BrandSetModel>> emitter) throws Exception {

                List<BrandSetModel> list = databaseRealm.getListBrandSetByBrandId(brandId, outletType);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> updateBrandSetCurrent(final int outletId, final int brandsetId, final int numberUsed) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateBrandSetCurrent(outletId, brandsetId,numberUsed);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addReason(final List<ReasonEntity> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addReason(list);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<ReasonModel>> getListReason() {
        return Observable.create(new ObservableOnSubscribe<List<ReasonModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ReasonModel>> emitter) throws Exception {
                List<ReasonModel> list = databaseRealm.getListReason();

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<CurrentBrandModel> getListCurrentBrandSet(final int outletId, final int brandId) {
        return Observable.create(new ObservableOnSubscribe<CurrentBrandModel>() {
            @Override
            public void subscribe(ObservableEmitter<CurrentBrandModel> emitter) throws Exception {
                CurrentBrandModel currentBrandModel = databaseRealm.getListCurrentBrandSet(outletId, brandId);

                emitter.onNext(currentBrandModel);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<String> updateNumberCurrentBrand(final List<CurrentBrandModel> currentBrandModels) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateNumberCurrentBrand(currentBrandModels);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addRotationToBackground(final int brandId, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addRotationToBackground(brandId, path);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addEmergency(final EmergencyModel emergencyModel) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addEmergency(emergencyModel);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<RealmResults<EmergencyModel>> getEmergencyNotFinished(final int userId) {
        return Observable.create(new ObservableOnSubscribe<RealmResults<EmergencyModel>>() {
            @Override
            public void subscribe(ObservableEmitter<RealmResults<EmergencyModel>> emitter) throws Exception {
                RealmResults<EmergencyModel> list = databaseRealm.getEmergencyNotFinished(userId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> updateStateEmergency(final int emergencyId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStateEmergency(emergencyId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Boolean> checkConditionChangeBrand(final int brandId, final int brandSetIdSelect) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
        @Override
        public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
            boolean b = databaseRealm.checkConditionChangeBrand(brandId, brandSetIdSelect);

            emitter.onNext(b);
            emitter.onComplete();

        }
    });


    }

    @Override
    public Observable<Integer> updateStatusCurrentGift(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Integer integer = databaseRealm.updateStatusCurrentGift(outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Integer> updateStatusCurrentBrand(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Integer integer = databaseRealm.updateStatusCurrentBrand(outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> deletePOSM() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.deletePOSM();

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addPOSMModel(final POSMEntity posmEntity, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.addPOSMModel(posmEntity, path);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>> getListPOSM(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>> emitter) throws Exception {
                LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> list = databaseRealm.getListPOSM(outletId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> saveListPOSMReport(final List<POSMReportModel> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.saveListPOSMReport(list);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Integer> updateStatusPOSM(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Integer integer = databaseRealm.updateStatusPOSM(outletId);

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>> getListBrandSetById() {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>> emitter) throws Exception {

                LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> list = databaseRealm.getListBrandSetById();

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> clearImage() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.clearImage();

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<NoteStockModel> getNoteStock(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<NoteStockModel>() {
            @Override
            public void subscribe(ObservableEmitter<NoteStockModel> emitter) throws Exception {

                NoteStockModel noteStockModel =  databaseRealm.getNoteStock(outletId);

                emitter.onNext(noteStockModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> saveNoteStock(final int outletId, final int type) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.saveNoteStock(outletId,type);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateStatusNoteStock(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStatusNoteStock(outletId);

                emitter.onNext("Success");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<CustomerModel> getInfoCusByPhone(final String phone) {
        return Observable.create(new ObservableOnSubscribe<CustomerModel>() {
            @Override
            public void subscribe(ObservableEmitter<CustomerModel> emitter) throws Exception {

                CustomerModel customerModel =   databaseRealm.getInfoCusByPhone(phone);

                emitter.onNext(customerModel);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<List<BrandSetDetailModel>> getListBrandSetDetail(final int brandId) {
        return Observable.create(new ObservableOnSubscribe<List<BrandSetDetailModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BrandSetDetailModel>> emitter) throws Exception {
                List<BrandSetDetailModel> brandSetDetailModelList =   databaseRealm.getListBrandSetDetail(brandId);

                emitter.onNext(brandSetDetailModelList);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<GiftModel>> getAllGift() {
        return Observable.create(new ObservableOnSubscribe<List<GiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<GiftModel>> emitter) throws Exception {
                List<GiftModel> giftModelList =   databaseRealm.getAllGift();

                emitter.onNext(giftModelList);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<BrandModel, Integer>> getListNumberGiftWithBrand(final String phone, final List<Integer> idListBrand) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<BrandModel, Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<BrandModel, Integer>> emitter) throws Exception {
                LinkedHashMap<BrandModel, Integer> list =   databaseRealm.getListNumberGiftWithBrand(phone,idListBrand);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> deleteGiftMega() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.deleteGiftMega();

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<GiftMegaModel>> addGiftMegaAndTime(final List<GiftMegaEntity> list) {
        return Observable.create(new ObservableOnSubscribe<List<GiftMegaModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<GiftMegaModel>> emitter) throws Exception {
                List<GiftMegaModel> giftModelList =   databaseRealm.addGiftMegaAndTime(list);

                emitter.onNext(giftModelList);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<String> updateFilePathGiftMega(final int id, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateFilePathGiftMega(id,path);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<TimeRotationModel> getListGiftMegaByDate() {
        return Observable.create(new ObservableOnSubscribe<TimeRotationModel>() {
            @Override
            public void subscribe(ObservableEmitter<TimeRotationModel> emitter) throws Exception {
                TimeRotationModel timeRotationModel =   databaseRealm.getListGiftMegaByDate();

                emitter.onNext(timeRotationModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> addImageRotaion(final int id, final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.addImageRotaion(id,path);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, TotalRotationModel>> getCustomerById(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel,TotalRotationModel>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel,TotalRotationModel>> emitter) throws Exception {
                LinkedHashMap<CustomerModel,TotalRotationModel> cu =   databaseRealm.getCustomerById(customerId);

                emitter.onNext(cu);
                emitter.onComplete();

            }
        });


    }

    @Override
    public Observable<String> saveTotalRotaion(final int cusId, final int totalRotation, final int teamOutletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.saveTotalRotaion(cusId,totalRotation,teamOutletId);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Integer> updateStatusTotalRotation() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Integer integer = databaseRealm.updateStatusTotalRotation();

                emitter.onNext(integer);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> saveInfoCusLucky(final CustomerGiftMegaModel customerGiftMegaModel) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.saveInfoCusLucky(customerGiftMegaModel);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getCusLuckyUploadServer(final int cusId) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<CustomerModel, List<ImageModel>>> emitter) throws Exception {
                LinkedHashMap<CustomerModel, List<ImageModel>> list = databaseRealm.getCusLuckyUploadServer(cusId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<CustomerProductModel>> getListProductByCustomer(final int cusId) {
        return Observable.create(new ObservableOnSubscribe<List<CustomerProductModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CustomerProductModel>> emitter) throws Exception {

                List<CustomerProductModel>list = databaseRealm.getListProductByCustomer(cusId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<List<CustomerImageModel>> getListImageByCustomer(final int cusId) {
        return Observable.create(new ObservableOnSubscribe<List<CustomerImageModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CustomerImageModel>> emitter) throws Exception {

                List<CustomerImageModel> list = databaseRealm.getListImageByCustomer(cusId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<CustomerGiftMegaModel> getCustomerGiftMega(final int cusId) {
        return Observable.create(new ObservableOnSubscribe<CustomerGiftMegaModel>() {
            @Override
            public void subscribe(ObservableEmitter<CustomerGiftMegaModel> emitter) throws Exception {

                CustomerGiftMegaModel model = databaseRealm.getCustomerGiftMega(cusId);

                emitter.onNext(model);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> uploadStatusCustomerGiftMega() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStatusCustomerGiftMega();

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<Boolean> checkUserCheckIn(final int teamOutletId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Boolean aBoolean = databaseRealm.checkUserCheckIn(teamOutletId);

                emitter.onNext(aBoolean);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> saveTotalRotaionWithServerId(final int cusId, final int cusIDServer, final int totalRotation, final int teamOutletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.saveTotalRotaionWithServerId(cusId,cusIDServer,totalRotation,teamOutletId);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateNumberTunedRotation(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateNumberTunedRotation(customerId);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<CustomerModel> getInfoCusNotFinished(final String orderCode, final String phone, final int createdBy, final boolean mega, final boolean SP) {
        return Observable.create(new ObservableOnSubscribe<CustomerModel>() {
            @Override
            public void subscribe(ObservableEmitter<CustomerModel> emitter) throws Exception {

                CustomerModel customerModel = databaseRealm.getInfoCusNotFinished(orderCode, phone,createdBy,mega,SP);

                emitter.onNext(customerModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<CustomerProductModel>> getListProductByCustomerId(final int id) {
        return Observable.create(new ObservableOnSubscribe<List<CustomerProductModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CustomerProductModel>> emitter) throws Exception {

                List<CustomerProductModel> customerModel = databaseRealm.getListProductByCustomerId(id);

                emitter.onNext(customerModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<ImageModel>> getListImageByCustomerId(final int id) {
        return Observable.create(new ObservableOnSubscribe<List<ImageModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageModel>> emitter) throws Exception {

                List<ImageModel>customerModel = databaseRealm.getListImageByCustomerId(id);

                emitter.onNext(customerModel);
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> saveTotalRotaionWithBrand(final List<TotalRotationBrandModel> rotationBrandModelList) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.saveTotalRotaionWithBrand(rotationBrandModelList);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<CustomerModel> getInfoCustomerById(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<CustomerModel>() {
            @Override
            public void subscribe(ObservableEmitter<CustomerModel> emitter) throws Exception {

                CustomerModel customerModel = databaseRealm.getInfoCustomerById(customerId);

                emitter.onNext(customerModel);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<TotalRotationBrandModel>> getListTotalRotationBrand(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<List<TotalRotationBrandModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TotalRotationBrandModel>> emitter) throws Exception {
                List<TotalRotationBrandModel> list = databaseRealm.getListTotalRotationBrand(customerId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateNumberTurnedAndSaveGift
            (final int id, final CustomerGiftModel model, final List<CurrentGift> currentGiftList, final CurrentBrandModel currentBrandModel, final boolean finish) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.updateNumberTurnedAndSaveGift(id,model,currentGiftList,currentBrandModel,finish);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> finishRotationBrandCustomer(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                databaseRealm.finishRotationBrandCustomer(customerId);

                emitter.onNext("");
                emitter.onComplete();

            }
        });
    }

    @Override
    public Observable<String> saveTotalProductChooseGift(final List<TotalChangeGiftModel> list) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.saveTotalProductChooseGift(list);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<TotalChangeGiftModel>> getListProductChooseGift(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<List<TotalChangeGiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TotalChangeGiftModel>> emitter) throws Exception {
                List<TotalChangeGiftModel> list = databaseRealm.getListProductChooseGift(customerId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<List<CustomerGiftModel>> getGiftByCustomerId(final int customerId) {
        return Observable.create(new ObservableOnSubscribe<List<CustomerGiftModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CustomerGiftModel>> emitter) throws Exception {

                List<CustomerGiftModel> list = databaseRealm.getGiftByCustomerId(customerId);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateStatusCustomerImageById(final int id) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStatusCustomerImageById(id);

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

    @Override
    public Observable<String> updateStatusCustomer() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                databaseRealm.updateStatusCustomer();

                emitter.onNext("");
                emitter.onComplete();

            }
        });

    }

}
