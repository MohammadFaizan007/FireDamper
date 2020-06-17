package com.e.firedamper.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.ReceiverResultInterface;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.AppHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunctionAdapter extends BaseAdapter implements AdvertiseResultInterface, ReceiverResultInterface {
    Activity activity;
    ArrayList<BeconDeviceClass> arrayList;
    ArrayList<DeviceClass> list ;
//    DeviceClass deviceClass;
    ScannerTask scannerTask;
    AnimatedProgress animatedProgress;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;
    int ahu_hex, flour_hex;
    boolean isAdvertisingFinished = false;


    public FunctionAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
        scannerTask = new ScannerTask(activity, this);
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        if (AppHelper.IS_TESTING) {
            setArrayList();
        }
    }

    public void clearList() {
        if (this.arrayList == null)
            this.arrayList = new ArrayList<>();
        this.arrayList.clear();
        notifyDataSetChanged();

    }

    public void setArrayList(ArrayList<BeconDeviceClass> arrayList) {
//        if(this.arrayList==null)
//            this.arrayList=new ArrayList<>();
//        this.arrayList.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();

    }

    public void setArrayList() {
        for (int i = 0; i <= 20; i++) {
            BeconDeviceClass beconDeviceClass = new BeconDeviceClass();
            beconDeviceClass.setBeaconUID(i + 10);
            beconDeviceClass.setDeviceUid((i + 10) + "");
            beconDeviceClass.setDeriveType(0x01);
            arrayList.add(beconDeviceClass);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public BeconDeviceClass getItem(int position) {
        if (arrayList.size() <= position)
            return null;
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.function_status_adapter, parent, false);

        }
        BeconDeviceClass beconDeviceClass = arrayList.get(position);
        ViewHolder viewHolder = new ViewHolder(convertView);
//        String relay_uid = beconDeviceClass.getRelay_uid();
//        Integer int_uid = Integer.parseInt(relay_uid, 16);
//        viewHolder.uid.setText("UID Number   = "+int_uid.toString());
//        if (beconDeviceClass.isAdded()) {
//            Log.e("Checck" ,beconDeviceClass.getDeviceName());
//        }
//        viewHolder.uid.setText("UID Number   =" + list.get(position).getDeviceName());

//        Log.e("SECOND>>>>>>>",list.get(position).getDeviceName());
        if (beconDeviceClass.getNormal_relay1().equalsIgnoreCase("00")) {
            viewHolder.normal_r1.setText("Disabled");
            viewHolder.normal_r1.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_red));
        } else if (beconDeviceClass.getNormal_relay1().equalsIgnoreCase("01")) {
            viewHolder.normal_r1.setText("Enabled");
            viewHolder.normal_r1.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_green));


        }

        if (beconDeviceClass.getNormal_relay2().equalsIgnoreCase("00")) {
            viewHolder.normal_r2.setText("Disabled");
            viewHolder.normal_r2.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_red));

        } else if (beconDeviceClass.getNormal_relay2().equalsIgnoreCase("01")) {
            viewHolder.normal_r2.setText("Enabled");
            viewHolder.normal_r2.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_green));


        }

        if (beconDeviceClass.getFire_relay1().equalsIgnoreCase("00")) {
            viewHolder.fire_r1.setText("Disabled");
            viewHolder.fire_r1.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_red));


        } else if (beconDeviceClass.getFire_relay1().equalsIgnoreCase("01")) {
            viewHolder.fire_r1.setText("Enabled");
            viewHolder.fire_r1.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_green));


        }

        if (beconDeviceClass.getFire_relay2().equalsIgnoreCase("00")) {
            viewHolder.fire_r2.setText("Disabled");
            viewHolder.fire_r2.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_red));

        } else if (beconDeviceClass.getFire_relay2().equalsIgnoreCase("01")) {
            viewHolder.fire_r2.setText("Enabled");
            viewHolder.fire_r2.setBackground(activity.getResources().getDrawable(R.drawable.btn_backgroun_green));

        }
        return convertView;
    }

    @Override
    public void onSuccess(String message) {
        animatedProgress.showProgress();
        Log.w(TAG, "Advertising start");
    }


    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Advertising Failed.", Toast.LENGTH_SHORT).show();
        animatedProgress.hideProgress();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        scannerTask = new ScannerTask(activity, this);
        scannerTask.setRequestCode(resultCode);
        scannerTask.start();
        isAdvertisingFinished = true;
        Log.w(TAG, "Advertising stop" + resultCode);
    }


    @Override
    public void onScanSuccess(int successCode, ByteQueue byteQueue) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();

    }

    @Override
    public void onScanFailed(int errorCode) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();

    }

    static class ViewHolder {
        @BindView(R.id.name_image)
        ImageView review1;
        @BindView(R.id.normal_r1)
        Button normal_r1;
        @BindView(R.id.normal_r2)
        Button normal_r2;
        @BindView(R.id.fire_r1)
        Button fire_r1;
        @BindView(R.id.fire_r2)
        Button fire_r2;
//        @BindView(R.id.uid)
//        TextView uid;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


