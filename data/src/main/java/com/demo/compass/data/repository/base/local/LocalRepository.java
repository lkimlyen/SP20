package com.demo.compass.data.repository.base.local;

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

import java.util.LinkedHashMap;
import java.util.List;

import io.realm.RealmResults;

import io.reactivex.Observable;
public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<String> addNotification(final List<NotificationEntity> list);

    Observable<List<NotificationModel>> findAllNoti();

    Observable<NotificationModel> deleteAllNotification();

    Observable<String> addAttendanceModel(AttendanceModel model);

    Observable<List<Integer>> addImageModel(List<ImageModel> list);

    Observable<String> addProductModel(ProductEntity productEntity, String path);

    Observable<String> deleteProduct();

    Observable<LinkedHashMap<ProductModel, StockModel>> getListStockByDate(int outletId);

    Observable<String> saveListStock(List<StockModel> list, int outletId, int type);

    Observable<Integer> countNumberWaitingUpload(int teamId, int outletId);

    Observable<List<StockModel>> getListStockWaitingUpload(int outletId);

    Observable<Integer> updateStatusStock(int outletId);

    Observable<String> updateImageAttendance(int attendaceId, int imageId, String path);

    Observable<LinkedHashMap<ProductModel, TakeOffVolumnModel>> getListTakeOffVolumnByDate(final int teamId, int outletId);

    Observable<String> saveListTakeOffVolumn(List<TakeOffVolumnModel> list);

    Observable<List<TakeOffVolumnModel>> getListTakeOffWaitingUpload(int teamOutletId, int outletId);

    Observable<LinkedHashMap<String, String>> getListWaitingUpload(int teamId, int outletId);

    Observable<Integer> updateStatusTakeOffVolumn(int teamId, int outletId);

    Observable<String> addBrandSetDetail(List<BrandSetDetailEntity> list);

    Observable<String> addBrand(List<BrandEntitiy> list);

    Observable<String> addGiftModel(GiftEntity giftEntity, String path);

    Observable<String> addBrandSet(List<BrandSetEntity> list);

    Observable<String> deleteGift();

    Observable<List<ProductModel>> getListProductByBrandId(List<Integer> idList);

    Observable<List<BrandModel>> getListBrandById(List<Integer> idList);

    Observable<String> addBackgroundDial(BackgroundDialEntity backgroundDialEntity, String path, String path1, String path2, String path3);

    Observable<String> deleteAllBackgroundDial();

    Observable<List<GiftModel>> getListGiftByBrandIdAndSetId(int outletId, int brandId);

    Observable<BackgroundDialModel> getBackgroundDialByBrandId(int brandId);

    Observable<List<CurrentGiftModel>> getListCurrentGift(int outletId, int brandId);

    Observable<Integer> addCustomerModel(CustomerModel customerModel);

    Observable<String> addCustomerProductModel(List<CustomerProductModel> productModelList);

    Observable<String> addCustomerImageModel(List<CustomerImageModel> customerImageModelList);

    Observable<String> addCustomerGiftModel(List<CustomerGiftModel> giftModelList, int outletId);

    Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getListCustomerWaitingUpload(int teamOutletId);

    Observable<String> addCustomerServerId(int id, int serverId);

    Observable<String> addCustomerImageServerId(int id, int id1, int id2);

    Observable<String> updateStatusCustomerProduct(int outletId);

    Observable<String> updateStatusCustomerGift(int outletId);

    Observable<Integer> updateStatusCustomerImage(int outletId);

    Observable<BrandModel> getBrandCurrent(int brandId);

    Observable<LinkedHashMap<GiftModel, DetailCurrentGiftModel>> getListGiftChangeByDate(int outletID, List<Integer> brandIdList);

    Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> getListCustomerGiftByDate(String date);

    Observable<String> addCurrentSetBrand(List<CurrentBrandSetEntity> list, final int outletId, final int userId);

    Observable<String> addProductGift(List<ProductGiftEntity> list);

    Observable<LinkedHashMap<BrandModel, List<ProductGiftModel>>> getListGiftByProduct(List<Integer> idProductList);

    Observable<LinkedHashMap<CurrentBrandModel, List<BrandSetDetailModel>>> getListBrandSetDetailCurrent(int outletId);

    Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkOrderCode(String customerCode);

    Observable<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>> checkCustomerPhone(String phone, List<Integer> idListBrand);

    Observable<String> saveRequestGift(RequestGiftModel requestGiftModel, List<DetailRequestGiftModel> list);

    Observable<String> updateCurrentGiftModel(List<CurrentGiftModel> currentGiftModelList);

    Observable<RealmResults<RequestGiftModel>> getListConfirmReciever(int outletID);

    Observable<String> updateStateRequest(int id);

    Observable<List<CurrentBrandModel>> getListBrandSetCurrent(int outletId);

    Observable<List<BrandSetModel>> getListBrandSetByBrandId(int brandId, int outletType);

    Observable<String> updateBrandSetCurrent(int outletId, int brandsetId, int numberUsed);

    Observable<String> addReason(List<ReasonEntity> list);

    Observable<List<ReasonModel>> getListReason();

    Observable<CurrentBrandModel> getListCurrentBrandSet(int outletId, int brandId);

    Observable<String> updateNumberCurrentBrand(List<CurrentBrandModel> currentBrandModels);

    Observable<String> addRotationToBackground(int brandId, String path);

    Observable<String> addEmergency(EmergencyModel emergencyModel);

    Observable<RealmResults<EmergencyModel>> getEmergencyNotFinished(int userId);

    Observable<String> updateStateEmergency(int emergencyId);

    Observable<Boolean> checkConditionChangeBrand(int brandId, int brandSetIdSelect);

    Observable<Integer> updateStatusCurrentGift(int outletId);

    Observable<Integer> updateStatusCurrentBrand(int outletId);

    Observable<String> deletePOSM();

    Observable<String> addPOSMModel(POSMEntity posmEntity, String path);

    Observable<LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>>> getListPOSM(int outletId);

    Observable<String> saveListPOSMReport(List<POSMReportModel> list);

    Observable<Integer> updateStatusPOSM(int outletId);

    Observable<LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>>> getListBrandSetById();

    Observable<String> clearImage();

    Observable<NoteStockModel> getNoteStock(int outletId);

    Observable<String> saveNoteStock(int outletId, int type);

    Observable<String> updateStatusNoteStock(int outletId);

    Observable<CustomerModel> getInfoCusByPhone(String phone);

    Observable<List<BrandSetDetailModel>> getListBrandSetDetail(int brandId);

    Observable<List<GiftModel>> getAllGift();

    Observable<LinkedHashMap<BrandModel, Integer>> getListNumberGiftWithBrand(String phone, final List<Integer> idListBrand);

    Observable<String> deleteGiftMega();


    Observable<List<GiftMegaModel>> addGiftMegaAndTime(List<GiftMegaEntity> list);

    Observable<String> updateFilePathGiftMega(int id, String path);

    Observable<TimeRotationModel> getListGiftMegaByDate();

    Observable<String> addImageRotaion(int id, String path);

    Observable<LinkedHashMap<CustomerModel, TotalRotationModel>> getCustomerById(int customerId);

    Observable<String> saveTotalRotaion(int cusId, int totalRotation, int teamOutletId);

    Observable<Integer> updateStatusTotalRotation();

    Observable<String> saveInfoCusLucky(CustomerGiftMegaModel customerGiftMegaModel);

    Observable<LinkedHashMap<CustomerModel, List<ImageModel>>> getCusLuckyUploadServer(int cusId);

    Observable<List<CustomerProductModel>> getListProductByCustomer(int id);

    Observable<List<CustomerImageModel>> getListImageByCustomer(int id);

    Observable<CustomerGiftMegaModel> getCustomerGiftMega(int id);

    Observable<String> uploadStatusCustomerGiftMega();

    Observable<Boolean> checkUserCheckIn(int teamOutletId);

    Observable<String> saveTotalRotaionWithServerId(int cusId, int cusIDServer, int totalRotation, int teamOutletId);

    Observable<String> updateNumberTunedRotation(int customerId);

    Observable<CustomerModel> getInfoCusNotFinished(String orderCode, String phone, int createdBy, boolean mega, boolean SP);

    Observable<List<CustomerProductModel>> getListProductByCustomerId(int id);

    Observable<List<ImageModel>> getListImageByCustomerId(int id);

    Observable<String> saveTotalRotaionWithBrand(List<TotalRotationBrandModel> rotationBrandModelList);

    Observable<CustomerModel> getInfoCustomerById(int customerId);

    Observable<List<TotalRotationBrandModel>> getListTotalRotationBrand(int customerId);

    Observable<String> updateNumberTurnedAndSaveGift(int id, CustomerGiftModel model, List<CurrentGift>
            currentGiftList, CurrentBrandModel currentBrandModel, boolean finish);

    Observable<String> finishRotationBrandCustomer(int customerId);

    Observable<String> saveTotalProductChooseGift(List<TotalChangeGiftModel> list);

    Observable<List<TotalChangeGiftModel>> getListProductChooseGift(int customerId);

    Observable<List<CustomerGiftModel>> getGiftByCustomerId(int customerId);

    Observable<String> updateStatusCustomerImageById(int id);

    Observable<String> updateStatusCustomer();
}
