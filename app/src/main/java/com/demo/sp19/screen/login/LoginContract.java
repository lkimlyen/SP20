package com.demo.sp19.screen.login;

import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void showError(String error);

        void loginSuccess();

    }

    interface Presenter extends BasePresenter {
        void login(String username, String password, String deviceId);
    }
}
