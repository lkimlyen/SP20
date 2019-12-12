package com.demo.sp19.screen.dashboard;


import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DashboardContract {
    interface View extends BaseView<Presenter> {
        void showDialogDownload(int totalTask, int progress, int positionDownload);

        void hideDialogDownload();

        void updateDialogDownload(int progressTask, int progress);

        void showButtonRetry(int positionTask);
    }

    interface Presenter extends BasePresenter {
        void getListProduct();

        void downloadFromServer();

        void downloadTask(int positionTask);

        void clearData();
    }
}
