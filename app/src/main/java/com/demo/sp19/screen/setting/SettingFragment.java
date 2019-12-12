package com.demo.sp19.screen.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.model.ChooseSetEntitiy;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.dialogs.ChangePasswordDialog;
import com.demo.sp19.manager.ChooseSetManager;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.screen.login.LoginActivity;
import com.demo.sp19.util.Precondition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingFragment extends BaseFragment implements SettingContract.View {
    private final String TAG = SettingFragment.class.getName();
    private SettingContract.Presenter mPresenter;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseAuth auth;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @BindView(R.id.tv_state)
    TextView tvState;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333) {
            if (resultCode == Activity.RESULT_OK) {
                UserManager.getInstance().setUser(null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {


        tvVersion.setText(String.format(getString(R.string.text_version), mPresenter.getVersion()));

        // mPresenter.getListBrandSetCurrent();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
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
    public void showError(String message) {
        startDialogNoti(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {

        startDialogNoti(message, SweetAlertDialog.SUCCESS_TYPE);
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
    public void uploadFile(String path, UserEntity user) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        String email = getString(R.string.text_email_firebase);
        String password = getString(R.string.text_password_firebase);
        auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            Log.d(TAG, "Login fail");

                        } else {
                            storageRef = storage.getReference();
                            Log.d(TAG, "Success");
                        }
                    }
                });
        UploadTask uploadTask;
        Uri file = Uri.fromFile(new File(path));
        if (storageRef != null) {
            Calendar instance = Calendar.getInstance();
            int month = instance.get(Calendar.MONTH) + 1;
            StorageReference riversRef = storageRef.child(user.getOutlet().getOutletName() + "/" + instance.get(Calendar.YEAR) + "/" + month + "/" + instance.get(Calendar.DATE) + "/" + ConvertUtils.getCodeGenerationByTime() + file.getLastPathSegment());
            uploadTask = riversRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    if (isAdded()) {
                        showError(getString(R.string.text_backup_fail));
                        Log.d(TAG, exception.getMessage());
                        hideProgressBar();
                    }

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (isAdded()) {
                        showSuccess(getString(R.string.text_backup_success));
                        hideProgressBar();
                    }

                }
            });
        } else {
            showError(getString(R.string.text_backup_fail));
            hideProgressBar();
        }

    }


    @Override
    public void installApp(String path) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Uri uriApk = FileProvider.getUriForFile(getContext(),
                "com.demo.sp19.fileprovider",
                new File(path));

        Intent install = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (!getContext().getPackageManager().canRequestPackageInstalls()) {
                startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:com.demo.sp19")));
            }
            install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        } else {
            install = new Intent(Intent.ACTION_VIEW);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            install.setDataAndType(uriApk, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        }
        install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivityForResult(install, 333);
    }

    @Override
    public void resetAllData(UserEntity user, String version) {

        showProgressBar();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference();
        String date = ConvertUtils.getDateTimeCurrentShort().replace("/","_");
        String name =  user.getName().replace(".","");
        mDatabase.child("reset_data").child(date).child(user.getTeamOutletId() + "_" + name).setValue(version).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...

                hideProgressBar();
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        hideProgressBar();
                        e.printStackTrace();
                        showError(getString(R.string.text_reset_fail));
                    }
                });


    }

    @Override
    public void goToLogin() {
        UserManager.getInstance().setUser(null);
        RealmHelper.getInstance().initRealm(false);
        LoginActivity.start(getContext());
        getActivity().finishAffinity();
    }

    @OnClick(R.id.ll_backup)
    public void backupData() {
        mPresenter.backupData();
    }

    @OnClick(R.id.ll_change_password)
    public void changePassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setOnChangeListener(new ChangePasswordDialog.OnChangeListener() {
            @Override
            public void onChange(String passOld, String passNew) {
                mPresenter.changePassword(passOld, passNew);
            }
        }, new ChangePasswordDialog.OnErrorListener() {
            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    @OnClick(R.id.ll_logout)
    public void logOut() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_notification))
                .setContentText(getString(R.string.text_do_you_want_logout))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.logout();
                    }
                }).setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.ll_check_version)
    public void updateApp() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_notification))
                .setContentText(getString(R.string.text_do_you_want_update_app))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.updateApp();
                    }
                }).setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.ll_reset_data)
    public void resetData() {
        mPresenter.counNumberWaitingUpload(tvVersion.getText().toString());
    }

    @OnClick(R.id.ll_clear_image)
    public void clearImage() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_notification))
                .setContentText(getString(R.string.text_do_you_want_clear_image))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.clearImage();
                    }
                }).setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.ll_update_set)
    public void updateSet() {
        List<ChooseSetEntitiy> list = ChooseSetManager.getInstance().getListChooseSet();
        if (list != null && list.size() > 0) {
            tvState.setText(getString(R.string.text_not_update));
            tvState.setTextColor(getResources().getColor(R.color.red));
        } else {
            tvState.setText(getString(R.string.text_updated));
            tvState.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
