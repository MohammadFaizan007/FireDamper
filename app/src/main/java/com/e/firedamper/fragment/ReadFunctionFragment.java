package com.e.firedamper.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.MyBeaconScanner;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScanningBeacon;
import com.e.firedamper.activity.AppHelper;
import com.e.firedamper.adapter.FunctionAdapter;
import com.e.firedamper.adapter.PinStatusAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.e.firedamper.EncodeDecodeModule.RxMethodType.FUNCTIONALITY_STATUS;
import static com.e.firedamper.EncodeDecodeModule.RxMethodType.RELAY_STATUS;

public class ReadFunctionFragment extends Fragment implements MyBeaconScanner, AdvertiseResultInterface {
    DeviceClass deviceClass;
    @BindView(R.id.diPin_status_list)
    ListView diPinStatusList;
    //    @BindView(R.id.deviceName)
//    TextView deviceName;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    FunctionAdapter functionAdapter;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<DeviceClass> deviceList;
    int movement = 150;
    ScanningBeacon scanningBeacon;
    boolean isAdvertisingFinished = false;
    AdvertiseTask advertiseTask;
    AnimatedProgress animatedProgress;
    String TAG = this.getClass().getSimpleName();
    Unbinder unbinder;
    Activity activity;

    Handler handler;
    private Runnable runnable = () -> {
        if (animatedProgress != null) {
            animatedProgress.hideProgress();
        }

    };

    public ReadFunctionFragment() {
        deviceList = new ArrayList<>();
        // Required empty public constructor
    }

    private void handlerProgressar() {
        animatedProgress.showProgress();
        handler = new Handler();
        handler.postDelayed(runnable, 10 * 1000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.read_function, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
//        deviceName.setText("Name  ="+"   "+deviceClass.getDeviceName());
        functionAdapter = new FunctionAdapter(activity);
        diPinStatusList.setAdapter(functionAdapter);
        scanningBeacon = new ScanningBeacon(activity);
        scanningBeacon.setMyBeaconScanner(this);

        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);

        ByteQueue byteQueue = new ByteQueue();
        byteQueue.push(FUNCTIONALITY_STATUS);   //// Light Level Command method type
        byteQueue.pushU4B(deviceClass.getDeviceUID());
        Log.e("Relay_status", byteQueue.toString());
        advertiseTask = new AdvertiseTask(this, activity, 5 * 1000);
        animatedProgress.setText("Scanning");
        advertiseTask.setByteQueue(byteQueue);
        advertiseTask.setSearchRequestCode(FUNCTIONALITY_STATUS);
        advertiseTask.startAdvertising();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdvertisingFinished) {
            animatedProgress.setText("Scanning");
            scanningBeacon.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanningBeacon.stop();
        if (handler != null)
            handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String message) {
        handlerProgressar();
        Log.e(TAG, "Scanning");

    }


    @Override
    public void onFailed(String errorMessage) {
        isAdvertisingFinished = true;
        scanningBeacon.start();
        animatedProgress.setText("Scanning");

    }

    @Override
    public void onStop() {
        scanningBeacon.stop();
        super.onStop();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        isAdvertisingFinished = true;
        animatedProgress.setText("Scanning");
        scanningBeacon.start();

    }

    @Override
    public void onBeaconFound(ArrayList<BeconDeviceClass> beaconList) {
        if (functionAdapter == null)
            functionAdapter = new FunctionAdapter(activity);
        functionAdapter.setArrayList(beaconList);


    }

    @Override
    public void noBeaconFound() {
//        scanningBeacon.stop();
        Log.w("AddDeviceFragment", "No Beacon founded");
        if (!AppHelper.IS_TESTING)
            functionAdapter.clearList();
        Toast.makeText(activity, "No Record Founded", Toast.LENGTH_SHORT).show();

    }


    public void setFunctionData(DeviceClass deviceData) {
        this.deviceClass = deviceData;
    }
}
