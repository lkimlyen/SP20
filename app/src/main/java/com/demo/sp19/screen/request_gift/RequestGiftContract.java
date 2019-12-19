package com.demo.sp19.screen.request_gift;


import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface RequestGiftContract {
    interface View extends BaseView<Presenter> {
        void showListBrandSetDetailCurrent(LinkedHashMap<Object, List<BrandSetDetailModel>> list);
    }

    interface Presenter extends BasePresenter {
        //lấy ds quà của set quà theo các brand đang chạy của outlet
        void getListBrandSetDetailCurrent();
        //gửi yêu cầu quà và lưu lại thông tin yêu cầu
        void saveRequestGift(LinkedHashMap<Integer, Integer> linkedHashMap);
    }
}
