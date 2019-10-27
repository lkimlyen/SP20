package com.demo.sp19.screen.timekeeping;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class TimekeepingModule {
    private final TimekeepingContract.View TimekeepingContractView;

    public TimekeepingModule(TimekeepingContract.View TimekeepingContractView) {
        this.TimekeepingContractView = TimekeepingContractView;
    }

    @Provides
    @NonNull
    TimekeepingContract.View provideTimekeepingContractView() {
        return this.TimekeepingContractView;
    }
}

