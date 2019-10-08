package com.demo.compass.data.model.offline;

import com.demo.compass.data.helper.Constants;
import com.felix.kotline.utils.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CustomerModel extends RealmObject implements Serializable {
    @PrimaryKey
    private int Id;

    @SerializedName("pOutletID")
    @Expose
    private int OutletID;

    @SerializedName("pCustomerName")
    @Expose
    private String CustomerName;

    @SerializedName("pCustomerCode")
    @Expose
    private String CustomerCode;

    @SerializedName("pCustomerPhone")
    @Expose
    private String CustomerPhone;

    @SerializedName("pBillNumber")
    @Expose
    private String BillNumber;

    @SerializedName("pAddress")
    @Expose
    private String Address;

    @SerializedName("pNote")
    @Expose
    private String Note;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private boolean Finished;
    private boolean FinishedSP;
    private Date DateCreate;
    @SerializedName("pDeviceDateTime")
    @Expose
    private String CreatedDateTime;

    private int serverId;
    private int Status;

    public CustomerModel() {
    }

    public CustomerModel(int outletID, String customerName, String customerCode, String customerPhone, String billNumber, String address, String note, int createdBy) {
        OutletID = outletID;
        CustomerName = customerName;
        CustomerCode = customerCode;
        CustomerPhone = customerPhone;
        BillNumber = billNumber;
        Address = address;
        Note = note;
        CreatedBy = createdBy;
    }

    public CustomerModel(int id, int outletID, String customerName, String customerCode, String customerPhone,
                         String billNumber, String address, String note, int createdBy, boolean finished, boolean finishedSP,
                         Date dateCreate, String createdDateTime, int serverId, int status) {
        Id = id;
        OutletID = outletID;
        CustomerName = customerName;
        CustomerCode = customerCode;
        CustomerPhone = customerPhone;
        BillNumber = billNumber;
        Address = address;
        Note = note;
        CreatedBy = createdBy;
        Finished = finished;
        FinishedSP = finished;
        DateCreate = dateCreate;
        CreatedDateTime = createdDateTime;
        this.serverId = serverId;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CustomerModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static Integer addCustomer(Realm realm, CustomerModel customerModel) {
        CustomerModel customer = new CustomerModel(id(realm) + 1, customerModel.OutletID,
                customerModel.CustomerName, customerModel.CustomerCode, customerModel.CustomerPhone,
                customerModel.BillNumber, customerModel.Address, customerModel.Note, customerModel.CreatedBy,
                false, false, ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()), ConvertUtils.getDateTimeCurrent(), -1, Constants.WAITING_UPLOAD);
        int id = realm.copyToRealm(customer).getId();
        return id;
    }

    public static LinkedHashMap<CustomerModel, List<ImageModel>> getListCustomerAndImage(Realm realm, int OutletId) {
        LinkedHashMap<CustomerModel, List<ImageModel>> list = new LinkedHashMap<>();
        RealmResults<CustomerModel> results = realm.where(CustomerModel.class)
                .equalTo("OutletID", OutletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (CustomerModel customerModel : results) {
            List<ImageModel> imageModelList = new ArrayList<>();
            RealmResults<CustomerImageModel> customerImageList = realm.where(CustomerImageModel.class)
                    .equalTo("CusID", customerModel.Id).equalTo("ImageIDServer", 0).equalTo("Status", Constants.WAITING_UPLOAD).findAll();
            for (CustomerImageModel model : customerImageList) {
                ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", model.getImageID())
                        .findFirst();
                if (imageModel != null) {
                    imageModelList.add(realm.copyFromRealm(imageModel));
                }

            }

            list.put(realm.copyFromRealm(customerModel), imageModelList);

        }
        return list;
    }

    public static void addCustomerServerId(Realm realm, int id, int serverId) {
        CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", id).findFirst();
        customerModel.setServerId(serverId);
        //  customerModel.setStatus(Constants.UPLOADED);

        RealmResults<CustomerProductModel> customerProductModels = realm.where(CustomerProductModel.class).equalTo("CusID", id).findAll();
        for (CustomerProductModel customerProductModel : customerProductModels) {
            customerProductModel.setCusIDServer(serverId);
        }

        RealmResults<CustomerGiftModel> customerGiftModels = realm.where(CustomerGiftModel.class).equalTo("CusId", id).findAll();
        for (CustomerGiftModel customerGiftModel : customerGiftModels) {
            customerGiftModel.setCusIDServer(serverId);
        }
        RealmResults<CustomerImageModel> customerImageModels = realm.where(CustomerImageModel.class).equalTo("CusID", id).findAll();
        for (CustomerImageModel customerImageModel : customerImageModels) {
            customerImageModel.setCusIDServer(serverId);
        }

        TotalRotationModel totalRotationModel = realm.where(TotalRotationModel.class)
                .equalTo("CustomerId", id).findFirst();
        if (totalRotationModel != null) {
            totalRotationModel.setCusServerId(serverId);
        }

        CustomerGiftMegaModel customerGiftMegaModel = realm.where(CustomerGiftMegaModel.class).equalTo("CusId", id).findFirst();
        if (customerGiftMegaModel != null) {
            customerGiftMegaModel.setCusIDServer(serverId);
        }
    }

    public static void addCustomerImageServerId(Realm realm, int cusId, int imageId, int serverId) {
        ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", imageId).findFirst();
        imageModel.setStatus(Constants.UPLOADED);
        CustomerImageModel customerImageModel = realm.where(CustomerImageModel.class).equalTo("CusID", cusId).equalTo("ImageID", imageId).findFirst();
        customerImageModel.setImageIDServer(serverId);
    }

    public int getId() {
        return Id;
    }

    public int getOutletID() {
        return OutletID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public String getBillNumber() {
        return BillNumber;
    }

    public String getAddress() {
        return Address;
    }

    public String getNote() {
        return Note;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public void setBillNumber(String billNumber) {
        BillNumber = billNumber;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setNote(String note) {
        Note = note;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setFinishedSP(boolean finishedSP) {
        FinishedSP = finishedSP;
    }

    public void setFinished(boolean finished) {
        Finished = finished;
    }

    public boolean isFinished() {
        return Finished;
    }

    public boolean isFinishedSP() {
        return FinishedSP;
    }

    public static LinkedHashMap<CustomerModel, List<CustomerGiftModel>> getListCustomerGiftByDate(Realm realm, String date) {
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = new LinkedHashMap<>();
        RealmResults<CustomerModel> results = realm.where(CustomerModel.class).equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(date))
                .findAll();

        for (CustomerModel customerModel : results) {
            RealmResults<CustomerGiftModel> customerGiftModelRealmResults = realm.where(CustomerGiftModel.class).equalTo("CusId", customerModel.Id)
                    .findAll();
            if (list.get(customerModel.getBillNumber()) != null) {
                List<CustomerGiftModel> customerGiftList = list.get(customerModel.getBillNumber());
                customerGiftList.addAll(realm.copyFromRealm(customerGiftModelRealmResults));
                list.put(customerModel, customerGiftList);
            } else {

                list.put(customerModel, realm.copyFromRealm(customerGiftModelRealmResults));
            }
        }

        return list;
    }

    public static LinkedHashMap<CustomerModel, List<CustomerGiftModel>> checkOrderCode(Realm realm, String orderCode) {
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = new LinkedHashMap<>();

        RealmResults<CustomerModel> results = realm.where(CustomerModel.class)
                .equalTo("BillNumber", orderCode)
                .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort())).findAll();
        for (CustomerModel customerModel : results) {
            List<CustomerGiftModel> giftModelList = new ArrayList<>();
            RealmResults<CustomerGiftModel> customerGiftModelRealmResults = realm.where(CustomerGiftModel.class)
                    .equalTo("CusId", customerModel.Id).findAll();
            if (customerGiftModelRealmResults.size() > 0) {
                giftModelList.addAll(realm.copyFromRealm(customerGiftModelRealmResults));
                list.put(customerModel, giftModelList);
            }
        }

        return list;
    }

    public static LinkedHashMap<CustomerModel, List<CustomerGiftModel>> checkCustomerPhone(Realm realm, String phone, List<Integer> idListBrand) {
        LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = new LinkedHashMap<>();
        RealmResults<CustomerModel> results = realm.where(CustomerModel.class)
                .equalTo("CustomerPhone", phone)
                .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort())).findAll();
        RealmResults<CurrentBrandModel> currentBrandModels = realm.where(CurrentBrandModel.class)
                .equalTo("IsUsed", true).findAll();
        for (CustomerModel customerModel : results) {
            List<CustomerGiftModel> giftModelList = new ArrayList<>();
            for (int brandId : idListBrand) {
                BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandId).equalTo("IsDialLucky", false).notEqualTo("NumberGiftOfDay",-1).findFirst();
                if (brandModel != null) {
                    RealmResults<ProductModel> productModels = realm.where(ProductModel.class).equalTo("BrandID", brandId).findAll();
                    for (ProductModel productModel : productModels) {

                            RealmResults<ProductGiftModel> productGiftModels = realm.where(ProductGiftModel.class).equalTo("ProductID", productModel.getId()).findAll();

                            for (ProductGiftModel productGiftModel : productGiftModels) {
                                CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class)
                                        .equalTo("CusId", customerModel.Id).equalTo("GiftID", productGiftModel.getGiftID()).findFirst();
                                if (customerGiftModel != null) {
                                    giftModelList.add(realm.copyFromRealm(customerGiftModel));
                                }
                            }

                    }
                }
            }

            for (CurrentBrandModel currentBrandModel : currentBrandModels) {
                if (currentBrandModel.getBrandModel().getNumberGiftOfDay() != -1) {
                    RealmResults<BrandSetDetailModel> brandSetDetailModelRealmResults = realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();

                    for (BrandSetDetailModel brandSetDetailModel : brandSetDetailModelRealmResults) {
                        CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class)
                                .equalTo("CusId", customerModel.Id).equalTo("GiftID", brandSetDetailModel.getGiftID()).findFirst();
                        if (customerGiftModel != null) {
                            giftModelList.add(realm.copyFromRealm(customerGiftModel));
                        }
                    }
                }
            }
            list.put(customerModel, giftModelList);

        }
        return list;
    }

    public static LinkedHashMap<BrandModel, Integer> getListNumberGiftWithBrand(Realm realm, String phone, List<Integer> idListBrand) {
        LinkedHashMap<BrandModel, Integer> list = new LinkedHashMap<>();

        RealmResults<CustomerModel> results = realm.where(CustomerModel.class)
                .equalTo("CustomerPhone", phone)
                .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort())).findAll();
        RealmResults<CurrentBrandModel> currentBrandModels = realm.where(CurrentBrandModel.class)
                .equalTo("IsUsed", true).findAll();
        for (CustomerModel customerModel : results) {
            for (int brandId : idListBrand) {
                BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandId).equalTo("IsDialLucky", false).notEqualTo("NumberGiftOfDay", -1).findFirst();
                if (brandModel != null) {
                    RealmResults<ProductModel> productModels = realm.where(ProductModel.class).equalTo("BrandID", brandId).findAll();

                    for (ProductModel productModel : productModels) {
                        int countGift = 0;
                        if (list.get(brandModel) != null) {
                            countGift = list.get(brandModel);
                        }
                        RealmResults<ProductGiftModel> productGiftModels = realm.where(ProductGiftModel.class).equalTo("ProductID", productModel.getId()).findAll();
                        for (ProductGiftModel productGiftModel : productGiftModels) {
                            CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class)
                                    .equalTo("CusId", customerModel.Id).equalTo("GiftID", productGiftModel.getGiftID()).findFirst();
                            if (customerGiftModel != null) {
                                countGift += customerGiftModel.getNumberGift();
                            }
                        }
                        if (countGift > 0) {
                            list.put(brandModel, countGift);
                        }

                    }
                }
            }
            for (CurrentBrandModel currentBrandModel : currentBrandModels) {
                if (currentBrandModel.getBrandModel().getNumberGiftOfDay() != -1) {
                    int countGift = 0;
                    if (list.get(currentBrandModel.getBrandModel()) != null) {
                        countGift = list.get(currentBrandModel.getBrandModel());
                    }
                    RealmResults<BrandSetDetailModel> brandSetDetailModelRealmResults = realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();

                    for (BrandSetDetailModel brandSetDetailModel : brandSetDetailModelRealmResults) {
                        CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class)
                                .equalTo("CusId", customerModel.Id).equalTo("GiftID", brandSetDetailModel.getGiftID()).findFirst();
                        if (customerGiftModel != null) {
                            countGift += customerGiftModel.getNumberGift();
                        }
                    }
                    if (countGift > 0) {
                        list.put(currentBrandModel.getBrandModel(), countGift);
                    }
                }


            }

        }
        return list;
    }

    public static CustomerModel getInfoCusByPhone(Realm realm, String phone) {
        RealmResults<CustomerModel> results = realm.where(CustomerModel.class).equalTo("CustomerPhone", phone).findAll();
        return results.size() > 0 ? realm.copyFromRealm(results.last()) : null;
    }

    public static LinkedHashMap<CustomerModel, TotalRotationModel> getCustomerById(Realm realm, int customerId) {
        LinkedHashMap<CustomerModel, TotalRotationModel> linkedHashMap = new LinkedHashMap<>();
        CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", customerId).findFirst();
        TotalRotationModel totalRotationModel = realm.where(TotalRotationModel.class)
                .equalTo("CustomerId", customerId).findFirst();
        linkedHashMap.put(realm.copyFromRealm(customerModel), realm.copyFromRealm(totalRotationModel));
        return linkedHashMap;
    }

    public static LinkedHashMap<CustomerModel, List<ImageModel>> getCusLuckyUploadServer(Realm realm, int cusId) {
        LinkedHashMap<CustomerModel, List<ImageModel>> list = new LinkedHashMap<>();
        CustomerModel customerModel = realm.where(CustomerModel.class)
                .equalTo("Id", cusId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();

        List<ImageModel> imageModelList = new ArrayList<>();
        RealmResults<CustomerImageModel> customerImageList = realm.where(CustomerImageModel.class)
                .equalTo("CusID", customerModel.Id).equalTo("ImageIDServer", 0).findAll();
        for (CustomerImageModel model : customerImageList) {
            ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", model.getImageID())
                    .findFirst();
            if (imageModel != null) {
                imageModelList.add(realm.copyFromRealm(imageModel));
            }
        }

        list.put(realm.copyFromRealm(customerModel), imageModelList);


        return list;
    }

    public static CustomerModel getInfoCusNotFinished(Realm realm, String orderCode, String phone, int createdBy, boolean mega, boolean SP) {
        CustomerModel customerModel = null;
        if (mega && SP) {
            customerModel = realm.where(CustomerModel.class)
                    .equalTo("BillNumber", orderCode)
                    .equalTo("CustomerPhone", phone)
                    .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()))
                    .equalTo("CreatedBy", createdBy)
                    .equalTo("Status", Constants.WAITING_UPLOAD)
                    .findFirst();

            if (customerModel != null && customerModel.isFinished() && customerModel.isFinishedSP()) {
                customerModel = null;
            }
        } else if (mega) {
            customerModel = realm.where(CustomerModel.class)
                    .equalTo("BillNumber", orderCode)
                    .equalTo("CustomerPhone", phone)
                    .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()))
                    .equalTo("CreatedBy", createdBy)
                    .equalTo("Finished", false)
                    .equalTo("Status", Constants.WAITING_UPLOAD)
                    .findFirst();
        } else {
            customerModel = realm.where(CustomerModel.class)
                    .equalTo("BillNumber", orderCode)
                    .equalTo("CustomerPhone", phone)
                    .equalTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()))
                    .equalTo("CreatedBy", createdBy)
                    .equalTo("FinishedSP", false)
                    .equalTo("Status", Constants.WAITING_UPLOAD)
                    .findFirst();
        }

        return customerModel != null ? realm.copyFromRealm(customerModel) : null;
    }

    public static List<CustomerProductModel> getListProductByCustomerId(Realm realm, int id) {
        RealmResults<CustomerProductModel> customerProductModels = realm.where(CustomerProductModel.class).equalTo("CusID", id).findAll();

        return realm.copyFromRealm(customerProductModels);
    }

    public static List<ImageModel> getListImageByCustomerId(Realm realm, int id) {
        List<ImageModel> list = new ArrayList<>();
        RealmResults<CustomerImageModel> customerImageModels = realm.where(CustomerImageModel.class).equalTo("CusID", id).findAll();
        for (CustomerImageModel customerImageModel : customerImageModels) {
            ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", customerImageModel.getImageID()).findFirst();
            list.add(realm.copyFromRealm(imageModel));
        }
        return list;
    }

    public static CustomerModel getInfoCustomerById(Realm realm, int customerId) {
        CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", customerId).findFirst();
        RealmResults<TotalRotationBrandModel> totalRotationBrandModels = realm.where(TotalRotationBrandModel.class)
                .equalTo("CustomerId", customerId).equalTo("Finished", false).findAll();

        return realm.copyFromRealm(customerModel);


    }

    public static List<TotalRotationBrandModel> getListTotalRotationBrand(Realm realm, int customerId) {
        RealmResults<TotalRotationBrandModel> totalRotationBrandModels = realm.where(TotalRotationBrandModel.class)
                .equalTo("CustomerId", customerId).equalTo("Finished", false).findAll();
        return realm.copyFromRealm(totalRotationBrandModels);


    }

    public static void finishRotationBrandCustomer(Realm realm, int customerId) {
        CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", customerId).findFirst();
        customerModel.setFinishedSP(true);
    }

    public static void updateStatusCustomerImageById(Realm realm, int id) {
        CustomerImageModel customerImageModel = realm.where(CustomerImageModel.class).equalTo("ImageID", id).findFirst();
        if (customerImageModel != null) {
            customerImageModel.setStatus(Constants.UPLOADED);
        }

    }

    public static void updateStatusCustomer(Realm realm) {
        RealmResults<CustomerModel> results = realm.where(CustomerModel.class)
                .notEqualTo("serverId", -1)
                .equalTo("Status", Constants.WAITING_UPLOAD)
                .findAll();

        for (CustomerModel customerModel : results) {
            customerModel.setStatus(Constants.FINISHED);
        }

    }
}