package com.demo.sp19.screen.confirm_set;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ConfirmSetModule {
    private final ConfirmSetContract.View NotificationContractView;

    public ConfirmSetModule(ConfirmSetContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    ConfirmSetContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

