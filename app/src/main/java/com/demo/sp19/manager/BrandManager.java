package com.demo.sp19.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.sp19.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

public class BrandManager {
    private List<BrandModel> brandList;
    private static BrandManager instance;

    public static BrandManager getInstance() {
        if (instance == null) {
            instance = new BrandManager();
        }
        return instance;
    }

    public void setBrandList(List<BrandModel> list) {
        brandList = list;
    }

    public List<BrandModel> getBrandList() {
        return brandList;
    }

    public List<ProductModel> getListProductByBrand(int brandId) {
        List<ProductModel> list = new ArrayList<>();
        for (BrandModel brandModel : brandList) {
            if (brandModel.getId() == brandId) {
                list.addAll(brandModel.getProductList());
                break;
            }
        }
        return list;
    }


    public BrandModel getBrandModelByBrandId(int brandId) {
        for (BrandModel brandModel : brandList) {
            if (brandModel.getId() == brandId) {
                return brandModel;
            }
        }
        return null;
    }
}
