package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class StockModel extends RealmObject {
    @PrimaryKey
    private long Id;
    @SerializedName("pStockCode")
    @Expose
    private String StockCode;

    @SerializedName("pOutletID")
    @Expose
    private int OutletID;

    @SerializedName("pProductID")
    @Expose
    private int ProductID;

    @SerializedName("pNumber")
    @Expose
    private int NumberSP;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private Date DateCreate;
    private String CreatedDateTime;

    @SerializedName("pNumberType")
    @Expose
    private int NumberType;
    private int LastUpdatedBy;
    @SerializedName("pDeviceDateTime")
    @Expose
    private String LastUpdatedDateTime;
    private int Status;
    private int RowVersion;

    public StockModel() {
    }

    public StockModel(int outletID, int productID, int numberSP, int createdBy) {
        OutletID = outletID;
        ProductID = productID;
        NumberSP = numberSP;
        CreatedBy = createdBy;
    }

    public StockModel(long Id, String stockCode, int outletID, int productID, int numberSP, int createdBy, Date dateCreate, String createdDateTime, int numberType, String lastUpdatedDateTime, int status, int rowVersion) {
        this.Id = Id;
        StockCode = stockCode;
        OutletID = outletID;
        ProductID = productID;
        NumberSP = numberSP;
        CreatedBy = createdBy;
        DateCreate = dateCreate;
        CreatedDateTime = createdDateTime;
        NumberType = numberType;
        LastUpdatedDateTime = lastUpdatedDateTime;
        Status = status;
        RowVersion = rowVersion;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(StockModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void addStock(Realm realm, List<StockModel> list, int outletId, int type) {
        //lấy ngày hiện tại theo format dd/MM/yyyy
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        //add từng item trong list
        for (StockModel item : list) {
            //kiểm tra stock trong ngày đã được lưu và upload lên hay chưa
            StockModel stockModel = realm.where(StockModel.class).equalTo("OutletID", item.OutletID)
                    .equalTo("ProductID", item.ProductID)
                    .equalTo("DateCreate", dateCurrent)
                    .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();
            //nếu stock khác null thì update giá trị vào
            if (stockModel != null) {
                stockModel.setNumberSP(item.NumberSP);
                stockModel.setLastUpdatedBy(item.LastUpdatedBy);
                stockModel.setLastUpdatedDateTime(ConvertUtils.getDateTimeCurrent());
                stockModel.setRowVersion(stockModel.RowVersion + 1);
                stockModel.setNumberType(stockModel.NumberType);
            } else {//nếu null thì tạo mới
                stockModel = new StockModel(id(realm) + 1, ConvertUtils.getCodeGenerationByTime(), item.OutletID,
                        item.ProductID, item.NumberSP, item.CreatedBy, ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()), ConvertUtils.getDateTimeCurrent(),
                        type, ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD, 0);
                realm.copyToRealm(stockModel);
            }
        }
    }

    public static LinkedHashMap<ProductModel, StockModel> getStockByDate(Realm realm, int ouletId) {
        LinkedHashMap<ProductModel, StockModel> list = new LinkedHashMap<>();
        //lấy ds product có trong stock
        RealmResults<ProductModel> listProduct = realm.where(ProductModel.class).equalTo("IsStock", true).findAll();
        //lấy ngày hiện tại theo format dd/MM/yyyy
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        //lấy stock theo product
        for (ProductModel productModel : listProduct) {
            //lấy ds stock theo ngày
            RealmResults<StockModel> results = realm.where(StockModel.class).equalTo("OutletID", ouletId).equalTo("ProductID", productModel.getId())
                    .equalTo("DateCreate", dateCurrent)
                    .findAll();
            //kiểm tra list kết quả có hay không nếu có lấy giá trị cuối cùng or truyền vào null để add vào list
            if (results.size() > 0) {
                list.put(productModel, results.last());
            } else {
                list.put(productModel, null);
            }
        }

        return list;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getNumberSP() {
        return NumberSP;
    }

    public void setNumberSP(int numberSP) {
        NumberSP = numberSP;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public int getLastUpdatedBy() {
        return LastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {
        LastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedDateTime() {
        return LastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
        LastUpdatedDateTime = lastUpdatedDateTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getRowVersion() {
        return RowVersion;
    }

    public void setRowVersion(int rowVersion) {
        RowVersion = rowVersion;
    }

    public int getNumberType() {
        return NumberType;
    }

    public void setNumberType(int numberType) {
        NumberType = numberType;
    }


    public static List<StockModel> getListStockWaitingUpload(Realm realm, int outletId) {
        RealmResults<StockModel> results = realm.where(StockModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static int updateStatusStock(Realm realm, int outletId) {
        RealmResults<StockModel> results = realm.where(StockModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (StockModel stockModel : results) {
            stockModel.setStatus(Constants.UPLOADED);
        }
        return results.size();
    }
}
