package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BrandSetEntity;
import com.demo.architect.data.model.ChooseSetEntitiy;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.sp19.R;
import com.demo.sp19.manager.ChooseSetManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChooseSetDialog extends DialogFragment {
    private List<BrandSetModel> list = new ArrayList<>();
    private String name;
    private List<RadioButton> radioButtonList = new ArrayList<>();
    private int brandIdSelect;

    public void setList(int brandIdSelect, String name, List<BrandSetModel> list) {
        this.list = list;
        this.name = name;
        this.brandIdSelect = brandIdSelect;
    }

    private OnChangeListener onChangeListener;
    private OnCheckCoditionListener onCheckCoditionListener;

    public void setOnChangeListener(OnChangeListener onChangeListener, OnCheckCoditionListener onCheckCoditionListener) {
        this.onChangeListener = onChangeListener;
        this.onCheckCoditionListener = onCheckCoditionListener;
    }

    private ChooseSetEntitiy brandSetModelSelect;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_choose_brand_set);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(String.format(getString(R.string.text_choose_set_gift), name));
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        radioGroup.removeAllViews();
        for (BrandSetModel brandSetModel : list) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inf.inflate(R.layout.item_radiobutton, null);
            RadioButton rbSet = view.findViewById(R.id.rb_set);
            rbSet.setText(brandSetModel.getSetName());
            rbSet.setTag(brandSetModel);
            radioButtonList.add(rbSet);

            if (brandSetModel.getId() == brandIdSelect) {
                rbSet.setChecked(true);
            } else {
                rbSet.setChecked(false);
            }
            rbSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrandSetModel brandSetModel1 = ((BrandSetModel) v.getTag());
                    ChooseSetEntitiy chooseSetEntitiy = ChooseSetManager.getInstance().getChooseSetByBrandSetId(brandSetModel1.getBrandID(), brandSetModel1.getId());
                    if (chooseSetEntitiy != null) {
                        if (onCheckCoditionListener.onCheckCodition(brandSetModel1.getId())) {
                            brandSetModelSelect = chooseSetEntitiy;
                            for (RadioButton radioButton : radioButtonList) {
                                BrandSetModel brandSetModel2 = (BrandSetModel) radioButton.getTag();
                                if (brandSetModel2.getId() == chooseSetEntitiy.getBrandSetId()) {
                                    radioButton.setChecked(true);
                                } else {
                                    radioButton.setChecked(false);
                                }
                            }
                        }else {

                            rbSet.setChecked(false);
                            Toast.makeText(getActivity(), "Set quà vượt quá số lượng được cho phép", Toast.LENGTH_SHORT).show();
                        }
                    } else {


                        rbSet.setChecked(false);
                        Toast.makeText(getActivity(), "Bạn chưa có quyền thay đổi set quà này", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            radioGroup.addView(view);
        }
        dialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (brandSetModelSelect != null) {
                    onChangeListener.onChange(brandSetModelSelect);
                }


            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnChangeListener {
        void onChange(ChooseSetEntitiy chooseSetEntitiy);
    }

    public interface OnCheckCoditionListener {
        boolean onCheckCodition(int brandSetId);
    }
}
