package com.demo.sp19.app.base;

/**
 * Created by uyminhduc on 12/16/16.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
    void showProgressBar();
    void hideProgressBar();
    void showError(String message);
    void showSuccess(String message);
}
