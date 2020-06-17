package com.e.firedamper.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailableDevicesAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<DeviceClass> arrayList;
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;

    public AvailableDevicesAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();

    }

    public void setList(List<DeviceClass> arrayList1) {
        arrayList.clear();
        arrayList.addAll(arrayList1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public DeviceClass getItem(int position) {
        if (arrayList.size() <= position)
            return null;
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.available_device_adapter, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        DeviceClass deviceClass = arrayList.get(position);
        viewHolder.addDeviceUid.setText(deviceClass.getDeviceName());
        viewHolder.add_device.setOnClickListener(v -> {
            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.TEST_CODE);
            intent.putExtra(Constants.DEVICE_DETAIL_KEY, arrayList.get(position));
            activity.startActivity(intent);

        });

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.add_device_uid)
        TextView addDeviceUid;
//        @BindView(R.id.show_attribute)
//        Button showAttribute;
//        @BindView(R.id.damper_detail)
//        Button damper_detail;
//        @BindView(R.id.icon_delete)
//        ImageView icon_delete;
        @BindView(R.id.add_device)
        Button add_device;

        ViewHolder(View view) {

            ButterKnife.bind(this, view);

        }
    }


}

