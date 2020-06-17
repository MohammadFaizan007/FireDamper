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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.MyBeaconScanner;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScanningBeacon;
import com.e.firedamper.activity.AppHelper;
import com.e.firedamper.adapter.AddDeviceListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.e.firedamper.EncodeDecodeModule.RxMethodType.LIGHT_INFO;

public class AddDeviceFragment extends Fragment implements MyBeaconScanner, AdvertiseResultInterface {
    @BindView(R.id.device_list)
    ListView deviceList;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    AddDeviceListAdapter addDeviceListAdapter;
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
    public AddDeviceFragment() {
        // Required empty public constructor
    }

    private void handlerProgressar() {
        animatedProgress.showProgress();
        handler = new Handler();
        handler.postDelayed(runnable,15*1000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String key1 = getArguments().getString("KEY1");
        String key2 = getArguments().getString("KEY2");
        String key3 = getArguments().getString("KEY3");
        String key4 = getArguments().getString("KEY4");
        View view = inflater.inflate(R.layout.fragment_add_device, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = getActivity();

        addDeviceListAdapter=new AddDeviceListAdapter(activity);
        deviceList.setAdapter(addDeviceListAdapter);
        scanningBeacon=new ScanningBeacon(activity);
        scanningBeacon.setMyBeaconScanner(this);

        animatedProgress=new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);


        ByteQueue byteQueue=new ByteQueue();
        byteQueue.push(LIGHT_INFO);   //// Light Level Command method type
//        byteQueue.pushU4B(0x00);   ////deviceDetail.getGroupId()   node id;
        byteQueue.push("0"+key1);     ////0x00-0x64
        byteQueue.push("0"+key2);
        byteQueue.push("0"+key3);
        byteQueue.push("0"+key4);
        advertiseTask=new AdvertiseTask(this,activity,5*1000);
        animatedProgress.setText("FDU Devices");
        advertiseTask.setByteQueue(byteQueue);
//        advertiseTask.setAdvertiseTimeout(10*1000);
        advertiseTask.setSearchRequestCode(LIGHT_INFO);
        advertiseTask.startAdvertising();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAdvertisingFinished)
        {
            animatedProgress.setText("FDU Devices");
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
        Log.e(TAG,"FDU Devices");

    }


    @Override
    public void onFailed(String errorMessage) {
        isAdvertisingFinished=true;
        scanningBeacon.start();
        animatedProgress.setText("FDU Devices");

    }

    @Override
    public void onStop() {
        scanningBeacon.stop();
        super.onStop();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        isAdvertisingFinished=true;
        animatedProgress.setText("FDU Devices");
        scanningBeacon.start();

    }

    @Override
    public void onBeaconFound(ArrayList<BeconDeviceClass> beaconList) {
        if(addDeviceListAdapter==null)
            addDeviceListAdapter=new AddDeviceListAdapter(activity);
        addDeviceListAdapter.setArrayList(beaconList);

    }

    @Override
    public void noBeaconFound() {
//        scanningBeacon.stop();
//        noRecFound.setVisibility(View.VISIBLE);
        Toast.makeText(activity, "No Response Found.", Toast.LENGTH_SHORT).show();

        Log.w("AddDeviceFragment","No Beacon founded");
        if(!AppHelper.IS_TESTING)
            addDeviceListAdapter.clearList();


    }


}
