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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
import com.demo.sp19.R;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TakeOffVolumnAdapter extends RecyclerView.Adapter<TakeOffVolumnAdapter.StockViewHolder> {

    //ds list off take volume lấy từ database
    private LinkedHashMap<ProductModel, TakeOffVolumnModel> masterlist = new LinkedHashMap<>();
    //ds sản phẩm
    private List<ProductModel> listProduct = new ArrayList<>();

    //ds off take volumn sp đã chỉnh sửa
    private LinkedHashMap<Integer, TakeOffVolumnModel> takeOffVolumnSaveList = new LinkedHashMap<>();
    //ds id off take volume đã xóa
    private Set<Long> idRemoveList = new HashSet<>();
    //ds list off take volume đã lưu vào database
    private List<TakeOffVolumnModel> listStockSaved = new ArrayList<>();
    private Context context;

    public TakeOffVolumnAdapter(LinkedHashMap<ProductModel, TakeOffVolumnModel> masterlist, Context context) {
        this.context = context;
        this.masterlist = masterlist;
        this.listProduct = new ArrayList<ProductModel>(masterlist.keySet());
        //lấy ds off take volume # null đã lấy từ database add vào list riêng
        for (Map.Entry<ProductModel, TakeOffVolumnModel> map : masterlist.entrySet()){
            if (map.getValue() != null){
                listStockSaved.add(map.getValue());
            }
        }
    }

    public List<TakeOffVolumnModel> getListTakeOffVolumnEdit() {
        return new ArrayList<>(takeOffVolumnSaveList.values());
    }
    public List<TakeOffVolumnModel> getListStockSave() {
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
        if (masterlist != null && 0 <= position && position < masterlist.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(StockViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Resources r = context.getResources();
        //tính toán lại kích thước hình ảnh sản phẩm
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
        //lấy off take volume theo product
        TakeOffVolumnModel takeOffVolumnFromDatabase = masterlist.get(productModel);
        File file = new File(productModel.getFilePath());
        Picasso.get().load(file).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.ivProduct);
        holder.tvName.setText(productModel.getProductName());

        //lấy off take volume từ ds takeOffVolumnSaveList
        TakeOffVolumnModel takeOffVolumnSave = takeOffVolumnSaveList.get(productModel.getId());

        //kiểm tra có giá trị hay không nếu có thì set vào edittext số lượng
        if (takeOffVolumnSave != null) {
            holder.etNumber.setText(String.valueOf(takeOffVolumnSave.getNumberSP()));
        } else {
            //kiểm tra off take volume lấy từ database có null hay không và trong list id đã xóa có id của off take volume đó không
            if (takeOffVolumnFromDatabase != null && !idRemoveList.contains(takeOffVolumnFromDatabase.getId())) {
                holder.etNumber.setText(String.valueOf(takeOffVolumnFromDatabase.getNumberSP()));
            } else {
                holder.etNumber.setText("");
            }
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

                    //lấy giá trị off take volume từ ds lấy từ database
                    if (masterlist.get(productModel) != null) {
                        //cập nhật lại ds id đã xóa
                        idRemoveList.remove(masterlist.get(productModel).getId());
                    }
                    //lấy off take volume từ ds lưu  theo product id
                    TakeOffVolumnModel takeOffVolumnSave = takeOffVolumnSaveList.get(productModel.getId());

                    //kiểm tra có khác null hay không nếu có thì cập nhật lại giá trị, null thì tạo mới và add vào ds takeOffVolumnSaveList
                    if (takeOffVolumnSave != null) {
                        if (takeOffVolumnSave.getNumberSP() == numberInput) {
                            return;
                        }
                        takeOffVolumnSave.setNumberSP(numberInput);
                    } else {
                        takeOffVolumnSave = new TakeOffVolumnModel(userEntity.getOutlet().getOutletId(), productModel.getId(),
                                numberInput, userEntity.getTeamOutletId());
                        takeOffVolumnSaveList.put(productModel.getId(), takeOffVolumnSave);
                    }

                } catch (Exception e) {
                    //lấy giá trị off take volume từ ds lấy từ database
                    if (masterlist.get(productModel) != null) {
                        //add id của off take volume vào ds id xóa
                        idRemoveList.add(masterlist.get(productModel).getId());
                    }
                    //xóa off take volume đã lưu
                    takeOffVolumnSaveList.remove(productModel.getId());
                }
            }
        };

        Gson gson = new Gson();
        Log.d("SAVEJSON", gson.toJson(takeOffVolumnSaveList));
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
    }

    @Override
    public int getItemCount() {
        return masterlist.size();
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
