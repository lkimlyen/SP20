package com.demo.compass.phongthuy.screen.login;

import android.util.Log;

import com.demo.compass.data.repository.base.local.LocalRepository;
import com.demo.compass.domain.BaseUseCase;
import com.demo.compass.domain.LoginUseCase;
import com.google.gson.Gson;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getName();
    private final LoginContract.View view;
    @Inject
    LocalRepository localRepository;

    private final LoginUseCase loginUsecase;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view, LoginUseCase loginUsecase) {
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
    public void login(String username, String password) {
        view.showProgressBar();

        loginUsecase.executeIO(new LoginUseCase.RequestValue(username, password, null, ""), new BaseUseCase.UseCaseCallback
                <LoginUseCase.ResponseValue, LoginUseCase.ErrorValue>() {
            @Override
            public void onSuccess(LoginUseCase.ResponseValue successResponse) {
                view.hideProgressBar();
                Log.d(TAG, new Gson().toJson(successResponse.getEntity()));
                view.showSuccess("Thành công");
            }

            @Override
            public void onError(LoginUseCase.ErrorValue errorResponse) {

                    view.showError(errorResponse.getDescription());
                view.hideProgressBar();
            }
        });
    }

}
