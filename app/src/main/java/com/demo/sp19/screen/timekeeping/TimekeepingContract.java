package com.demo.sp19.screen.timekeeping;


import android.graphics.Bitmap;

import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface TimekeepingContract {
    interface View extends BaseView<Presenter> {
        void showDateServer(long time);
        //show thông báo lỗi
        void showError(String message);
        //show camera để sp chụp hình chấm côg
        void updateUI();
        //show thông báo thành công
        void showSuccess(String message);
        //về lại trang home sau khi chấm công và chụp hình đầy đủ
        void backToHome();
        //show popup cảnh báo chưa chấm công vào
        void showDialogNotCheckIn();
    }

    interface Presenter extends BasePresenter {
        //chấm công sp và cảnh báo nhân viên khi chưa chấm công vào
        void attendanceTracking(double latitude, double longitude, int number, String type, boolean accept);
        //tải hình ảnh chấm công
        void uploadImage(double latitude, double longitude, String path);
    }
}
