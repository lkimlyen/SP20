package com.demo.sp19.screen.gift;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class GiftModule {
    private final GiftContract.View NotificationContractView;

    public GiftModule(GiftContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    GiftContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

