package com.demo.compass.data.repository.base.product.remote;

import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BrandEntitiy;
import com.demo.compass.data.model.BrandSetDetailEntity;
import com.demo.compass.data.model.BrandSetEntity;
import com.demo.compass.data.model.GiftEntity;
import com.demo.compass.data.model.ProductEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface ProductApiInterface {

    @GET("https://sp.imark.com.vn/ws/api/GetProductWS?pAppCode=IDS")
    Call<BaseListResponse<ProductEntity>> getProduct(@Query("pProjectID") int projectId,
                                                     @Query("pOutletID") int outletId);

    @GET("https://sp.imark.com.vn/WS/api/GetBrandWS?pAppCode=IDS")
    Call<BaseListResponse<BrandEntitiy>> getBrand(@Query("pProjectID") int projectId);

    @GET("https://sp.imark.com.vn/WS/api/GetBrandSetWS?pAppCode=IDS")
    Call<BaseListResponse<BrandSetEntity>> getBrandSet(@Query("pProjectID") int projectId);

    @GET("https://sp.imark.com.vn/WS/api/GetBrandSetDetailWS?pAppCode=IDS")
    Call<BaseListResponse<BrandSetDetailEntity>> getBrandSetDetail(@Query("pProjectID") int projectId);

    @GET("https://sp.imark.com.vn/WS/api/GetGiftWS?pAppCode=IDS")
    Call<BaseListResponse<GiftEntity>> getGift(@Query("pProjectID") int projectId);

    @FormUrlEncoded
    @POST("https://sp.imark.com.vn/WS/api/GetOutletBrandWS")
    Call<BaseListResponse<Integer>> getOutletBrand(@Field("pAppCode") String appCode,
                                                   @Field("pOutletID") int outletId);
}
