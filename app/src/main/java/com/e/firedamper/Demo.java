package com.e.firedamper;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.ReceiverModule.BLEBroadcastReceiver;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.niftymodaldialogeffects.Effectstype;
import com.niftymodaldialogeffects.NiftyDialogBuilder;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Demo extends AppCompatActivity {


//    MaterialButtonToggleGroup btnToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

//        btnToggle = findViewById(R.id.toggleGroup);
//
//        btnToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
//
//            }
//        });
//
    }
}