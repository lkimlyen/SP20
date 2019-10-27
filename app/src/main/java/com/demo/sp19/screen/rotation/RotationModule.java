package com.demo.sp19.screen.rotation;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class RotationModule {
    private final RotationContract.View NotificationContractView;

    public RotationModule(RotationContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    RotationContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

