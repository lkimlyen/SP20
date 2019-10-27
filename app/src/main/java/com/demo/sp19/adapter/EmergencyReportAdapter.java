/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.sp19.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.EmergencyModel;
import com.demo.sp19.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class EmergencyReportAdapter extends RealmBaseAdapter<EmergencyModel> implements ListAdapter {

    private OnEndListener onEndListener;

    public EmergencyReportAdapter(OrderedRealmCollection<EmergencyModel> realmResults, OnEndListener onEndListener) {
        super(realmResults);
        this.onEndListener = onEndListener;
    }


    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if (convertView == null)

        {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_emergency_report, parent, false);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else

        {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        if (adapterData != null)

        {
            final EmergencyModel item = adapterData.get(position);
            setDataToViews(viewHolder, item, position);

        }
        return convertView;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerial;
        TextView tvEmployeeCode;
        TextView tvTimestart;
        Button btEnd;
        public EmergencyModel data;

        MyViewHolder(View view) {
            super(view);
            tvSerial = view.findViewById(R.id.tv_serial);
            tvEmployeeCode = view.findViewById(R.id.tv_employee_code);
            tvTimestart = view.findViewById(R.id.tv_timestart);
            btEnd = view.findViewById(R.id.bt_end);
        }
    }


    private void setDataToViews(MyViewHolder holder, EmergencyModel item, int position) {

        //noinspection ConstantConditions
        holder.tvSerial.setText(String.valueOf(position + 1));
        holder.tvTimestart.setText(item.getStartDateTime());
        holder.tvEmployeeCode.setText(item.getEmployeeId());
        holder.btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEndListener.onEnd(item.getId());
            }
        });


    }

    public interface OnEndListener {
        void onEnd(int id);
    }
}
