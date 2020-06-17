package com.e.firedamper.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.DatabaseModule.DatabaseConstant;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.EncodeDecodeModule.RxMethodType;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.ReceiverResultInterface;
import com.e.firedamper.MainActivity;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;
import com.e.firedamper.constant.PreferencesManager;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.e.firedamper.EncodeDecodeModule.RxMethodType.DI_INFO;
import static com.e.firedamper.EncodeDecodeModule.TxMethodType.LIGHT_STATE_COMMAND_RESPONSE;
import static com.e.firedamper.activity.AppHelper.sqlHelper;

public class DiPinFragment extends Fragment implements AdvertiseResultInterface, ReceiverResultInterface {
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    Activity activity;
    Unbinder unbinder;
    int requestCode;
    AnimatedProgress animatedProgress;
    ScannerTask scannerTask;
    @BindView(R.id.damper_type)
    TextView damperType;
    @BindView(R.id.normal_r1_status)
    RadioGroup normal_r1_status;
    @BindView(R.id.normal_r2_status)
    RadioGroup normal_r2_status;
    @BindView(R.id.fire_r1_status)
    RadioGroup fire_r1_status;
    @BindView(R.id.fire_r2_status)
    RadioGroup fire_r2_status;
    @BindView(R.id.normal_r1_close)
    RadioButton normal_r1_close;
    @BindView(R.id.normal_r1_open)
    RadioButton normal_r1_open;
    @BindView(R.id.normal_r2_close)
    RadioButton normal_r2_close;
    @BindView(R.id.normal_r2_open)
    RadioButton normal_r2_open;
    @BindView(R.id.fire_r1_close)
    RadioButton fire_r1_close;
    @BindView(R.id.fire_r1_open)
    RadioButton fire_r1_open;
    @BindView(R.id.fire_r2_close)
    RadioButton fire_r2_close;
    @BindView(R.id.fire_r2_open)
    RadioButton fire_r2_open;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    String name = "";
    int position;


    public DiPinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        name = getArguments().getString("name");
        position = getArguments().getInt("pos");
        Log.e("DiFragment", name);
        View view = inflater.inflate(R.layout.fragment_di_pin, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        scannerTask = new ScannerTask(activity, this);
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        return view;
    }


    public void setPinData(DeviceClass deviceData) {
        this.deviceClass = deviceData;

    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                AdvertiseTask advertiseTask = new AdvertiseTask(activity);
                ByteQueue byteQueue = new ByteQueue();
                byteQueue.push(DI_INFO);
                byteQueue.pushU4B(deviceClass.getDeviceUID());
                int selectedNormalR1 = normal_r1_status.getCheckedRadioButtonId();
                int selectedNormalR2 = normal_r2_status.getCheckedRadioButtonId();

                List<String> list = new ArrayList<>();
                if (PreferencesManager.getInstance(getContext()).getList(Constants.NORMAL_R1_RADIO_TYPE) != null) {
                    list = PreferencesManager.getInstance(getContext()).getList(Constants.NORMAL_R1_RADIO_TYPE);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).contains(name)) {
                            list.remove(i);
                        }
                    }
                }

                if (normal_r1_close.isChecked()) {
                    list.add("close" + "normalr1" + name);
//                    PreferencesManager.getInstance(getContext()).setNormalR1RadioType("close");
                } else if (normal_r1_open.isChecked()) {
                    list.add("open" + "normalr1" + name);
//                    PreferencesManager.getInstance(getContext()).setNormalR1RadioType("open");
                }
                if (normal_r2_close.isChecked()) {
                    list.add("close" + "normalr2" + name);
//                    PreferencesManager.getInstance(getContext()).setNormalR2RadioType("close");
                } else if (normal_r2_open.isChecked()) {
                    list.add("open" + "normalr2" + name);
//                    PreferencesManager.getInstance(getContext()).setNormalR2RadioType("open");
                }

                if (fire_r1_close.isChecked()) {
                    list.add("close" + "fire1" + name);
//                    PreferencesManager.getInstance(getContext()).setFireR1RadioType("close");
                } else if (fire_r1_open.isChecked()) {
                    list.add("open" + "fire1" + name);
//                    PreferencesManager.getInstance(getContext()).setFireR1RadioType("open");
                }
                if (fire_r2_close.isChecked()) {
                    list.add("close" + "fire2" + name);
//                    PreferencesManager.getInstance(getContext()).setFireR2RadioType("close");
                } else if (fire_r2_open.isChecked()) {
                    list.add("open" + "fire2" + name);
//                    PreferencesManager.getInstance(getContext()).setFireR2RadioType("open");
                }
                PreferencesManager.getInstance(getContext()).setList(Constants.NORMAL_R1_RADIO_TYPE, list);


                if (selectedNormalR1 == normal_r1_close.getId()) {
                    byteQueue.push(0x00);

                } else if (selectedNormalR1 == normal_r1_open.getId()) {
                    byteQueue.push(0x01);
                }
                else{
                    byteQueue.push(0x00);
                }
                if (selectedNormalR2 == normal_r2_close.getId()) {
                    byteQueue.push(0x00);
                } else if (selectedNormalR2 == normal_r2_open.getId()) {
                    byteQueue.push(0x01);
                }
                else{
                    byteQueue.push(0x00);
                }
                int selectedFireR1 = fire_r1_status.getCheckedRadioButtonId();
                int selectedFireR2 = fire_r2_status.getCheckedRadioButtonId();
                if (selectedFireR1 == fire_r1_close.getId()) {
                    byteQueue.push(0x00);
                } else if (selectedFireR1 == fire_r1_open.getId()) {
                    byteQueue.push(0x01);
                }
                else{
                    byteQueue.push(0x00);
                }
                if (selectedFireR2 == fire_r2_close.getId()) {
                    byteQueue.push(0x00);
                } else if (selectedFireR2 == fire_r2_open.getId()) {
                    byteQueue.push(0x01);
                }
                else{
                    byteQueue.push(0x00);
                }
