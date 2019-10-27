package com.demo.sp19.screen.rotation_mega;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseActivity;
import com.demo.sp19.constants.Constants;
import com.demo.sp19.util.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class RotationMegaActivity extends BaseActivity {
    @Inject
    RotationMegaPresenter RotationMegaPresenter;

    RotationMegaFragment fragment;

    public static void start(Activity context,int customerId, boolean isCrash) {
        Intent intent = new Intent(context, RotationMegaActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID, customerId);
        if (!isCrash){
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            context.startActivity(intent);
        }else {
            context.startActivityForResult(intent, 137);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new RotationMegaModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (RotationMegaFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = RotationMegaFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(RotationMegaFragment fragment, int frameId) {
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

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
    }
}
