package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.sp19.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class ImageOrderAdapter extends RecyclerView.Adapter<ImageOrderAdapter.ImageRealViewHolder> {

    private List<String> list = new ArrayList<>();
    private Context context;
    private boolean isDeleteEnable = true;

    public ImageOrderAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    public void addImage(String path) {
        list.add(path);
        notifyDataSetChanged();
    }

    public void setEnableDelete(boolean isDeleteEnable) {
        this.isDeleteEnable = isDeleteEnable;
    }

    public void addImageList(List<String> paths) {
        list.addAll(paths);
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public ImageRealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_order, parent, false);
        ImageRealViewHolder holder = new ImageRealViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageRealViewHolder holder, int position) {
        if (list != null && 0 <= position && position < list.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(ImageRealViewHolder holder, int position) {
        File file = new File(list.get(position));
        Picasso.get().load(file).into(holder.ivImage);
        if (isDeleteEnable) {
            holder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.ivDelete.setVisibility(View.GONE);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(context.getString(R.string.text_notification))
                        .setContentText(context.getString(R.string.text_delete_image))
                        .setConfirmText(context.getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                file.delete();
                                list.remove(position);
                                notifyDataSetChanged();

                            }
                        }).setCancelText(context.getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageRealViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private ImageView ivDelete;

        private ImageRealViewHolder(View v) {
            super(v);
            ivImage = (ImageView) v.findViewById(R.id.iv_logo);
            ivDelete = v.findViewById(R.id.iv_delete);

        }
    }

}
