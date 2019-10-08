package com.demo.compass.data.repository.base.product.remote;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.model.BrandSetEntity;
import com.demo.compass.data.model.GiftEntity;
import com.demo.compass.data.model.ProductEntity;

import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface ProductRepository {
    Observable<BaseListResponse<ProductEntity>> getProduct(int projectId, int outletId);
    Observable<BaseListResponse<BrandEntitiy>> getBrand(int projectId);
    Observable<BaseListResponse<BrandSetEntity>> getBrandSet(int projectId);
    Observable<BaseListResponse<BrandSetDetailEntity>> getBrandSetDetail(int projectId);
    Observable<BaseListResponse<GiftEntity>> getGift(int projectId);
    Observable<BaseListResponse<Integer>> getOutletBrand(String appCode, int outletId);
}
