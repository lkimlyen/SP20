package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.GiftEntity;
import com.demo.architect.data.model.GiftReceiveEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.sp19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ConfirmSetGiftDialog extends DialogFragment {
    private ConfirmSetEntity confirmSetEntity;

    public void setConfirmSetEntity(ConfirmSetEntity confirmSetEntity) {
        this.confirmSetEntity = confirmSetEntity;
    }

    public void setListener(OnConfirmListener onConfirmListener, OnCancleListener onCancleListener) {
        this.onConfirmListener = onConfirmListener;
        this.onCancleListener = onCancleListener;
    }

    public OnConfirmListener onConfirmListener;
    public OnCancleListener onCancleListener;

    Realm realm = Realm.getDefaultInstance();

    private List<EditText> editTextList = new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_confirm_set);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout layout = dialog.findViewById(R.id.container);
        layout.removeAllViews();
        for (ConfirmSetEntity.DetailSet detailSet : confirmSetEntity.getDetailSetList()) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_confirm_set_detail, null);
            LinearLayout container = v.findViewById(R.id.container);
            TextView tvName = v.findViewById(R.id.tv_name);
            tvName.setTextColor(getResources().getColor(R.color.black));
            TextView tvNumber = v.findViewById(R.id.tv_number);
            tvNumber.setTextColor(getResources().getColor(R.color.black));

            BrandSetModel brandSetModel = realm.where(BrandSetModel.class).equalTo("Id", detailSet.getBrandSetId()).findFirst();
            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandSetModel.getBrandID()).findFirst();

            tvName.setText(brandModel.getBrandName());
            tvNumber.setText(String.valueOf(detailSet.getNumber()));
            RealmResults<BrandSetDetailModel> brandSetDetailModelRealmResults =
                    realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", brandSetModel.getId()).findAll();
            for (BrandSetDetailModel brandSetDetailModel : brandSetDetailModelRealmResults) {
                View view = inf.inflate(R.layout.item_confirm_gift_detail, null);
                TextView tvGiftName = view.findViewById(R.id.tv_gift_name);
                TextView tvRequestNumber = view.findViewById(R.id.tv_request_number);
                tvGiftName.setText(brandSetDetailModel.getGiftModel().getGiftName());
                EditText etReceiveNumber = view.findViewById(R.id.et_receive_number);


                for (GiftEntity giftEntity : confirmSetEntity.getGifts()) {
                    if (giftEntity.getId() == brandSetDetailModel.getGiftID()) {
                        etReceiveNumber.setTag(giftEntity);
                        editTextList.add(etReceiveNumber);
                        tvRequestNumber.setText(String.valueOf(giftEntity.getQuantity()));
                        etReceiveNumber.setText(String.valueOf(giftEntity.getQuantity()));
                        break;
                    }
                }
                container.addView(view);
            }
            layout.addView(v);


        }

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancleListener!= null)
                onCancleListener.onCancle();
            }
        });

        dialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onConfirmListener!= null){
                    Map<String, Object> params = new HashMap<>();
                    params.put("RequestID",confirmSetEntity.getID());
                    List<GiftReceiveEntity> giftReceiveEntityList = new ArrayList<>();
                    for (EditText editText : editTextList){
                        GiftEntity giftEntity = (GiftEntity) editText.getTag();

                        try {
                            int number = Integer.parseInt(editText.getText().toString());
                           giftReceiveEntityList.add(new GiftReceiveEntity(giftEntity.getId(),
                                    number, giftEntity.getQuantity() - number));
                        }catch (NumberFormatException e){
                            return;
                        }

                    }
                    params.put("Gifts",getCartJson(giftReceiveEntityList));
                    onConfirmListener.onConfirm(params);

                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnConfirmListener {
        void onConfirm(Map<String, Object> params);
    }

    public interface OnCancleListener {
        void onCancle();
    }
    public JSONArray getCartJson(List<GiftReceiveEntity> list) {

        JSONArray jsonArr = new JSONArray();
        try {


            for (GiftReceiveEntity request : list) {
                JSONObject sellerObj = new JSONObject();
                sellerObj.put("ID",request.getID());
                sellerObj.put("QuantitySuccess", request.getQuantitySuccess());
                sellerObj.put("QuantityFail", request.getQuantityFail());
                jsonArr.put(sellerObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArr;

    }
}
