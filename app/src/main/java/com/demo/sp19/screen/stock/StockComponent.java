package com.demo.sp19.screen.stock;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {StockModule.class})
public interface StockComponent {
    void inject(StockFragment stockFragment);

}
