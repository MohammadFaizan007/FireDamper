package com.e.firedamper.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.AppHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestStatusAdapter extends BaseAdapter implements AdvertiseResultInterface, ReceiverResultInterface {
    Activity activity;
    ArrayList<BeconDeviceClass> arrayList;
    ScannerTask scannerTask;
    AnimatedProgress animatedProgress;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;
    int ahu_hex, flour_hex;
    boolean isAdvertisingFinished = false;


    public TestStatusAdapter(@NonNull Activity context) {
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
                    inflate(R.layout.latency_adapter, parent, false);

        }
        BeconDeviceClass beconDeviceClass = arrayList.get(position);
        String forward = beconDeviceClass.getForword_latency();
        Integer forward_int_value = Integer.parseInt(forward,16);

        String backward = beconDeviceClass.getBackword_latency();
        Integer backward_int_value = Integer.parseInt(backward,16);

        ViewHolder viewHolder = new ViewHolder(convertView);
        if (beconDeviceClass.getForword_latency().equalsIgnoreCase("00")){
            viewHolder.forward.setText("Not set");
        }else {
            viewHolder.forward.setText(forward_int_value.toString());
        }
        if (beconDeviceClass.getBackword_latency().equalsIgnoreCase("00")){
            viewHolder.backward.setText("Not set");
        }else {
            viewHolder.backward.setText(backward_int_value.toString());
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
        @BindView(R.id.review_1)
        ImageView review1;
        @BindView(R.id.forward)
        TextView forward;
        @BindView(R.id.backward)
        TextView backward;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


