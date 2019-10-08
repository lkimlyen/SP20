package com.demo.compass.data.repository.base.local;

import android.content.Context;

import com.demo.compass.data.helper.Constants;
import com.demo.compass.data.helper.RealmHelper;
import com.demo.compass.data.helper.SharedPreferenceHelper;
import com.demo.compass.data.model.BackgroundDialEntity;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.model.BrandSetEntity;
import com.demo.compass.data.model.CurrentBrandSetEntity;
import com.demo.compass.data.model.CurrentGift;
import com.demo.compass.data.model.GiftEntity;
import com.demo.compass.data.model.GiftMegaEntity;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class DatabaseRealm {
    private Context context;

    public DatabaseRealm() {

    }

    public DatabaseRealm(Context context) {
        this.context = context;
    }

    //khởi tạo database theo thông tin user
    public Realm getRealmInstance() {
        if (!RealmHelper.getInstance().getInitRealm()) {
            String nameDatabase = "SP19_" + SharedPreferenceHelper.getInstance(context).getUserObject().getOutlet().getOutletId()
                    + "_" + SharedPreferenceHelper.getInstance(context).getUserObject().getProjectId() + ".realm";
            RealmConfiguration realmConfigurationMain = new RealmConfiguration.Builder()
                    .name(nameDatabase)
                    .schemaVersion(1)
                    .migration(new MyMigration())
                    .build();
            Realm.setDefaultConfiguration(realmConfigurationMain);
            RealmHelper.getInstance().initRealm(true);
        }
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public void close() {
        getRealmInstance().close();
    }

    public void addNotficationItem(final List<NotificationEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NotificationModel.create(realm, list);
            }
        });
    }

    public List<NotificationModel> getListNoti() {
        Realm realm = getRealmInstance();
        List<NotificationModel> list = NotificationModel.getListNoti(realm);
        return list;
    }

    public <T extends RealmObject> void deleteAllItems(Class<T> clazz) {
        Realm realm = getRealmInstance();
        final RealmResults<T> results = realm.where(clazz).findAll();

// All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    public void deleteItemsAsync(Collection<Integer> ids) {
        Realm realm = getRealmInstance();
        // Create an new array to avoid concurrency problem.
        final Integer[] idsToDelete = new Integer[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Integer id : idsToDelete) {
                    NotificationModel.delete(realm, id);
                }
            }
        });
    }

    public List<Integer> addImage(final List<ImageModel> list) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        List<Integer> list1 = ImageModel.addImageModel(realm, list);
        realm.commitTransaction();
        return list1;
    }

    public void addProductModel(final ProductEntity productEntity, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductModel.addProduct(realm, productEntity, path);
            }
        });
    }

    public void deleteProduct() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductModel.deleteProduct(realm);
            }
        });
    }

    public LinkedHashMap<ProductModel, StockModel> getListStockByDate(int outletId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<ProductModel, StockModel> list = StockModel.getStockByDate(realm, outletId);
        return list;
    }

    public void saveListStock(final List<StockModel> list, final int outletId, final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                StockModel.addStock(realm, list, outletId, type);
            }
        });
    }

    public int countNumberWaitingUpload(int teamId, int outletId) {
        int count = 0;
        Realm realm = getRealmInstance();
        final RealmResults<StockModel> stockModels = realm.where(StockModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(stockModels).size();
        final NoteStockModel noteStockModel = NoteStockModel.getNoteWaitingUpload(realm, outletId);
        if (noteStockModel != null) {
            count += 1;
        }

        final RealmResults<TakeOffVolumnModel> takeOffVolumnModels = realm.where(TakeOffVolumnModel.class).equalTo("CreatedBy", teamId).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(takeOffVolumnModels).size();

        final RealmResults<CustomerModel> customerModels = realm.where(CustomerModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(customerModels).size();

        final RealmResults<CurrentGiftModel> currentGiftModels = realm.where(CurrentGiftModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(currentGiftModels).size();

        final RealmResults<CurrentBrandModel> currentBrandModels = realm.where(CurrentBrandModel.class).equalTo("OutletID", outletId)
                .greaterThan("CreatedBy", 0).isNotNull("DateTimeCreate").equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(currentBrandModels).size();
        final RealmResults<POSMReportModel> posmReportModels = realm.where(POSMReportModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        count += realm.copyFromRealm(posmReportModels).size();

        count += TotalRotationModel.getListTotalRotationWaitingUpload(realm).size();

        CustomerGiftMegaModel customerGiftMegaModel = CustomerGiftMegaModel.getCustomerGiftMegaWaitingUpload(realm);
        if (customerGiftMegaModel != null) {
            count += 1;
        }
        return count;
    }

    public List<StockModel> getListStockWaitingUpload(int outletId) {
        Realm realm = getRealmInstance();
        List<StockModel> list = StockModel.getListStockWaitingUpload(realm, outletId);
        return list;
    }

    private int size = 0;

    public int updateStatusStock(final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = StockModel.updateStatusStock(realm, outletId);
            }
        });
        return size;
    }

    public void updateImageAttendance(final int attendaceId, final int imageId, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AttendanceModel.addImageToAtten(realm, attendaceId, imageId, path);
            }
        });
    }

    public void addAttendanceModel(final AttendanceModel model) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AttendanceModel.addAttendance(realm, model);
            }
        });
    }

    public LinkedHashMap<ProductModel, TakeOffVolumnModel> getListTakeOffVolumnByDate(int teamId, int outletId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<ProductModel, TakeOffVolumnModel> list = TakeOffVolumnModel.getTakeOffVolumnByDate(realm, teamId, outletId);
        return list;
    }

    public void saveListTakeOffVolumn(final List<TakeOffVolumnModel> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TakeOffVolumnModel.addTakeOffVolumn(realm, list);
            }
        });
    }

    public List<TakeOffVolumnModel> getListTakeOffWaitingUpload(int teamOutletId, int outletId) {
        Realm realm = getRealmInstance();
        List<TakeOffVolumnModel> list = TakeOffVolumnModel.getListTakeOffVolumnWaitingUpload(realm, teamOutletId, outletId);
        return list;
    }

    public LinkedHashMap<String, String> getListWaitingUpload(int teamId, int outletId) {
        Realm realm = getRealmInstance();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        List<StockModel> listStock = StockModel.getListStockWaitingUpload(realm, outletId);
        if (listStock.size() > 0) {
            result.put(StockModel.class.getName(), gson.toJson(listStock));
        }
        NoteStockModel noteStockModel = NoteStockModel.getNoteWaitingUpload(realm, outletId);
        if (noteStockModel != null) {
            result.put(NoteStockModel.class.getName(), gson.toJson(noteStockModel));
        }
        List<TakeOffVolumnModel> listTakeOffVolumn = TakeOffVolumnModel.getListTakeOffVolumnWaitingUpload(realm, teamId, outletId);
        if (listTakeOffVolumn.size() > 0) {
            result.put(TakeOffVolumnModel.class.getName(), gson.toJson(listTakeOffVolumn));
        }

        List<CustomerProductModel> listCustomerProduct = CustomerProductModel.getListCustomerProductWaitingUpload(realm, outletId);
        if (listCustomerProduct.size() > 0) {
            result.put(CustomerProductModel.class.getName(), gson.toJson(listCustomerProduct));
        }

        List<CustomerGiftModel> listCustomerGift = CustomerGiftModel.getListCustomerGiftWaitingUpload(realm, outletId);
        if (listCustomerGift.size() > 0) {
            result.put(CustomerGiftModel.class.getName(), gson.toJson(listCustomerGift));
        }

        List<CustomerImageModel> listCustomerImage = CustomerImageModel.getListCustomerImageWaitingUpload(realm, outletId);
        if (listCustomerImage.size() > 0) {
            result.put(CustomerImageModel.class.getName(), gson.toJson(listCustomerImage));
        }
        List<CurrentGiftModel> listCurrentGift = CurrentGiftModel.getListRequestWaitingUpload(realm, outletId);
        if (listCurrentGift.size() > 0) {
            result.put(CurrentGiftModel.class.getName(), gson.toJson(listCurrentGift));
        }

        List<CurrentBrandModel> listCurrentBrand = CurrentBrandModel.getListCurrentBrandWaitingUpload(realm, outletId);
        if (listCurrentBrand.size() > 0) {
            result.put(CurrentBrandModel.class.getName(), gson.toJson(listCurrentBrand));
        }

        List<POSMReportModel> listPOSMReport = POSMReportModel.getListPOSMReportWaitingUpload(realm, outletId);
        if (listPOSMReport.size() > 0) {
            result.put(POSMReportModel.class.getName(), gson.toJson(listPOSMReport));
        }

        List<TotalRotationModel> totalRotationModels = TotalRotationModel.getListTotalRotationWaitingUpload(realm);
        if (totalRotationModels.size() > 0) {
            result.put(TotalRotationModel.class.getName(), gson.toJson(totalRotationModels));
        }


        CustomerGiftMegaModel customerGiftMegaModel = CustomerGiftMegaModel.getCustomerGiftMegaWaitingUpload(realm);
        if (customerGiftMegaModel != null) {
            result.put(CustomerGiftMegaModel.class.getName(), gson.toJson(customerGiftMegaModel));
        }
        return result;
    }

    public int updateStatusTakeOffVolumn(final int teamId, final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = TakeOffVolumnModel.updateStatusTakeOffVolumn(realm, teamId, outletId);
            }
        });
        return size;
    }

    public void addBrandSetDetail(final List<BrandSetDetailEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BrandSetDetailModel.addBrandSetDetail(realm, list);
            }
        });
    }

    public void addBrand(final List<BrandEntitiy> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BrandModel.addBrand(realm, list);
            }
        });
    }

    public void addGiftModel(final GiftEntity giftEntity, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GiftModel.addGift(realm, giftEntity, path);
            }
        });
    }

    public void addBrandSet(final List<BrandSetEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BrandSetModel.addBrandSet(realm, list);
            }
        });
    }

    public void deleteGift() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GiftModel.deleteGift(realm);
            }
        });
    }

    public List<ProductModel> getListProductByBrandId(List<Integer> idList) {
        Realm realm = getRealmInstance();
        List<ProductModel> list = ProductModel.getListProductByBrandId(realm, idList);
        return list;
    }

    public List<BrandModel> getListBrandById(List<Integer> idList) {
        Realm realm = getRealmInstance();
        List<BrandModel> list = BrandModel.getListBrandById(realm, idList);
        return list;
    }

    public void addBackgroundDial(final BackgroundDialEntity backgroundDialEntity, final String pathArrow, final String pathButton, final String pathLayout, final String pathCircle) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BackgroundDialModel.addBackgroundDial(realm, backgroundDialEntity, pathArrow, pathButton, pathLayout, pathCircle);
            }
        });
    }

    public void deleteAllBackgroundDial() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BackgroundDialModel.deleteBackgroundDial(realm);
            }
        });
    }

    public List<GiftModel> getListGiftByBrandIdAndSetId(int outletId, int brandId) {

        Realm realm = getRealmInstance();
        List<GiftModel> list = GiftModel.getListGiftByBrandIdAndSetId(realm, outletId, brandId);
        return list;
    }

    public BackgroundDialModel getBackgroundDialByBrandId(int brandId) {
        Realm realm = getRealmInstance();
        BackgroundDialModel list = BackgroundDialModel.getBackgroundDialByBrandId(realm, brandId);
        return list;
    }

    public List<CurrentGiftModel> getListCurrentGift(int outletId, int brandId) {
        Realm realm = getRealmInstance();
        List<CurrentGiftModel> list = CurrentGiftModel.getListCurrentGift(realm, outletId, brandId);
        return list;
    }

    public Integer addCustomerModel(final CustomerModel customerModel) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        int id = CustomerModel.addCustomer(realm, customerModel);
        realm.commitTransaction();
        return id;
    }

    public void addCustomerProductModel(final List<CustomerProductModel> productModelList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerProductModel.addCustomerProduct(realm, productModelList);
            }
        });
    }

    public void addCustomerImageModel(final List<CustomerImageModel> customerImageModelList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerImageModel.addCustomerImage(realm, customerImageModelList);
            }
        });
    }

    public void addCustomerGiftModel(final List<CustomerGiftModel> giftModelList, final int customerId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerGiftModel.addCustomerGift(realm, giftModelList, customerId);
            }
        });
    }

    public LinkedHashMap<CustomerModel, List<ImageModel>> getListCustomerWaitingUpload(int OutletId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, List<ImageModel>> list = CustomerModel.getListCustomerAndImage(realm, OutletId);
        return list;
    }

    public void addCustomerServerId(final int id, final int serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerModel.addCustomerServerId(realm, id, serverId);
            }
        });
    }

    public void addCustomerImageServerId(final int cusId, final int imageId, final int serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerModel.addCustomerImageServerId(realm, cusId, imageId, serverId);
            }
        });
    }

    public void updateStatusCustomerProduct(final int outletId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerProductModel.updateStatusCustomerProduct(realm, outletId);
            }
        });
    }

    public void updateStatusCustomerGift(final int outletId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerGiftModel.updateStatusCustomerGift(realm, outletId);
            }
        });
    }

    public int updateStatusCustomerImage(final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = CustomerImageModel.updateStatusCustomerImage(realm, outletId);
            }
        });
        return size;
    }

    public BrandModel getBrandCurrent(int brandId) {
        Realm realm = getRealmInstance();
        BrandModel brandModel = BrandModel.getBrandCurrent(realm, brandId);
        return brandModel;
    }

    public LinkedHashMap<GiftModel, DetailCurrentGiftModel> getListGiftChangeByDate(int outletID, List<Integer> brandIdList) {
        Realm realm = getRealmInstance();
        LinkedHashMap<GiftModel, DetailCurrentGiftModel> list = DetailCurrentGiftModel.getListGiftChangeByDate(realm, outletID, brandIdList);
        return list;
    }

    public LinkedHashMap<CustomerModel, List<CustomerGiftModel>> getListCustomerGiftByDate(String date) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = CustomerModel.getListCustomerGiftByDate(realm, date);
        return list;
    }

    public void addCurrentSetBrand(final List<CurrentBrandSetEntity> list, final int outletId, final int userId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CurrentGiftModel.addListCurrentGift(realm, list, outletId, userId);
            }
        });
    }

    public void addProductGift(final List<ProductGiftEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductGiftModel.addProductGift(realm, list);
            }
        });
    }

    public LinkedHashMap<BrandModel, List<ProductGiftModel>> getListGiftByProduct(List<Integer> idProductList) {
        Realm realm = getRealmInstance();
        LinkedHashMap<BrandModel, List<ProductGiftModel>> list = ProductGiftModel.getListGiftByProduct(realm, idProductList);
        return list;
    }

    public LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>> getListBrandSetDetailCurrent(int outletId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>> list = CurrentBrandModel.getListBrandSetDetailCurrent(realm, outletId);
        return list;
    }

    public LinkedHashMap<CustomerModel, List<CustomerGiftModel>> checkOrderCode(String orderCode) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> listLinkedHashMap = CustomerModel.checkOrderCode(realm, orderCode);
        return listLinkedHashMap;
    }

    public LinkedHashMap<CustomerModel, List<CustomerGiftModel>> checkCustomerPhone(String phone, List<Integer> idListBrand) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> listLinkedHashMap = CustomerModel.checkCustomerPhone(realm, phone,idListBrand);
        return listLinkedHashMap;
    }

    public void saveRequestGift(final RequestGiftModel requestGiftModel, final List<DetailRequestGiftModel> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RequestGiftModel.saveRequestGift(realm, requestGiftModel, list);
            }
        });
    }

    public void updateCurrentGiftModel(final List<CurrentGiftModel> currentGiftModelList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //CurrentGiftModel.addOrUpdateCurrentGift(realm, currentGiftModelList);
            }
        });
    }

    public RealmResults<RequestGiftModel> getListConfirmReciever(int outletID) {
        Realm realm = getRealmInstance();
        RealmResults<RequestGiftModel> list = RequestGiftModel.getListConfirmReciever(realm, outletID);
        return list;
    }

    public void updateStateRequest(final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RequestGiftModel.updateStateRequest(realm, id);
            }
        });
    }

    public List<CurrentBrandModel> getListBrandSetCurrent(int outletId) {
        Realm realm = getRealmInstance();
        List<CurrentBrandModel> list = CurrentBrandModel.getListBrandSetCurrent(realm, outletId);
        return list;
    }

    public List<BrandSetModel> getListBrandSetByBrandId(int brandId, int outletType) {
        Realm realm = getRealmInstance();
        List<BrandSetModel> list = BrandSetModel.getListBrandSetByBrandId(realm, brandId, outletType);
        return list;
    }

    public void updateBrandSetCurrent(final int outletId, final int brandsetId, final int numberUsed) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CurrentBrandModel.updateBrandSetCurrent(realm, outletId, brandsetId);
            }
        });

    }

    public void addReason(final List<ReasonEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ReasonModel.addReason(realm, list);
            }
        });

    }

    public List<ReasonModel> getListReason() {
        Realm realm = getRealmInstance();
        List<ReasonModel> listReason = ReasonModel.getListReason(realm);
        return listReason;
    }

    public CurrentBrandModel getListCurrentBrandSet(int outletId, int brandId) {
        Realm realm = getRealmInstance();
        CurrentBrandModel currentBrandModel = CurrentBrandModel.getListCurrentBrandSet(realm, outletId, brandId);
        return currentBrandModel;
    }

    public void updateNumberCurrentBrand(final List<CurrentBrandModel> currentBrandModels) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //  CurrentBrandModel.updateNumberCurrentBrand(realm, currentBrandModels);
            }
        });


    }

    public void addRotationToBackground(final int brandId, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BackgroundDialModel.addRotation(realm, brandId, path);
            }
        });

    }

    public void addEmergency(final EmergencyModel emergencyModel) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EmergencyModel.addEmergency(realm, emergencyModel);
            }
        });
    }

    public RealmResults<EmergencyModel> getEmergencyNotFinished(int userId) {
        Realm realm = getRealmInstance();
        RealmResults<EmergencyModel> list = EmergencyModel.getEmergencyNotEnd(realm, userId);
        return list;
    }

    public void updateStateEmergency(final int emergencyId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EmergencyModel.updateStateEmergency(realm, emergencyId);
            }
        });
    }

    public boolean checkConditionChangeBrand(int brandId, int brandSetIdSelect) {
        Realm realm = getRealmInstance();
        boolean b = CurrentGiftModel.checkConditionChangeBrand(realm, brandId, brandSetIdSelect);
        return b;
    }

    public Integer updateStatusCurrentGift(final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = CurrentGiftModel.updateStatusCurrentGift(realm, outletId);
            }
        });
        return size;
    }

    public Integer updateStatusCurrentBrand(final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = CurrentBrandModel.updateStatusCurrentBrand(realm, outletId);
            }
        });
        return size;
    }

    public void deletePOSM() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                POSMModel.deletePOSM(realm);
            }
        });
    }

    public void addPOSMModel(final POSMEntity posmEntity, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                POSMModel.addPOSM(realm, posmEntity, path);
            }
        });
    }

    public LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> getListPOSM(int outletId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> list = POSMReportModel.getPOSMByDate(realm, outletId);
        return list;
    }

    public void saveListPOSMReport(final List<POSMReportModel> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                POSMReportModel.addPOSMReport(realm, list);
            }
        });
    }

    public Integer updateStatusPOSM(final int outletId) {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = POSMReportModel.updateStatusPOSMReport(realm, outletId);
            }
        });
        return size;
    }

    public LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> getListBrandSetById() {
        Realm realm = getRealmInstance();
        LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> listBrandSetById = BrandSetModel.getListBrandSetById(realm);
        return listBrandSetById;
    }

    public void clearImage() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AttendanceModel> attendanceModels = realm.where(AttendanceModel.class).findAll();
                for (AttendanceModel attendanceModel : attendanceModels) {
                    String[] paths = attendanceModel.getPathImage().split(",");
                    for (String s : paths) {
                        File file = new File(s);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                RealmResults<CustomerImageModel> customerImageModels = realm.where(CustomerImageModel.class).equalTo("Status", Constants.UPLOADED).findAll();

                for (CustomerImageModel customerImageModel : customerImageModels) {
                    ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", customerImageModel.getImageID()).findFirst();
                    if (imageModel != null) {
                        File file = new File(imageModel.getPath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }


                }

            }
        });
    }

    public NoteStockModel getNoteStock(int outletId) {
        Realm realm = getRealmInstance();
        NoteStockModel noteStockModel = NoteStockModel.getNoteStockByDate(realm, outletId);
        return noteStockModel;
    }

    public void saveNoteStock(final int outletId, final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NoteStockModel.addNote(realm, outletId, type);
            }
        });
    }

    public void updateStatusNoteStock(final int outletId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NoteStockModel.updateStatusNote(realm, outletId);
            }
        });
    }

    public CustomerModel getInfoCusByPhone(String phone) {
        Realm realm = getRealmInstance();
        CustomerModel customerModel = CustomerModel.getInfoCusByPhone(realm, phone);
        return customerModel;
    }

    public List<BrandSetDetailModel> getListBrandSetDetail(int brandId) {
        Realm realm = getRealmInstance();
        List<BrandSetDetailModel> brandSetDetail = BrandSetDetailModel.getListBrandSetDetail(realm, brandId);
        return brandSetDetail;
    }

    public List<GiftModel> getAllGift() {
        Realm realm = getRealmInstance();
        RealmResults<GiftModel> results = realm.where(GiftModel.class).findAll();
        return realm.copyFromRealm(results);
    }

    public LinkedHashMap<BrandModel, Integer> getListNumberGiftWithBrand(String phone, List<Integer> idListBrand) {
        Realm realm = getRealmInstance();
        LinkedHashMap<BrandModel, Integer> listBrandSetDetail = CustomerModel.getListNumberGiftWithBrand(realm, phone,idListBrand);
        return listBrandSetDetail;
    }

    public void deleteGiftMega() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GiftMegaModel.deleteGiftMega(realm);
            }
        });
    }

    public List<GiftMegaModel> addGiftMegaAndTime(final List<GiftMegaEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GiftMegaModel.addGift(realm, list);
            }
        });

        RealmResults<GiftMegaModel> results = realm.where(GiftMegaModel.class).findAll();
        return realm.copyFromRealm(results);
    }

    public void updateFilePathGiftMega(final int id, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GiftMegaModel.updateFilePathGiftMega(realm, id, path);
            }
        });
    }

    public TimeRotationModel getListGiftMegaByDate() {
        Realm realm = getRealmInstance();
        TimeRotationModel timeRotationModel = GiftMegaModel.getLListGiftMegaByDate(realm);
        return timeRotationModel;
    }

    public void addImageRotaion(final int id, final String path) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TimeRotationModel.addImageRotation(realm, id, path);
            }
        });
    }

    public LinkedHashMap<CustomerModel, TotalRotationModel> getCustomerById(int customerId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, TotalRotationModel> customerModel = CustomerModel.getCustomerById(realm, customerId);
        return customerModel;
    }

    public void saveTotalRotaion(final int cusId, final int totalRotation, final int teamOutletId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalRotationModel.addTotalRotaion(realm, cusId, totalRotation, teamOutletId);
            }
        });
    }

    public Integer updateStatusTotalRotation() {
        Realm realm = getRealmInstance();
        size = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                size = TotalRotationModel.updateStatusRotation(realm);
            }
        });
        return size;
    }

    public void saveInfoCusLucky(final CustomerGiftMegaModel customerGiftMegaModel) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerGiftMegaModel.addCustomerGift(realm, customerGiftMegaModel);
            }
        });
    }

    public LinkedHashMap<CustomerModel, List<ImageModel>> getCusLuckyUploadServer(int cusId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<CustomerModel, List<ImageModel>> list = CustomerModel.getCusLuckyUploadServer(realm, cusId);
        return list;
    }

    public List<CustomerProductModel> getListProductByCustomer(int cusId) {
        Realm realm = getRealmInstance();
        List<CustomerProductModel> list = CustomerProductModel.getListProductByCustomer(realm, cusId);
        return list;
    }

    public List<CustomerImageModel> getListImageByCustomer(int cusId) {
        Realm realm = getRealmInstance();
        List<CustomerImageModel> list = CustomerImageModel.getListImageByCustomer(realm, cusId);
        return list;
    }

    public CustomerGiftMegaModel getCustomerGiftMega(int cusId) {
        Realm realm = getRealmInstance();
        CustomerGiftMegaModel customer = CustomerGiftMegaModel.getCustomerGiftMega(realm, cusId);
        return customer;
    }

    public void updateStatusCustomerGiftMega() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerGiftMegaModel.updateStatusCustomerGiftMega(realm);
            }
        });
    }

    public Boolean checkUserCheckIn(int teamOutletId) {
        Realm realm = getRealmInstance();
        boolean b = AttendanceModel.checkUserCheckIn(realm, teamOutletId);
        return b;
    }

    public void saveTotalRotaionWithServerId(final int cusId, final int cusIDServer, final int totalRotation, final int teamOutletId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalRotationModel.saveTotalRotaionWithServerId(realm, cusId, cusIDServer, totalRotation, teamOutletId);
            }
        });
    }

    public void updateNumberTunedRotation(final int customerId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalRotationModel.updateNumberTunedRotation(realm, customerId);
            }
        });
    }

    public CustomerModel getInfoCusNotFinished(final String orderCode, final String phone, final int createdBy, boolean mega, boolean SP) {
        Realm realm = getRealmInstance();
        CustomerModel customerModel = CustomerModel.getInfoCusNotFinished(realm, orderCode, phone, createdBy, mega, SP);
        return customerModel;

    }

    public List<CustomerProductModel> getListProductByCustomerId(int id) {
        Realm realm = getRealmInstance();
        List<CustomerProductModel> customerModel = CustomerModel.getListProductByCustomerId(realm, id);
        return customerModel;

    }

    public List<ImageModel> getListImageByCustomerId(int id) {
        Realm realm = getRealmInstance();
        List<ImageModel> customerModel = CustomerModel.getListImageByCustomerId(realm, id);
        return customerModel;
    }

    public void saveTotalRotaionWithBrand(final List<TotalRotationBrandModel> rotationBrandModelList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalRotationBrandModel.create(realm, rotationBrandModelList);
            }
        });
    }

    public CustomerModel getInfoCustomerById(int customerId) {
        Realm realm = getRealmInstance();
        CustomerModel customerModel = CustomerModel.getInfoCustomerById(realm, customerId);
        return customerModel;
    }


    public List<TotalRotationBrandModel> getListTotalRotationBrand(int customerId) {
        Realm realm = getRealmInstance();
        List<TotalRotationBrandModel> list = CustomerModel.getListTotalRotationBrand(realm, customerId);
        return list;
    }

    public void updateNumberTurnedAndSaveGift(final int id, final CustomerGiftModel model, final List<CurrentGift> currentGiftList,
                                              final CurrentBrandModel currentBrandModel, final boolean finish) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalRotationBrandModel.updateNumberTurnedAndSaveGift(realm, id, model, currentGiftList, currentBrandModel, finish);
            }
        });
    }

    public void finishRotationBrandCustomer(final int customerId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerModel.finishRotationBrandCustomer(realm, customerId);
            }
        });
    }

    public void saveTotalProductChooseGift(final List<TotalChangeGiftModel> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TotalChangeGiftModel.create(realm, list);
            }
        });
    }

    public List<TotalChangeGiftModel> getListProductChooseGift(final int customerId) {
        Realm realm = getRealmInstance();

        List<TotalChangeGiftModel> list = TotalChangeGiftModel.getListProductChooseGift(realm, customerId);
        return list;
    }

    public List<CustomerGiftModel> getGiftByCustomerId(int customerId) {
        Realm realm = getRealmInstance();

        List<CustomerGiftModel> list = CustomerGiftModel.getGiftByCustomerId(realm, customerId);
        return list;
    }

    public void updateStatusCustomerImageById(final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerModel.updateStatusCustomerImageById(realm, id);
            }
        });
    }

    public void updateStatusCustomer() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomerModel.updateStatusCustomer(realm);
            }
        });
    }

    public class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();


            // Migrate to version 2: Add a primary key + object references
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     @PrimaryKey
            //     private int age;
            //     private Dog favoriteDog;
            //     private RealmList<Dog> dogs;
            //     // getters and setters left out for brevity
            // }


        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof MyMigration; // obj instance of your Migration class name, here My class is Migration.
        }

        @Override
        public int hashCode() {
            return MyMigration.class.hashCode();
        }
    }
}
