package com.demo.sp19.screen.manager_gift;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ManagerGiftModule {
    private final ManagerGiftContract.View ManagerGiftContractView;

    public ManagerGiftModule(ManagerGiftContract.View ManagerGiftContractView) {
        this.ManagerGiftContractView = ManagerGiftContractView;
    }

    @Provides
    @NonNull
    ManagerGiftContract.View provideNotificationContractView() {
        return this.ManagerGiftContractView;
    }
}

