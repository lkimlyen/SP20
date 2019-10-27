package com.demo.sp19.screen.gift;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {GiftModule.class})
public interface GiftComponent {
    void inject(GiftFragment giftFragment);

}
