package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class TopupCardDialog extends DialogFragment {
    private OnConfirmListener confirmListener;

    private String phone;

    Realm realm = Realm.getDefaultInstance();
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_topup_card);
        dialog.setCanceledOnTouchOutside(true);

        EditText etPhone = dialog.findViewById(R.id.et_phone);
        etPhone.setText(phone);

        Spinner spType = dialog.findViewById(R.id.sp_type);
        List<String> typeList = new ArrayList<>();
        typeList.add("Trả trước");
        typeList.add("Trả sau");
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typeList);
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(reasonAdapter);

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Date DateCreate = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        dialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    return;
                }

                if (confirmListener!= null){
                    confirmListener.onConfirm(etPhone.getText().toString(),spType.getSelectedItemPosition() == 0? "prepaid" : "postpaid");
                }
            }
        });
        dialog.findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public interface OnConfirmListener {
        void onConfirm(String phone, String type);
    }
}
