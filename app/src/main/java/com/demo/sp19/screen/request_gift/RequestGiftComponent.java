package com.demo.sp19.screen.request_gift;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {RequestGiftModule.class})
public interface RequestGiftComponent {
    void inject(RequestGiftActivity activity);

}
