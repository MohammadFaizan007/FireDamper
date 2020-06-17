package com.e.firedamper.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.ScannerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowAttributeFragment extends Fragment {
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    Activity activity;
    Unbinder unbinder;
    //        TextView ahu_No = attribute_dialog.findViewById(R.id.ahu_No);
//        TextView air_type = attribute_dialog.findViewById(R.id.air_type);
//        TextView floor_No = attribute_dialog.findViewById(R.id.floor_No);
//        TextView damper_type = attribute_dialog.findViewById(R.id.damper_type);
//        TextView deviceName = attribute_dialog.findViewById(R.id.deviceName);
//        deviceName.setText("Device Name  :"+   deviceClass.getDeviceName());
//        ahu_No.setText(deviceClass.getAhuNumber());
//        air_type.setText(deviceClass.getAirType());
//        floor_No.setText(deviceClass.getFlourNumber());
//        damper_type.setText(deviceClass.getDamperType());

    @BindView(R.id.deviceName)
    TextView deviceName;
    @BindView(R.id.ahu_No)
    TextView ahu_No;
    @BindView(R.id.air_type)
    TextView air_type;
    @BindView(R.id.floor_No)
    TextView floor_No;
    @BindView(R.id.damper_type)
    TextView damper_type;


    public ShowAttributeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_attribute_dialog, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        deviceName.setText("Device Name  :"+   deviceClass.getDeviceName());
        ahu_No.setText(deviceClass.getAhuNumber());
        if (deviceClass.getAirType().equalsIgnoreCase("00")){
            air_type.setText("Supply Air");
        } else if (deviceClass.getAirType().equalsIgnoreCase("01")){
            air_type.setText("Exhaust Air");
        }else{
            air_type.setText(deviceClass.getAirType());
        }

        floor_No.setText(deviceClass.getFlourNumber());

        if (deviceClass.getDamperType().equalsIgnoreCase("00")){
            damper_type.setText("Main");
        } else if (deviceClass.getDamperType().equalsIgnoreCase("01")){
            damper_type.setText("Backend");
        } else {
            damper_type.setText(deviceClass.getDamperType());
        }

        return view;
    }


    public void setData(DeviceClass deviceData) {
        this.deviceClass = deviceData;
    }
}
