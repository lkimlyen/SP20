package com.demo.sp19.screen.stock;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class StockModule {
    private final StockContract.View StockContractView;

    public StockModule(StockContract.View StockContractView) {
        this.StockContractView = StockContractView;
    }

    @Provides
    @NonNull
    StockContract.View provideStockContractView() {
        return this.StockContractView;
    }
}

