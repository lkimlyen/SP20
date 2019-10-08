package com.demo.compass.phongthuy.app.di.component;



import com.demo.compass.phongthuy.app.CoreApplication;
import com.demo.compass.phongthuy.app.base.BaseActivity;
import com.demo.compass.phongthuy.app.base.BaseFragment;
import com.demo.compass.phongthuy.app.di.module.ApplicationModule;
import com.demo.compass.phongthuy.app.di.module.NetModule;
import com.demo.compass.phongthuy.app.di.module.RepositoryModule;
import com.demo.compass.phongthuy.app.di.module.UseCaseModule;
import com.demo.compass.phongthuy.screen.login.LoginComponent;
import com.demo.compass.phongthuy.screen.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by uyminhduc on 12/16/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        NetModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CoreApplication application);

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    LoginComponent plus(LoginModule module);
}
