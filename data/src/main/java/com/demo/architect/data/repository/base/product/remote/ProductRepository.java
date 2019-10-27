package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BrandEntitiy;
import com.demo.architect.data.model.BrandSetDetailEntity;
import com.demo.architect.data.model.BrandSetEntity;
import com.demo.architect.data.model.GiftEntity;
import com.demo.architect.data.model.ProductEntity;

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
