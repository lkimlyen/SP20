package com.demo.compass.phongthuy.screen.login;


import com.demo.compass.phongthuy.app.base.BasePresenter;
import com.demo.compass.phongthuy.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void showError(String error);

        void loginSuccess();

    }

    interface Presenter extends BasePresenter {
        void login(String username, String password);
    }
}
