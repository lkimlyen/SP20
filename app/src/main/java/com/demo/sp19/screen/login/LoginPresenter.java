package com.demo.sp19.screen.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.TokenManager;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getName();
    private final LoginContract.View view;
    @Inject
    LocalRepository localRepository;

    LoginUsecase loginUsecase;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view, LoginUsecase loginUsecase) {
        this.view = view;
        this.loginUsecase = loginUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }

    @Override
    public void login(String username, String password,String deviceId) {
        view.showProgressBar();

        loginUsecase.executeIO(new LoginUsecase.RequestValue(username, password, TokenManager.getInstance().getToken(), deviceId), new BaseUseCase.UseCaseCallback
                <LoginUsecase.ResponseValue, LoginUsecase.ErrorValue>() {
            @Override
            public void onSuccess(LoginUsecase.ResponseValue successResponse) {
                view.hideProgressBar();
                Log.d(TAG, new Gson().toJson(successResponse.getEntity()));
                //Lưu thông tin user vào sharepreference qua class manager
                UserManager.getInstance().setUser(successResponse.getEntity());
                view.loginSuccess();
            }

            @Override
            public void onError(LoginUsecase.ErrorValue errorResponse) {
                if(errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))){
                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                }else {
                    view.showError(errorResponse.getDescription());
                }
                view.hideProgressBar();
            }
        });
    }

}
