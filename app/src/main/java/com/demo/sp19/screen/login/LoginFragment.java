package com.demo.sp19.screen.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.demo.sp19.R;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.screen.dashboard.DashboardActivity;
import com.demo.sp19.util.Precondition;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {
    private final String TAG = LoginFragment.class.getName();
    private LoginContract.Presenter mPresenter;
    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.edt_username)
    EditText edtUsername;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = Precondition.checkNotNull(presenter);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        //kiểm tra các trường hợp lỗi và cảnh báo trc khi login
        if (edtUsername.getText().toString().equals("")) {
            startDialogNoti(getActivity().getString(R.string.text_username_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        if (edtPassword.getText().toString().equals("")) {
            startDialogNoti(getActivity().getString(R.string.text_password_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        //lấy deviceId, phải xin quyền READ_PHONE_STATE nếu không sẽ lỗi
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mPresenter.login(edtUsername.getText().toString(), edtPassword.getText().toString(),deviceId);
    }

    private void startDialogNoti(String content, int type) {
        Activity activity = getActivity();
        if (activity != null) {
            new SweetAlertDialog(activity, type)
                    .setTitleText(getString(R.string.text_sweet_dialog_title))
                    .setContentText(content)
                    .setConfirmText(getString(R.string.text_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    @Override
    public void showError(String error) {
        startDialogNoti(error, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {

    }

    @Override
    public void loginSuccess() {
        DashboardActivity.start(getContext());
        getActivity().finish();
    }


}
