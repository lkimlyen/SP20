package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.ProductGiftEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ProductGiftModel extends RealmObject {

    @PrimaryKey
    private int Id;

    private int ProductID;

    private int GiftID;

    private int Number;

    private GiftModel giftModel;
    public ProductGiftModel() {
    }

    public ProductGiftModel(int id, int productID, int giftID, int number) {
        Id = id;
        ProductID = productID;
        GiftID = giftID;
        Number = number;
    }

    public int getId() {
        return Id;
    }

    public int getProductID() {
        return ProductID;
    }

    public int getGiftID() {
        return GiftID;
    }

    public int getNumber() {
        return Number;
    }

    public void setGiftModel(GiftModel giftModel){
        this.giftModel = giftModel;
    }

    public GiftModel getGiftModel() {
        return giftModel;
    }

    public static void addProductGift(Realm realm, List<ProductGiftEntity> list) {
        RealmResults<ProductGiftModel> results = realm.where(ProductGiftModel.class).findAll();
        results.deleteAllFromRealm();
        for (ProductGiftEntity productGiftEntity : list) {
            ProductGiftModel productGiftModel = new ProductGiftModel(productGiftEntity.getProductGiftID(),
                    productGiftEntity.getProductID(), productGiftEntity.getGiftID(), productGiftEntity.getNumber());
            productGiftModel = realm.copyToRealm(productGiftModel);
            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id",productGiftModel.GiftID).findFirst();
            productGiftModel.setGiftModel(giftModel);
        }
    }

    public static LinkedHashMap<BrandModel, List<ProductGiftModel>> getListGiftByProduct(Realm realm, List<Integer> idProductList) {
        LinkedHashMap<BrandModel, List<ProductGiftModel>> list = new LinkedHashMap<>();
        for (Integer id : idProductList) {
            //lấy product theo id
            ProductModel productModel = realm.where(ProductModel.class).equalTo("Id", id).findFirst();
            //lấy brand id từ productModel
            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", productModel.getBrandID()).findFirst();
            List<ProductGiftModel> giftModelList = new ArrayList<>();
            //kiểm tra ds quà theo brand đã có chưa
            if (list.get(brandModel) != null) {
                giftModelList.addAll(list.get(brandModel));
            }
            //lấy tất cả gift theo product id
            RealmResults<ProductGiftModel> results = realm.where(ProductGiftModel.class).equalTo("ProductID", id).findAll();
                giftModelList.addAll(realm.copyFromRealm(results));
            list.put(brandModel,giftModelList );
        }
        return list;
    }
}
