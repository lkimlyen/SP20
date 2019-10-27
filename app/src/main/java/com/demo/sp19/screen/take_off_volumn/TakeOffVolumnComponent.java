package com.demo.sp19.screen.take_off_volumn;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {TakeOffVolumnModule.class})
public interface TakeOffVolumnComponent {
    void inject(TakeOffVolumnActivity activity);

}
