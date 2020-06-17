package com.e.firedamper.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import com.e.firedamper.PogoClasses.DeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.e.firedamper.activity.AppHelper.sqlHelper;

public class AllDevicesListAdapter extends BaseAdapter {
    Dialog pinStatus_dialog;
    Dialog choose_dialog;
    Dialog attribute_dialog;
    Activity activity;
    ArrayList<DeviceClass> arrayList;
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;

    public AllDevicesListAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
//        scannerTask=new ScannerTask(activity,this);
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
                    inflate(R.layout.all_device_adapter, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        DeviceClass deviceClass = arrayList.get(position);
        viewHolder.addDeviceUid.setText(deviceClass.getDeviceName());
        viewHolder.icon_delete.setOnClickListener(view -> {
            deleteDialog(position);

        });
        viewHolder.diPins.setOnClickListener(v -> {
//            chooseDialog(position);
            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.SET_PINS);
            intent.putExtra(Constants.PINS_DETAIL_KEY, arrayList.get(position));
            intent.putExtra("name", deviceClass.getDeviceName());
            intent.putExtra("pos", position);
            activity.startActivity(intent);
        });

        viewHolder.damper_detail.setOnClickListener(v -> {
//            @Override
//            public void onClick(View v) {
////                keyDialog();

            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.EDIT_DEVICE);
            intent.putExtra(Constants.DEVICE_DETAIL_KEY, arrayList.get(position));
            activity.startActivity(intent);

        });

        viewHolder.showAttribute.setOnClickListener(view -> {
            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.SHOW_ATTRIBUTE);
            intent.putExtra(Constants.DEVICE_DETAIL_KEY, arrayList.get(position));
            activity.startActivity(intent);


        });


        return convertView;
    }

//    private void attributeDialog(int position) {
//        selectedPosition = position;
//        DeviceClass deviceClass = arrayList.get(position);
//        final Dialog attribute_dialog = new Dialog(activity, R.style.FullScreenDialog);
//        attribute_dialog.setCanceledOnTouchOutside(true);
//        attribute_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        attribute_dialog.setContentView(R.layout.show_attribute_dialog);
//        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.95);
//        attribute_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//        attribute_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
////        attribute_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
//        attribute_dialog.show();
//
//    }

//    private void chooseDialog(int position) {
//        selectedPosition = position;
//        DeviceClass deviceClass = arrayList.get(position);
//        choose_dialog = new Dialog(activity);
//        choose_dialog.setCanceledOnTouchOutside(true);
//        choose_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        choose_dialog.setContentView(R.layout.choose_dialog);
//        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);
//        choose_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//        choose_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        Button btn_submit = choose_dialog.findViewById(R.id.btn_submit);
//        EditText damper_type = choose_dialog.findViewById(R.id.damper_type);
//        damper_type.setOnClickListener(view -> {
//            PopupMenu airPopUp = new PopupMenu(activity, damper_type);
//            airPopUp.getMenuInflater().inflate(R.menu.choose_menu, airPopUp.getMenu());
//            airPopUp.setOnMenuItemClickListener(item -> {
//                try {
//                    damper_type.setText(item.getTitle());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return true;
//            });
//            airPopUp.show();
//        });
//
//
//        btn_submit.setOnClickListener(v -> choose_dialog.dismiss());
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                choose_dialog.dismiss();
//                String val_st ;
//
//                Intent intent = new Intent(activity, HelperActivity.class);
////                String chooseKey = damper_type.getText().toString().trim();
//                intent.putExtra("choose_Key", damper_type.getText().toString().trim());
//                intent.putExtra(Constants.MAIN_KEY, Constants.SET_PINS);
//                intent.putExtra(Constants.PINS_DETAIL_KEY, arrayList.get(position));
//                activity.startActivity(intent);
//
//            }
//        });
//        choose_dialog.show();
//
//    }

    private void keyDialog() {
//        hideKeyboard();
        pinStatus_dialog = new Dialog(activity);
        pinStatus_dialog.setCanceledOnTouchOutside(true);
        pinStatus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pinStatus_dialog.setContentView(R.layout.unique_key_dialog);
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.80);
        pinStatus_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        pinStatus_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btn_submit = pinStatus_dialog.findViewById(R.id.btn_submit);
        EditText remark_et = pinStatus_dialog.findViewById(R.id.remark_et);


        btn_submit.setOnClickListener(v -> pinStatus_dialog.dismiss());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hideKeyboard();
//                     PreferencesManager.getInstance(MainActivity.this).setUniqueKey(remark_et.getText().toString().trim());
                Log.e("DI_uniqueKey>>>>>>", remark_et.getText().toString().trim());

                if (remark_et.getText().length() == 4) {
                    Intent intent = new Intent(activity, HelperActivity.class);
                    String inputKey = remark_et.getText().toString().trim();
                    String first, second, third, fourth;
                    first = inputKey.substring(0, 1);
                    second = inputKey.substring(1, 2);
                    third = inputKey.substring(2, 3);
                    fourth = inputKey.substring(3, 4);
                    Log.e("DI Status Key>>>>>>", first + "," + second + "," + third + "," + fourth);
                    intent.putExtra("Unique_Key", remark_et.getText().toString().trim());
                    intent.putExtra("first_key", first);
                    intent.putExtra("second_key", second);
                    intent.putExtra("third_key", third);
                    intent.putExtra("fourth_key", fourth);
                    intent.putExtra(Constants.MAIN_KEY, Constants.EDIT_DEVICE);
                    activity.startActivity(intent);
                    pinStatus_dialog.dismiss();
                } else {
                    Toast.makeText(activity, "Please Enter a Valid Unique Key", Toast.LENGTH_SHORT).show();
                }

            }

        });

        pinStatus_dialog.show();


    }


    void deleteDialog(int position) {
        selectedPosition = position;
        DeviceClass deviceClass = arrayList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to delete " + deviceClass.getDeviceName())
                .setTitle("Remove FDU");
        builder.setPositiveButton("delete", (dialog1, id) -> {
            dialog1.dismiss();
            if (sqlHelper.deleteDevice(deviceClass.getDeviceUID()) > 0) {
                Toast.makeText(activity, "FDU Deleted.", Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            } else
                Toast.makeText(activity, "Some Error to Delete FDU", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static class ViewHolder {

        @BindView(R.id.deviceName)
        TextView addDeviceUid;
        @BindView(R.id.show_attribute)
        Button showAttribute;
        @BindView(R.id.damper_detail)
        Button damper_detail;
        @BindView(R.id.icon_delete)
        ImageView icon_delete;
        @BindView(R.id.diPins)
        Button diPins;

        ViewHolder(View view) {

            ButterKnife.bind(this, view);

        }
    }


}


