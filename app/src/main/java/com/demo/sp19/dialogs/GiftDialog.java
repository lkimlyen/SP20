package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.sp19.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by yenyen on 6/20/2017.
 */
public class GiftDialog extends DialogFragment {

    private String path;
    private boolean isGift = true;

    public void setPath(String path) {
        this.path = path;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView imgPresent = dialog.findViewById(R.id.img_present);
        TextView tvTitle= dialog.findViewById(R.id.tv_title);
        if(!isGift){
            tvTitle.setVisibility(View.GONE);
        }
        File file = new File(path);
        Picasso.get().load(file).into(imgPresent);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return dialog;
    }

}
