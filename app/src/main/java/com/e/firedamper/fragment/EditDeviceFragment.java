package com.e.firedamper.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.e.firedamper.adapter.PinStatusAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.e.firedamper.EncodeDecodeModule.RxMethodType.RELAY_STATUS;

public class EditDeviceFragment extends Fragment implements MyBeaconScanner, AdvertiseResultInterface {
    DeviceClass deviceClass;
    @BindView(R.id.diPin_status_list)
    ListView diPinStatusList;
//    @BindView(R.id.deviceName)
//    TextView deviceName;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    PinStatusAdapter pinStatusAdapter;
    ArrayAdapter<CharSequence> adapter;
    int movement=150;
    ScanningBeacon scanningBeacon;
    boolean isAdvertisingFinished=false;
    AdvertiseTask advertiseTask;
    AnimatedProgress animatedProgress;
    String TAG=this.getClass().getSimpleName();
    Unbinder unbinder;
    Activity activity;

    Handler handler ;
    private Runnable runnable= () -> {
        if(animatedProgress!=null)
        {
            animatedProgress.hideProgress();
        }

    };
    public EditDeviceFragment() {
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
        View view = inflater.inflate(R.layout.device_detail, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
//        deviceName.setText("Name  ="+"   "+deviceClass.getDeviceName());
        pinStatusAdapter=new PinStatusAdapter(activity);
        diPinStatusList.setAdapter(pinStatusAdapter);
        scanningBeacon=new ScanningBeacon(activity);
        scanningBeacon.setMyBeaconScanner(this);

        animatedProgress=new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);

//        Toast.makeText(getActivity(),  PreferencesManager.getInstance(activity).getUniqueKey(), Toast.LENGTH_SHORT).show();

        ByteQueue byteQueue=new ByteQueue();
        byteQueue.push(RELAY_STATUS);   //// Light Level Command method type
        byteQueue.pushU4B(deviceClass.getDeviceUID());
        Log.e("Relay_status",byteQueue.toString());
        advertiseTask=new AdvertiseTask(this,activity,5*1000);
        animatedProgress.setText("Scanning");
        advertiseTask.setByteQueue(byteQueue);
        advertiseTask.setSearchRequestCode(RELAY_STATUS);
        advertiseTask.startAdvertising();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAdvertisingFinished)
        {
            animatedProgress.setText("Scanning");
            scanningBeacon.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanningBeacon.stop();
        if(handler!=null)
            handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String message) {
        handlerProgressar();
        Log.e(TAG,"Scan Relay");

    }


    @Override
    public void onFailed(String errorMessage) {
        isAdvertisingFinished=true;
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
        isAdvertisingFinished=true;
        animatedProgress.setText("Scanning");
        scanningBeacon.start();

    }

    @Override
    public void onBeaconFound(ArrayList<BeconDeviceClass> beaconList) {
        if(pinStatusAdapter==null)
            pinStatusAdapter=new PinStatusAdapter(activity);
        pinStatusAdapter.setArrayList(beaconList);

    }

    @Override
    public void noBeaconFound() {
//        scanningBeacon.stop();
//        noRecFound.setVisibility(View.VISIBLE);
//        Toast.makeText(activity, "No Response Found.", Toast.LENGTH_LONG).show();
        Log.w("AddDeviceFragment","No Beacon founded");
        if(!AppHelper.IS_TESTING)
            pinStatusAdapter.clearList();
        Toast.makeText(activity, "No Response Found.", Toast.LENGTH_SHORT).show();

    }


    public void setRelayData(DeviceClass deviceData) {
        this.deviceClass = deviceData;
    }
}
