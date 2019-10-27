package com.demo.sp19.screen.rotation_mega;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class RotationMegaModule {
    private final RotationMegaContract.View NotificationContractView;

    public RotationMegaModule(RotationMegaContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    RotationMegaContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

