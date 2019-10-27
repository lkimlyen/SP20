package com.demo.sp19.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.StockModel;
import com.demo.sp19.R;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private LinkedHashMap<ProductModel, StockModel> masterList = new LinkedHashMap<>();
    private List<ProductModel> listProduct = new ArrayList<>();
    private List<StockModel> listStockSaved = new ArrayList<>();
    private LinkedHashMap<Integer, StockModel> stockHashSaveList = new LinkedHashMap<>();
    private Context context;
    private Set<Long> idRemoveList = new HashSet<>();

    public StockAdapter(LinkedHashMap<ProductModel, StockModel> masterList, Context context) {
        this.context = context;
        this.masterList = masterList;
        this.listProduct = new ArrayList<ProductModel>(masterList.keySet());
        for (Map.Entry<ProductModel, StockModel> map : masterList.entrySet()){
            if (map.getValue() != null){
                listStockSaved.add(map.getValue());
            }
        }
    }

    public List<StockModel> getListStockEdit() {
        return new ArrayList<>(stockHashSaveList.values());
    }
    public List<StockModel> getListStockSave() {
        return listStockSaved;
    }


    public Set<Long> idRemoveList() {
        return idRemoveList;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock, parent, false);
        StockViewHolder holder = new StockViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        if (masterList != null && 0 <= position && position < masterList.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(StockViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Resources r = context.getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics()));
        int widthPx = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics()));
        int heightPx = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 71, r.getDisplayMetrics()));
        LinearLayout.LayoutParams layoutParams;
        if (width / 3 >= px) {
            layoutParams = new LinearLayout.LayoutParams(width / 3,
                    ((width / 3) / widthPx * heightPx));
        } else {
            layoutParams = new LinearLayout.LayoutParams(width / 2,
                    ((width / 2) / widthPx * heightPx));
        }
        layoutParams.setMargins(0, (int) context.getResources().getDimension(R.dimen.distance_24dp), 0, 0);
        holder.ivProduct.setLayoutParams(layoutParams);
        ProductModel productModel = listProduct.get(position);
        StockModel stockModel = masterList.get(productModel);
        File file = new File(productModel.getFilePath());
        Picasso.get().load(file).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.ivProduct);
        holder.tvName.setText(productModel.getProductName());
        StockModel stockModel1 = stockHashSaveList.get(productModel.getId());
        if (stockModel1 != null) {

            holder.etNumber.setText(String.valueOf(stockModel1.getNumberSP()));

        } else {
            if (stockModel != null && !idRemoveList.contains(stockModel.getId())) {
                holder.etNumber.setText(String.valueOf(stockModel.getNumberSP()));
            } else {
                holder.etNumber.setText("");
            }
        }
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        if (hour >= 10) {
            //   holder.etNumber.setEnabled(false);
            holder.etNumber.setClickable(true);
            holder.etNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        Calendar calendar1 = Calendar.getInstance();
                        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                        if (hour >= 10) {
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getString(R.string.text_notification))
                                    .setContentText(context.getString(R.string.text_close_oos))
                                    .setConfirmText(context.getString(R.string.text_ok))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }

                    return true; // return is important...
                }
            });
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int numberInput = Integer.parseInt(s.toString());
                    UserEntity userEntity = UserManager.getInstance().getUser();
                    StockModel stockModel2 = stockHashSaveList.get(productModel.getId());
                    if (masterList.get(productModel) != null) {
                        idRemoveList.remove(masterList.get(productModel).getId());
                    }
                    if (stockModel2 != null) {
                        if (stockModel2.getNumberSP() == numberInput) {
                            return;
                        }
                        stockModel2.setNumberSP(numberInput);
                    } else {
                        stockModel2 = new StockModel(userEntity.getOutlet().getOutletId(), productModel.getId(),
                                numberInput, userEntity.getTeamOutletId());
                        stockHashSaveList.put(productModel.getId(), stockModel2);
                    }

                } catch (Exception e) {
                    if (masterList.get(productModel) != null) {
                        idRemoveList.add(masterList.get(productModel).getId());
                    }
                    stockHashSaveList.remove(productModel.getId());
                }
            }
        };

        Gson gson = new Gson();
        Log.d("SAVEJSON", gson.toJson(stockHashSaveList));
        holder.etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.etNumber.addTextChangedListener(textWatcher);

                } else {
                    holder.etNumber.removeTextChangedListener(textWatcher);
                }
            }
        });

        holder.etNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1 = Calendar.getInstance();
                int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                if (hour >= 10) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.text_notification))
                            .setContentText(context.getString(R.string.text_close_oos))
                            .setConfirmText(context.getString(R.string.text_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return masterList.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvName;
        private EditText etNumber;
        private LinearLayout llRoot;

        private StockViewHolder(View v) {
            super(v);
            ivProduct = v.findViewById(R.id.iv_product);
            tvName = v.findViewById(R.id.tv_name);
            etNumber = v.findViewById(R.id.et_number);
            llRoot = v.findViewById(R.id.ll_root);

        }
    }

}
