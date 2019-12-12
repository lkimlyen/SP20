package com.demo.sp19.screen.gift;


import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface GiftContract {
    interface View extends BaseView<Presenter> {
        void showListProductChangeGift(List<ProductModel> list);

        void showCustomerCode(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list);

        void showCustomerPhone(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list, LinkedHashMap<BrandModel, Integer> brandModelIntegerLinkedHashMap);

        void showInfoCus(CustomerModel customerModel, List<CustomerProductModel> customerProductModelProductModelLinkedHashMap,
                         List<ImageModel> customerImageModelImageModelLinkedHashMap);

        void showNameAndCusCode(CustomerModel customerModel);

        void goToRotationMega(int customerId);

        void goToRotationSP(int customerId);

        void resetInfoCutomerWhenInput();

    }

    interface Presenter extends BasePresenter {
        //lấy danh sách sản phẩm đổi quà
        void getListProductChangeGift();

        //lấy brand theo id
        BrandModel getBrandModelByBrandId(int brandId);

        //kiểm tra mã hóa đơn đã được đổi trong ngày chưa
        void checkOrderCode(String orderCode);

        //kiểm tra khách hầng đã đổi quà trong ngày chưa
        void checkCustomerPhone(String phone);

        //lưu thông tin khách hàng, sl sản phẩm đã mua
        void saveInfoCustomer(String orderCode, String cusCode, String cusName, String phone, String sex, String email, int yearOfBirth, String reasonBuy,
                              String note, List<String> imagePath, LinkedHashMap<ProductModel, Integer> numberProductList,
                              LinkedHashMap<Integer, Integer> brandList,
                              LinkedHashMap<ProductModel, Integer> productChooseNumberList, int totalRotaion);

        //lấy thông tin của khách hàng bị quay mà app bị lỗi
        void getInfoCusCrash(String orderCode, String phone, int customerId);

        //kiểm tra khách hàng đang quay ở brand nào
        void checkCondition(CustomerModel customerModel);

    }
}
