package com.demo.sp19.screen.take_off_volumn;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class TakeOffVolumnModule {
    private final TakeOffVolumnContract.View TakeOffVolumnContractView;

    public TakeOffVolumnModule(TakeOffVolumnContract.View TakeOffVolumnContractView) {
        this.TakeOffVolumnContractView = TakeOffVolumnContractView;
    }

    @Provides
    @NonNull
    TakeOffVolumnContract.View provideTakeOffVolumnContractView() {
        return this.TakeOffVolumnContractView;
    }
}

