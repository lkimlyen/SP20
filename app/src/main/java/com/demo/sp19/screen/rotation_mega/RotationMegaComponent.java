package com.demo.sp19.screen.rotation_mega;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {RotationMegaModule.class})
public interface RotationMegaComponent {
    void inject(RotationMegaActivity activity);

}
