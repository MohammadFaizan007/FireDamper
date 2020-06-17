package com.e.firedamper.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.DatabaseModule.DatabaseConstant;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.ReceiverResultInterface;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;
import com.e.firedamper.constant.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.Gravity.CENTER;
import static com.e.firedamper.EncodeDecodeModule.RxMethodType.FUNCTIONALITY_INFO;
import static com.e.firedamper.EncodeDecodeModule.RxMethodType.SAVE_INFO;
import static com.e.firedamper.EncodeDecodeModule.TxMethodType.GROUP_STATE_COMMAND_RESPONSE;
import static com.e.firedamper.activity.AppHelper.sqlHelper;

public class ReArrangeAttributeAdapter extends BaseAdapter implements AdvertiseResultInterface, ReceiverResultInterface {
    Activity activity;
    ArrayList<DeviceClass> arrayList;
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;
    int ahu_hex, flour_hex;
    ScannerTask scannerTask;
    AnimatedProgress animatedProgress;
    boolean isAdvertisingFinished = false;
    String name = "";

    public ReArrangeAttributeAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        scannerTask = new ScannerTask(activity, this);
//        animatedProgress=new AnimatedProgress(activity);
//        animatedProgress.setCancelable(false);

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
                    inflate(R.layout.rearrange_adapter, parent, false);
        }


        ViewHolder viewHolder = new ViewHolder(convertView);
        DeviceClass deviceClass = arrayList.get(position);
        name=deviceClass.getDeviceName();



        viewHolder.addDeviceUid.setText(deviceClass.getDeviceName());
        viewHolder.add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HelperActivity.class);
                intent.putExtra(Constants.MAIN_KEY, Constants.ADD_FUNCTION);
                intent.putExtra(Constants.DEVICE_DETAIL_KEY, arrayList.get(position));
                intent.putExtra("name", deviceClass.getDeviceName());
                intent.putExtra("pos", position);
                activity.startActivity(intent);
//                showDialog(position);

            }
        });


        viewHolder.readFunctionality.setOnClickListener(v -> {
            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.READ_FUNCTION);
            intent.putExtra(Constants.DEVICE_DETAIL_KEY, arrayList.get(position));
            activity.startActivity(intent);

        });
        return convertView;
    }

    public void showDialog(int position) {
        selectedPosition = position;
        final Dialog dialog = new Dialog(activity, R.style.FullScreenDialog);
        DeviceClass deviceClass = arrayList.get(position);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.95);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(CENTER);
        dialog.setContentView(R.layout.choose_functionality);
        CheckBox ahuRadio = dialog.findViewById(R.id.ahu_radio);
        CheckBox airRadio = dialog.findViewById(R.id.air_radio);
        CheckBox floorRadio = dialog.findViewById(R.id.floor_radio);
        CheckBox damperRadio = dialog.findViewById(R.id.damper_radio);
//        RadioGroup radioGroup1 = dialog.findViewById(R.id.radio_group1);
//        RadioGroup radioGroup2 = dialog.findViewById(R.id.radio_group2);
//        RadioGroup radioGroup3 = dialog.findViewById(R.id.radio_group3);
//        RadioGroup radioGroup4 = dialog.findViewById(R.id.radio_group4);

        if (PreferencesManager.getInstance(activity).getList(Constants.CHECK_TYPE) != null) {
            List<String> list = PreferencesManager.getInstance(activity).getList(Constants.CHECK_TYPE);
            Log.e(">>", list.toString());
            for (int i = 0; i < list.size(); i++) {
                Log.e(">>>name", list.get(i).toString());
                if (list.get(i).contains(name)) {
                    if (list.get(i).contains("Check"))
                    if (list.get(i).equalsIgnoreCase("Checkahu" + name)) {
//                        ahuRadio.setChecked(true);
                        ahuRadio.setEnabled(true);
                    }else {
                        ahuRadio.setEnabled(false);
//                        ahuRadio.setChecked(false);


                    }
                }
            }

        }

        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
//        activity.onBackPressed();
        btn_submit.setOnClickListener(view -> {
            dialog.dismiss();
            AdvertiseTask advertiseTask;
            ByteQueue byteQueue = new ByteQueue();
            byteQueue.push(FUNCTIONALITY_INFO);
            byteQueue.pushU4B(deviceClass.getDeviceUID());
            List<String> list = new ArrayList<>();
            if (PreferencesManager.getInstance(activity).getList(Constants.CHECK_TYPE) != null) {
                list = PreferencesManager.getInstance(activity).getList(Constants.CHECK_TYPE);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).contains(name)) {
                        list.remove(i);
                    }
                }
            }
            if (ahuRadio.isChecked()) {
                byteQueue.push(0x01);
                list.add("Check"+"ahu"+name);
            }else {
                byteQueue.push(0x00);
                list.add("UnCheck"+"ahu"+name);
            }
            if (airRadio.isChecked()) {
                byteQueue.push(0x01);
                list.add("Check"+"air"+name);
            }else {
                byteQueue.push(0x00);
                list.add("UnCheck"+"air"+name);
            }
            if (floorRadio.isChecked()) {
                byteQueue.push(0x01);
                list.add("Check"+"floor"+name);
            }else {
                byteQueue.push(0x00);
                list.add("UnCheck"+"floor"+name);
            }
            if (damperRadio.isChecked()) {
                byteQueue.push(0x01);
                list.add("Check"+"damper"+name);
            }else {
                byteQueue.push(0x00);
                list.add("UnCheck"+"damper"+name);
                PreferencesManager.getInstance(activity).setList(Constants.CHECK_TYPE, list);
            }
//            int selsctRadio = radioGroup1.getCheckedRadioButtonId();
//            if (selsctRadio == ahuRadio.getId()) {
//                byteQueue.push(0x01);
//            } else {
//                byteQueue.push(0x00);
//            }
//            int selsctRadio2 = radioGroup2.getCheckedRadioButtonId();
//            if (selsctRadio2 == airRadio.getId()) {
//                byteQueue.push(0x01);
//            } else {
//                byteQueue.push(0x00);
//            }
//            int selsctRadio3 = radioGroup3.getCheckedRadioButtonId();
//            if (selsctRadio3 == floorRadio.getId()) {
//                byteQueue.push(0x01);
//            } else {
//                byteQueue.push(0x00);
//            }
//            int selsctRadio4 = radioGroup4.getCheckedRadioButtonId();
//            if (selsctRadio4 == damperRadio.getId()) {
//                byteQueue.push(0x01);
//            } else {
//                byteQueue.push(0x00);
//            }

            advertiseTask = new AdvertiseTask(this, activity, 2 * 1000);
            animatedProgress.setText("Advertising");
            advertiseTask.setByteQueue(byteQueue);
            Log.e("Check>>>>", byteQueue.toString());
            advertiseTask.setSearchRequestCode(FUNCTIONALITY_INFO);
            advertiseTask.startAdvertising();


        });
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

        @BindView(R.id.add_device_uid)
        TextView addDeviceUid;
        @BindView(R.id.read_func)
        Button readFunctionality;
        //            @BindView(R.id.icon_delete)
//            ImageView icon_delete;
        @BindView(R.id.add_device)
        Button add_device;

        ViewHolder(View view) {

            ButterKnife.bind(this, view);

        }
    }
}





