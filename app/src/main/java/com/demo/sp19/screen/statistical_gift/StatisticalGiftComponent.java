package com.demo.sp19.screen.statistical_gift;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {StatisticalGiftModule.class})
public interface StatisticalGiftComponent {
    void inject(StatisticalGiftActivity activity);

}
