package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangePasswordDialog extends DialogFragment {
    public void setOnChangeListener(OnChangeListener onChangeListener,OnErrorListener onErrorListener) {
        this.onChangeListener = onChangeListener;
        this.onErrorListener = onErrorListener;
    }

    public OnChangeListener onChangeListener;
    private OnErrorListener onErrorListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_change_password);
        dialog.setCanceledOnTouchOutside(true);

        EditText etPassOld = dialog.findViewById(R.id.et_pass_old);
        EditText etPassNew = dialog.findViewById(R.id.et_pass_new);
        EditText etRePassNew = dialog.findViewById(R.id.et_re_pass_new);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPassOld.getText().toString())){
                    onErrorListener.onError(getString(R.string.text_password_old_null));
                    return;
                }
                if (TextUtils.isEmpty(etPassNew.getText().toString())){
                    onErrorListener.onError(getString(R.string.text_password_new_null));
                    return;
                }
                if (TextUtils.isEmpty(etRePassNew.getText().toString())){
                    onErrorListener.onError(getString(R.string.text_re_password_new_null));
                    return;
                }

                if (!etPassNew.getText().toString().equals(etRePassNew.getText().toString())){
                    onErrorListener.onError(getString(R.string.text_password_not_equal));
                    return;
                }
                dialog.dismiss();
                onChangeListener.onChange(etPassOld.getText().toString(),etRePassNew.getText().toString());
            }
        });
        return dialog;
    }

    public interface OnChangeListener {
        void onChange(String passOld, String passNew);
    }
    public interface OnErrorListener {
        void onError(String message);
    }
}
