package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RequestGiftDialog extends DialogFragment {
    private List<CurrentBrandModel> list = new ArrayList<>();

    public void setList(List<CurrentBrandModel> list) {
        this.list = list;
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public OnSaveListener onSaveListener;
    private LinkedHashMap<Integer, Integer> requestList = new LinkedHashMap<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_request_gift);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout llGift = dialog.findViewById(R.id.ll_gift);
        llGift.removeAllViews();
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (CurrentBrandModel giftModel : list) {
            View v = inf.inflate(R.layout.item_gift_product, null);
            TextView tvName = v.findViewById(R.id.tv_name);
            EditText etNumber = v.findViewById(R.id.et_number);
            etNumber.setTag(giftModel);
          //  tvName.setText(String.format(getString(R.string.text_set_gift), giftModel.getBrandModel().getBrandName(), giftModel.getBrandSetModel().getSetName()));

            etNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    CurrentBrandModel productGiftModel = (CurrentBrandModel) etNumber.getTag();
                    int number = 0;
                    try {
                        number = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (number > 0) {
                        requestList.put(productGiftModel.getBrandSetID(), number);

                    } else {
                        if (requestList.get(productGiftModel.getBrandSetID()) != null) {
                            requestList.remove(productGiftModel.getBrandSetID());
                        }
                    }
                }
            });
            llGift.addView(v);
        }

        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_notification))
                        .setContentText(getString(R.string.text_do_you_want_cancel_request))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                dialog.dismiss();
                            }
                        }).setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
        });

        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestList.size() == 0) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.text_notification))
                            .setContentText(getString(R.string.text_number_set_null))
                            .setConfirmText(getString(R.string.text_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                    return;
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_notification))
                        .setContentText(getString(R.string.text_do_you_want_send_request))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                dialog.dismiss();
                                onSaveListener.onSave(requestList);
                            }
                        }).setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
        });
        return dialog;
    }

    public interface OnSaveListener {
        void onSave(LinkedHashMap<Integer, Integer> requestList);
    }
}
