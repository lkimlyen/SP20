package com.demo.sp19.screen.home;


import com.demo.architect.data.model.UserEntity;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void showListLogo(List<Integer> list);

        //show số lượng dữ liệu chưa upload
        void showCountNumberWaiting(int count);

        //show popup tiến trình upload
        void showDialogDownload(int progress, int positionDownload);

        //ẩn popup tiến trình upload
        void hideDialogDownload();

        //update tiến trình upload trên dialog upload
        void updateDialogDownload(int progressTask, int progress);

        //hiện nút upload lại có lỗi
        void showButtonRetry();

        //show thông tin user
        void showInfoUser(UserEntity userEntity);

    }

    interface Presenter extends BasePresenter {
        void getListLogo();

        //đếm số lượng dữ liệu chưa upload
        void countNumberUpload();

        //upload dữ liệu lên server
        void uploadAllData();

        //lấy thông tin user
        void getInfoUser();

    }
}
