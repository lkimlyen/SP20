package com.demo.sp19.screen.confirm_set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseActivity;
import com.demo.sp19.util.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmSetActivity extends BaseActivity {
    @Inject
    ConfirmSetPresenter RotationPresenter;

    ConfirmSetFragment fragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, ConfirmSetActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new ConfirmSetModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (ConfirmSetFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = ConfirmSetFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(ConfirmSetFragment fragment, int frameId) {
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
