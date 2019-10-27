package com.demo.sp19.screen.rotation;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {RotationModule.class})
public interface RotationComponent {
    void inject(RotationActivity activity);

}
