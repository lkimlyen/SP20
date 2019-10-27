package com.demo.sp19.screen.setting;


import com.demo.architect.data.model.BrandSetEntity;
import com.demo.architect.data.model.ChooseSetEntitiy;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface SettingContract {
    interface View extends BaseView<Presenter> {
        //upload file lên firebase
        void uploadFile(String path, UserEntity user);

        //caid đặt app sau khi download file cài đặt xong
        void installApp(String path);

        //download lại dữ liệu
        void resetAllData(UserEntity user, String version);

        //đi đên trang login sau khi logout
        void goToLogin();
    }

    interface Presenter extends BasePresenter {
        //lưu lại database và gửi lên firebase
        void backupData();

        //đổi mật khẩu
        void changePassword(String passOld, String passNew);

        String getVersion();

        //cập nhật phần mềm
        void updateApp();

        //xóa hình ảnh đã lưu
        void clearImage();

        void counNumberWaitingUpload(String version);

        //đăng xuất
        void logout();
    }
}
