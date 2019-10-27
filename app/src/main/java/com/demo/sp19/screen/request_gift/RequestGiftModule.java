package com.demo.sp19.screen.request_gift;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class RequestGiftModule {
    private final RequestGiftContract.View NotificationContractView;

    public RequestGiftModule(RequestGiftContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    RequestGiftContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

