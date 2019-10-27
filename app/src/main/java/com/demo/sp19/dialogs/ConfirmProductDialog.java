package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.sp19.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfirmProductDialog extends DialogFragment {

    private LinkedHashMap<ProductModel, Integer> ProductList = new LinkedHashMap<>();

    public void setProductList(LinkedHashMap<ProductModel, Integer> productList) {
        ProductList = productList;
    }

    public void setListener(OnConfirmListener onConfirmListener, OnCancleListener onCancleListener) {
        this.onConfirmListener = onConfirmListener;
        this.onCancleListener = onCancleListener;
    }

    public OnConfirmListener onConfirmListener;
    public OnCancleListener onCancleListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_confirm_product);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout layout = dialog.findViewById(R.id.ll_content);
        layout.removeAllViews();
        for (Map.Entry<ProductModel, Integer> map : ProductList.entrySet()) {
            if (map.getValue() > 0) {
                LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inf.inflate(R.layout.item_gift, null);
                ImageView ivGift = v.findViewById(R.id.iv_gift);
                ivGift.setVisibility(View.GONE);
                TextView tvName = v.findViewById(R.id.tv_name);
                tvName.setTextColor(getResources().getColor(R.color.black));
                TextView tvNumber = v.findViewById(R.id.tv_number);
                tvNumber.setTextColor(getResources().getColor(R.color.black));
                tvName.setText(map.getKey().getProductName());
                tvNumber.setText(String.valueOf(map.getValue()));
                layout.addView(v);
            }

        }
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onCancleListener.onCancle();
            }
        });

        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onConfirmListener.onConfirm();
            }
        });
        return dialog;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public interface OnCancleListener {
        void onCancle();
    }
}
