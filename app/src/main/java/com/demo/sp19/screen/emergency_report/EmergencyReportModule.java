package com.demo.sp19.screen.emergency_report;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class EmergencyReportModule {
    private final EmergencyReportContract.View ManagerGiftContractView;

    public EmergencyReportModule(EmergencyReportContract.View ManagerGiftContractView) {
        this.ManagerGiftContractView = ManagerGiftContractView;
    }

    @Provides
    @NonNull
    EmergencyReportContract.View provideNotificationContractView() {
        return this.ManagerGiftContractView;
    }
}

