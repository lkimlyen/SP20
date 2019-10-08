package com.demo.compass.phongthuy.screen.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import com.demo.compass.phongthuy.R;
import com.demo.compass.phongthuy.app.CoreApplication;
import com.demo.compass.phongthuy.app.base.BaseActivity;
import com.demo.compass.phongthuy.app.di.Precondition;

import javax.inject.Inject;

import androidx.fragment.app.FragmentTransaction;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginActivity extends BaseActivity {
    @Inject
    LoginPresenter LoginPresenter;

    LoginFragment fragment;

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean clearTop) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new LoginModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(LoginFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.findViewById(android.R.id.content).findViewById(R.id.fragmentContainer).setPadding(0, 0, 0, 0);
    }

}
