package com.demo.sp19.screen.confirm_set;


import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ConfirmSetContract {
    interface View extends BaseView<Presenter> {
        void showListConfirm(List<ConfirmSetEntity> confirmList, LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> brandSetList);
    }

    interface Presenter extends BasePresenter {
        void getListConfirmReciever();

        //xác nhận số set quà đã nhận
        void confirmSet(Map<String, Object> params);

        //lấy ds yêu câu quà chưa được giao
        void getListConfirmRequestGift(boolean upload);
    }
}
