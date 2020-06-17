package com.e.firedamper.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.ReceiverResultInterface;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.constant.Constants;
import com.e.firedamper.constant.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.e.firedamper.EncodeDecodeModule.RxMethodType.FUNCTIONALITY_INFO;

public class AddFunctionFragment extends Fragment implements AdvertiseResultInterface, ReceiverResultInterface {
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    Activity activity;
    Unbinder unbinder;
    int requestCode;
    AnimatedProgress animatedProgress;
    ScannerTask scannerTask;
    String name = "";
    int position;
    @BindView(R.id.ahu_radio)
    CheckBox ahu_radio;
    @BindView(R.id.air_radio)
    CheckBox air_radio;
    @BindView(R.id.floor_radio)
    CheckBox floor_radio;
    @BindView(R.id.damper_radio)
    CheckBox damper_radio;

    public AddFunctionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        name = getArguments().getString("name");
        position = getArguments().getInt("pos");
        Log.e("Fun", name);
        View view = inflater.inflate(R.layout.choose_functionality, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        scannerTask = new ScannerTask(activity, this);
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        return view;
    }

    public void setFunData(DeviceClass deviceData) {
        this.deviceClass = deviceData;

    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                AdvertiseTask advertiseTask = new AdvertiseTask(activity);
                ByteQueue byteQueue = new ByteQueue();
                byteQueue.push(FUNCTIONALITY_INFO);
                byteQueue.pushU4B(deviceClass.getDeviceUID());
                List<String> list = new ArrayList<>();
                if (PreferencesManager.getInstance(getContext()).getList(Constants.CHECK_TYPE) != null) {
                    list = PreferencesManager.getInstance(getContext()).getList(Constants.CHECK_TYPE);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).contains(name)) {
                            list.remove(i);
                        }
                    }
                }
                if (ahu_radio.isChecked()) {
                    byteQueue.push(0x01);
                    list.add("true" + "ahu" + name);
                } else {
                    byteQueue.push(0x00);
                    list.add("false" + "ahu" + name);
                }
                if (air_radio.isChecked()) {
                    byteQueue.push(0x01);
                    list.add("true" + "air" + name);
                } else {
                    byteQueue.push(0x00);
                    list.add("false" + "air" + name);
                }
                if (floor_radio.isChecked()) {
                    byteQueue.push(0x01);
                    list.add("true" + "floor" + name);
                } else {
                    byteQueue.push(0x00);
                    list.add("false" + "floor" + name);
                }
                if (damper_radio.isChecked()) {
                    byteQueue.push(0x01);
                    list.add("true" + "damper" + name);
                } else {
                    byteQueue.push(0x00);
                    list.add("false" + "damper" + name);
                }
                PreferencesManager.getInstance(activity).setList(Constants.CHECK_TYPE, list);

                advertiseTask = new AdvertiseTask(AddFunctionFragment.this,activity,5*1000);
                animatedProgress.setText("Uploading");
                advertiseTask.setByteQueue(byteQueue);
                Log.e("Check>>>>", byteQueue.toString());
                advertiseTask.setSearchRequestCode(FUNCTIONALITY_INFO);
                advertiseTask.startAdvertising();
                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String message) {
        animatedProgress.showProgress();
        Log.w(TAG, "Uploading");

    }

    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Uploading", Toast.LENGTH_SHORT).show();
        animatedProgress.hideProgress();
        activity.onBackPressed();
        Log.w(TAG, "onScanFailed " + errorMessage);

    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        if (animatedProgress != null)
            animatedProgress.hideProgress();
        activity.onBackPressed();
        ContentValues contentValues = new ContentValues();


    }

    @Override
    public void onScanSuccess(int successCode, ByteQueue byteQueue) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();
        activity.onBackPressed();

    }

    @Override
    public void onScanFailed(int errorCode) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();
        activity.onBackPressed();

    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
//      E/>>: [opennormalr1test, closenormalr2test, openfire1test, closefire2test]
//       E/>>>name: opennormalr1test
//       E/>>>name: closenormalr2test
//        E/>>>name: openfire1test
//     E/>>>name: closefire2test

//        [Checkahutest, UnCheckairtest, UnCheckfloortest, UnCheckdampertest]
//        E / >>> name:Checkahutest
//        E / >>> name:UnCheckairtest
//        E / >>> name:UnCheckfloortest
//        E / >>> name:UnCheckdampertest

//        if (list.get(i).contains(name)) {
//            if (list.get(i).contains("normalr1")) {
//                if (list.get(i).equalsIgnoreCase("opennormalr1" + name)) {
//                    normal_r1_open.setChecked(true);
//                    normal_r1_close.setChecked(false);
//                } else {
//                    normal_r1_open.setChecked(false);
//                    normal_r1_close.setChecked(true);
//                }
//            }

        if (PreferencesManager.getInstance(getContext()).getList(Constants.CHECK_TYPE) != null) {
            List<String> list = PreferencesManager.getInstance(getContext()).getList(Constants.CHECK_TYPE);
            Log.e(">>", list.toString());
            for (int i = 0; i < list.size(); i++) {
                Log.e(">>>name", list.get(i).toString());
                if (list.get(i).contains(name)) {
                    if (list.get(i).contains("ahu")) {
                        if (list.get(i).equalsIgnoreCase("trueahu" + name)) {
                            ahu_radio.setChecked(true);
                        } else {
                            ahu_radio.setChecked(false);
                        }
                    }
                    if (list.get(i).contains("air")) {
                        if (list.get(i).equalsIgnoreCase("trueair" + name)) {
                            air_radio.setChecked(true);
                        } else {
                            air_radio.setChecked(false);
                        }
                    }
                    if (list.get(i).contains("floor")) {
                        if (list.get(i).equalsIgnoreCase("truefloor" + name)) {
                            floor_radio.setChecked(true);
                        } else {
                            floor_radio.setChecked(false);
                        }

                    }

                    if (list.get(i).contains("damper")) {
                        if (list.get(i).equalsIgnoreCase("truedamper" + name)) {
                            damper_radio.setChecked(true);
                        } else {
                            damper_radio.setChecked(false);
                        }
                    }
                }
            }

        }

    }

    public void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
