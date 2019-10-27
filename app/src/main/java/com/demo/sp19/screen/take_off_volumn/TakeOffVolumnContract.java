package com.demo.sp19.screen.take_off_volumn;


import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by MSI on 26/11/2017.
 */

public interface TakeOffVolumnContract {
    interface View extends BaseView<Presenter> {
        //show ds số lượng đã bán theo product
        void showListTakeOffVolumn(LinkedHashMap<ProductModel, TakeOffVolumnModel> list);
    }

    interface Presenter extends BasePresenter {
        //lấy ds số lượng đã bán theo product by date
        void getListTakeOffVolumn();
        //lưu danh sách số lượng đã bán
        void saveListTakeOffVolumn(List<TakeOffVolumnModel> list);
    }
}