//                advertiseTask = new AdvertiseTask(DiPinFragment.this, activity);
                advertiseTask=new AdvertiseTask(DiPinFragment.this,activity,5*1000);
                animatedProgress.setText("Uploading");
                advertiseTask.setByteQueue(byteQueue);
                advertiseTask.setSearchRequestCode(LIGHT_STATE_COMMAND_RESPONSE);
                Log.e("DIPIN>>>>", byteQueue.toString());
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
        Log.w(TAG, "Uploading Relay");

    }

    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Advertising failed.", Toast.LENGTH_SHORT).show();
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

//        try {

        //      E/>>: [opennormalr1test, closenormalr2test, openfire1test, closefire2test]
//       E/>>>name: opennormalr1test
//       E/>>>name: closenormalr2test
//        E/>>>name: openfire1test
//     E/>>>name: closefire2test

        if (PreferencesManager.getInstance(getContext()).getList(Constants.NORMAL_R1_RADIO_TYPE) != null) {
            List<String> list = PreferencesManager.getInstance(getContext()).getList(Constants.NORMAL_R1_RADIO_TYPE);
            Log.e(">>", list.toString());
            for (int i = 0; i < list.size(); i++) {
                Log.e(">>>name", list.get(i).toString());
                if (list.get(i).contains(name)) {
                    if (list.get(i).contains("normalr1")) {
                        if (list.get(i).equalsIgnoreCase("opennormalr1" + name)) {
                            normal_r1_open.setChecked(true);
                            normal_r1_close.setChecked(false);
                        } else {
                            normal_r1_open.setChecked(false);
                            normal_r1_close.setChecked(true);
                        }
                    }
                    if (list.get(i).contains("normalr2")) {
                        if (list.get(i).equalsIgnoreCase("opennormalr2" + name)) {
                            normal_r2_open.setChecked(true);
                            normal_r2_close.setChecked(false);
                        } else {
                            normal_r2_open.setChecked(false);
                            normal_r2_close.setChecked(true);
                        }
                    }
                    if (list.get(i).contains("fire1")) {
                        if (list.get(i).equalsIgnoreCase("openfire1" + name)) {
                            fire_r1_open.setChecked(true);
                            fire_r1_close.setChecked(false);
                        } else {
                            fire_r1_open.setChecked(false);
                            fire_r1_close.setChecked(true);
                        }
                    }
                    if (list.get(i).contains("fire2")) {
                        if (list.get(i).equalsIgnoreCase("openfire2" + name)) {
                            fire_r2_open.setChecked(true);
                            fire_r2_close.setChecked(false);
                        } else {
                            fire_r2_open.setChecked(false);
                            fire_r2_close.setChecked(true);
                        }
                    }
                }
            }
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

