package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.ProductEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ProductModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String ProductCode;
    private String ProductName;
    private String FilePath;
    private int BrandID;
    private boolean IsChangeGift;
    private boolean IsPrice;
    private boolean IsStock;
    private boolean IsTakeOffVolume;
    private boolean IsGetInfo;
    private int NumberOfEnough;

    public ProductModel() {
    }

    public ProductModel(int id, String productCode, String productName, String filePath, int brandID, boolean isChangeGift, boolean isPrice, boolean isStock, boolean isTakeOffVolume, boolean isGetInfo, int numberOfEnough) {
        Id = id;
        ProductCode = productCode;
        ProductName = productName;
        FilePath = filePath;
        BrandID = brandID;
        IsChangeGift = isChangeGift;
        IsPrice = isPrice;
        IsStock = isStock;
        IsTakeOffVolume = isTakeOffVolume;
        IsGetInfo = isGetInfo;
        NumberOfEnough = numberOfEnough;
    }

    public static void addProduct(Realm realm, ProductEntity productEntity, String path) {
        ProductModel productModel = new ProductModel(productEntity.getId(), productEntity.getProductCode(),
                productEntity.getProductName(), path,
                productEntity.getBrandID(), productEntity.isChangeGift(), productEntity.isPrice(),
                productEntity.isStock(), productEntity.isTakeOffVolume(), productEntity.isGetInfo(), productEntity.getNumberOfEnough());
        realm.copyToRealm(productModel);
    }

    public int getId() {
        return Id;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public String getProductName() {
        return ProductName;
    }


    public String getFilePath() {
        return FilePath;
    }

    public int getBrandID() {
        return BrandID;
    }

    public boolean isChangeGift() {
        return IsChangeGift;
    }

    public boolean isPrice() {
        return IsPrice;
    }

    public boolean isStock() {
        return IsStock;
    }

    public boolean isTakeOffVolume() {
        return IsTakeOffVolume;
    }

    public int getNumberOfEnough() {
        return NumberOfEnough;
    }

    public static void deleteProduct(Realm realm) {
        RealmResults<ProductModel> results = realm.where(ProductModel.class).findAll();
        for (ProductModel productModel : results) {
            File file = new File(productModel.FilePath);
            file.delete();
        }
        results.deleteAllFromRealm();
    }

    public static List<ProductModel> getListProductByBrandId(Realm realm, List<Integer> idList) {
        List<ProductModel> list = new ArrayList<>();
        for (Integer id : idList) {
            RealmResults<ProductModel> productModel = realm.where(ProductModel.class)
                    .equalTo("BrandID", id).equalTo("IsGetInfo",true).findAll();
            list.addAll(realm.copyFromRealm(productModel));
        }
        return list;
    }
}
