package com.demo.sp19.screen.posm;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class POSMModule {
    private final POSMContract.View NotificationContractView;

    public POSMModule(POSMContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    POSMContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

