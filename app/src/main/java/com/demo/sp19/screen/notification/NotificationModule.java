package com.demo.sp19.screen.notification;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class NotificationModule {
    private final NotificationContract.View NotificationContractView;

    public NotificationModule(NotificationContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    NotificationContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

