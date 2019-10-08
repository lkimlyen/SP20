package com.demo.compass.phongthuy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.demo.compass.phongthuy.app.bus.MainThreadBus;
import com.demo.compass.phongthuy.app.di.component.ApplicationComponent;
import com.demo.compass.phongthuy.app.di.component.DaggerApplicationComponent;
import com.demo.compass.phongthuy.app.di.module.ApplicationModule;
import com.demo.compass.phongthuy.app.di.module.NetModule;
import com.demo.compass.phongthuy.app.di.module.UseCaseModule;
import com.google.firebase.FirebaseApp;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by uyminhduc on 12/16/16.
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CoreApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static CoreApplication sInstance;
    private String TAG = CoreApplication.class.getSimpleName();
    private static Bus bus;

    public static CoreApplication getInstance() {
        return sInstance;
    }

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        initializeCrashAnalytics();
        initializeFirebase();
        initializeDagger();
        initializeRealm();
        initializeCalligraphy();
        //  initializeFacebook();
        initializeEventBus();

    }

    private void initializeEventBus() {
        bus = new MainThreadBus(ThreadEnforcer.ANY);
    }

    public static void postEvent(Object event) {
        bus.post(event);
    }

    public static void registerForEvents(Object o) {
        bus.register(o);
    }

    public static void unregisterForEvents(Object o) {
        bus.unregister(o);
    }

    private void initializeCrashAnalytics() {
        Fabric.with(this, new Crashlytics());
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initializeMultiDex();
    }

    private void initializeMultiDex() {
        MultiDex.install(this);
    }

    private void initializeFacebook() {
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeRealm() {
        Realm.init(this);
    }

    private void initializeDagger() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule("http://google.com/"))
                .useCaseModule(new UseCaseModule())
                .build();

        this.applicationComponent.inject(this);
    }

    private void initializeCalligraphy() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/FuturaSTD-Bold.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
