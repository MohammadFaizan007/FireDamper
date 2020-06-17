package com.e.firedamper.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.DatabaseModule.DatabaseConstant;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.InterfaceModule.AdvertiseResultInterface;
import com.e.firedamper.InterfaceModule.ReceiverResultInterface;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.R;
import com.e.firedamper.ServiceModule.AdvertiseTask;
import com.e.firedamper.ServiceModule.ScannerTask;
import com.e.firedamper.activity.AppHelper;
import com.e.firedamper.activity.HelperActivity;
import com.e.firedamper.constant.Constants;
import com.niftymodaldialogeffects.Effectstype;
import com.niftymodaldialogeffects.NiftyDialogBuilder;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.Gravity.CENTER;
import static com.e.firedamper.EncodeDecodeModule.RxMethodType.SAVE_INFO;
import static com.e.firedamper.EncodeDecodeModule.TxMethodType.GROUP_STATE_COMMAND_RESPONSE;
import static com.e.firedamper.activity.AppHelper.sqlHelper;

public class AddDeviceListAdapter extends BaseAdapter implements AdvertiseResultInterface, ReceiverResultInterface {
    Activity activity;
    ArrayList<BeconDeviceClass> arrayList;
    ScannerTask scannerTask;
    AnimatedProgress animatedProgress;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;
    int ahu_hex, flour_hex;
    boolean isAdvertisingFinished = false;


    public AddDeviceListAdapter(@NonNull Activity context) {
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.add_device_adapter, parent, false);

        }
        BeconDeviceClass beconDeviceClass = arrayList.get(position);
        ViewHolder viewHolder = new ViewHolder(convertView);

        viewHolder.viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.expand_detail.getVisibility() == View.VISIBLE) {
                    viewHolder.expand_detail.setVisibility(View.GONE);
                    viewHolder.viewDetail.setBackgroundResource(R.drawable.pluse_ic);
                } else {
                    viewHolder.expand_detail.setVisibility(View.VISIBLE);
                    viewHolder.viewDetail.setBackgroundResource(R.drawable.minus_ic);

                }
            }
        });
        if (beconDeviceClass.getSensor_ahuNo().equalsIgnoreCase("FF") && beconDeviceClass.getSensor_airtype().equalsIgnoreCase("FF")
                && beconDeviceClass.getSensor_floorNo().equalsIgnoreCase("FF") && beconDeviceClass.getSensor_damperType().equalsIgnoreCase("FF")) {
            viewHolder.addDevice.setText("Add");
            viewHolder.btn_save.setVisibility(View.GONE);

        } else {
            viewHolder.addDevice.setText("Modify");
            viewHolder.btn_save.setVisibility(View.VISIBLE);
        }
        String ahu_int = beconDeviceClass.getSensor_ahuNo();
        Integer int_ahuNo = Integer.parseInt(ahu_int, 16);

        String floor_int = beconDeviceClass.getSensor_floorNo();
        Integer int_floorNo = Integer.parseInt(floor_int, 16);

        String uid_int = beconDeviceClass.getDeviceUid();
        Integer uid = Integer.parseInt(uid_int, 16);

        Log.i("FZ>>", int_ahuNo.toString() + "," + int_floorNo.toString());
        if (beconDeviceClass.isAdded()) {
//            viewHolder.addDevice.setText("Modify");
            viewHolder.addDeviceUid.setText(beconDeviceClass.getDeviceName());
//            viewHolder.ahu_no.setText(beconDeviceClass.getAhuNumber());
//            viewHolder.air_type.setText(beconDeviceClass.getSupplyType());
//            viewHolder.floor_no.setText(beconDeviceClass.getFlourNumber());
//            viewHolder.damper_type.setText(beconDeviceClass.getDamperType());
        } else {
//            viewHolder.addDevice.setText("Add");
            viewHolder.addDeviceUid.setText(uid.toString());
        }

        if (beconDeviceClass.getSensor_ahuNo().equalsIgnoreCase("FF")) {
            viewHolder.ahu_no.setText("No Information");
        } else {
            viewHolder.ahu_no.setText(int_ahuNo.toString());
        }
        if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("FF")) {
            viewHolder.air_type.setText("No Information");
        } else if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("00")) {
            viewHolder.air_type.setText("Supply Air");
        } else if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("01")) {
            viewHolder.air_type.setText("Exhaust Air");
        }
        if (beconDeviceClass.getSensor_floorNo().equalsIgnoreCase("FF")) {
            viewHolder.floor_no.setText("No Information");
        } else {
            viewHolder.floor_no.setText(int_floorNo.toString());
        }

        if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("FF")) {
            viewHolder.damper_type.setText("No Information");
        } else if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("00")) {
            viewHolder.damper_type.setText("Main");
        } else if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("01")) {
            viewHolder.damper_type.setText("Backend");
        }
//        }



        viewHolder.btn_save.setOnClickListener(view -> {
            showNameDialog(position);
        });


        viewHolder.addDevice.setOnClickListener(view -> {
//            if(beconDeviceClass.isAdded())
//            {
//                NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(activity);
//                dialogBuilder
//                        .withTitle("ADD DEVICE")
//                        .withEffect(Effectstype.Slit)
//                        .withMessage("Device is already added")
//                        .withButton1Text("OK")
//                        .setButton1Click(v -> {
//                            dialogBuilder.dismiss();
//                        })
//                        .show();
//                return;
//            }
            showDialog(position);
//
        });

        return convertView;
    }

    public void showNameDialog(int position){
        selectedPosition = position;
        final Dialog dialog = new Dialog(activity);
        BeconDeviceClass beconDeviceClass = arrayList.get(position);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.unique_key_dialog_second);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.95);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        EditText deviceName = dialog.findViewById(R.id.deviceName);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        Button btn_submit2 = dialog.findViewById(R.id.btn_submit2);
        String dialog_uid_int = beconDeviceClass.getDeviceUid();
        Integer int_uid_dialog = Integer.parseInt(dialog_uid_int, 16);
        deviceName.setHint(int_uid_dialog.toString());
        dialog.show();

        if (beconDeviceClass.isAdded()) {
            btn_submit2.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.GONE);
        }else {
            btn_submit2.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(view -> {
            String name_st;
            name_st = deviceName.getText().toString().trim();
            if (name_st.length() == 0) {
                deviceName.setError("Please Enter Name");
                requestFocus(deviceName);
                return;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_UID, beconDeviceClass.getBeaconUID());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_NAME, deviceName.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER, beconDeviceClass.getSensor_ahuNo());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE, beconDeviceClass.getSensor_airtype());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER, beconDeviceClass.getSensor_floorNo());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE, beconDeviceClass.getSensor_damperType());
            if (sqlHelper.insertData(DatabaseConstant.ADD_DEVICE_TABLE, contentValues) <0) {
                arrayList.get(position).setAdded(true);
                dialog.dismiss();
            } else {
                arrayList.get(position).setAdded(true);
                arrayList.get(position).setDeviceName(deviceName.getText().toString());
                arrayList.get(position).setAhuNumber(beconDeviceClass.getSensor_ahuNo());
                arrayList.get(position).setSupplyType(beconDeviceClass.getSensor_airtype());
                arrayList.get(position).setFlourNumber(beconDeviceClass.getSensor_floorNo());
                arrayList.get(position).setDamperType(beconDeviceClass.getSensor_damperType());
                Toast.makeText(activity, "Device Added Successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            notifyDataSetChanged();

        });




        btn_submit2.setOnClickListener(view -> {
            String name_st;
            name_st = deviceName.getText().toString().trim();
            if (name_st.length() == 0) {
                deviceName.setError("Please Enter Name");
                requestFocus(deviceName);
                return;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_UID, beconDeviceClass.getBeaconUID());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_NAME, deviceName.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER, beconDeviceClass.getSensor_ahuNo());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE, beconDeviceClass.getSensor_airtype());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER, beconDeviceClass.getSensor_floorNo());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE, beconDeviceClass.getSensor_damperType());
            if (sqlHelper.insertData(DatabaseConstant.ADD_DEVICE_TABLE, contentValues) >0) {
                arrayList.clear();
                arrayList.get(position).setAdded(true);
                dialog.dismiss();
            } else {
                arrayList.get(position).setAdded(true);
                arrayList.get(position).setDeviceName(deviceName.getText().toString());
                arrayList.get(position).setDeviceName(deviceName.getText().toString());
                arrayList.get(position).setAhuNumber(beconDeviceClass.getSensor_ahuNo());
                arrayList.get(position).setSupplyType(beconDeviceClass.getSensor_airtype());
                arrayList.get(position).setFlourNumber(beconDeviceClass.getSensor_floorNo());
                arrayList.get(position).setDamperType(beconDeviceClass.getSensor_damperType());
                Toast.makeText(activity, "Device Added Successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            notifyDataSetChanged();

        });

    }

    public void showDialog(int position) {
        selectedPosition = position;
        final Dialog dialog = new Dialog(activity);
        BeconDeviceClass beconDeviceClass = arrayList.get(position);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fullscreendialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.95);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        EditText deviceName = dialog.findViewById(R.id.deviceName);
        EditText ahuNo = dialog.findViewById(R.id.ahuNo);
        EditText windowNo = dialog.findViewById(R.id.windowNo);
        EditText flourNo = dialog.findViewById(R.id.flourNo);
        EditText damperNO = dialog.findViewById(R.id.damperNO);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        Button btn_update = dialog.findViewById(R.id.btn_update);
//        Button btn_save = dialog.findViewById(R.id.btn_save);
        String dialog_ahu_int = beconDeviceClass.getSensor_ahuNo();
        Integer int_ahuNo_dialog = Integer.parseInt(dialog_ahu_int, 16);
        String dialog_floor_int = beconDeviceClass.getSensor_floorNo();
        Integer int_floorNo_dialog = Integer.parseInt(dialog_floor_int, 16);
        String dialog_uid_int = beconDeviceClass.getDeviceUid();
        Integer int_uid_dialog = Integer.parseInt(dialog_uid_int, 16);
        if (beconDeviceClass.isAdded()) {
            btn_submit.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
//            btn_save.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
//            btn_save.setVisibility(View.GONE);
        }
        deviceName.setHint(int_uid_dialog.toString());
        if (beconDeviceClass.getSensor_ahuNo().equalsIgnoreCase("FF")) {
            ahuNo.setHint("No Information");
        } else {
            ahuNo.setHint(int_ahuNo_dialog.toString());
        }
        if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("FF")) {
            windowNo.setHint("No Information");
        } else if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("00")) {
            windowNo.setHint("Supply Air");
        } else if (beconDeviceClass.getSensor_airtype().equalsIgnoreCase("01")) {
            windowNo.setHint("Exhaust Air");
        }

        if (beconDeviceClass.getSensor_floorNo().equalsIgnoreCase("FF")) {
            flourNo.setHint("No Information");
        } else {
            flourNo.setHint(int_floorNo_dialog.toString());
        }

        if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("FF")) {
            damperNO.setHint("No Information");
        } else if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("00")) {
            damperNO.setHint("Main");
        } else if (beconDeviceClass.getSensor_damperType().equalsIgnoreCase("01")) {
            damperNO.setHint("Backend");
        }
        dialog.show();

            windowNo.setOnClickListener(view -> {
                PopupMenu airPopUp = new PopupMenu(activity, windowNo);
                airPopUp.getMenuInflater().inflate(R.menu.air_unit, airPopUp.getMenu());
                airPopUp.setOnMenuItemClickListener(item -> {
                    try {
                        windowNo.setText(item.getTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                });
                airPopUp.show();
            });

            damperNO.setOnClickListener(view -> {
                PopupMenu damperPopUp = new PopupMenu(activity, damperNO);
                damperPopUp.getMenuInflater().inflate(R.menu.damper, damperPopUp.getMenu());
                damperPopUp.setOnMenuItemClickListener(item -> {
                    try {
                        damperNO.setText(item.getTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                });

                damperPopUp.show();
            });
//        }
        btn_submit.setOnClickListener(view -> {
            String ahu_st, flour_st, damper_st, air_st, name_st;
            name_st = deviceName.getText().toString().trim();
            ahu_st = ahuNo.getText().toString().trim();
            flour_st = flourNo.getText().toString().trim();
            damper_st = damperNO.getText().toString().trim();
            air_st = windowNo.getText().toString().trim();
//
            if (name_st.length() == 0) {
                deviceName.setError("Please Enter Name");
                requestFocus(deviceName);
                return;
            } else if (ahu_st.length() == 0) {
                ahuNo.setError("Please Enter Valid Ahu No");
                requestFocus(ahuNo);
                return;
            } else if (air_st.length() == 0) {
                windowNo.setError("Please Select Air No");
                requestFocus(windowNo);
                return;
            } else if (flour_st.length() == 0) {
                flourNo.setError("Please Enter Valid flour No");
                requestFocus(flourNo);
                return;
            } else if (damper_st.length() == 0) {
                damperNO.setError("Please Select Damper");
                requestFocus(damperNO);
                return;
            }
            try {
                String ahu = ahuNo.getText().toString().trim();
                ahu_hex = Integer.parseInt(ahu);
                String flour = flourNo.getText().toString().trim();
                flour_hex = Integer.parseInt(flour);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_UID, beconDeviceClass.getBeaconUID());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_NAME, deviceName.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER, ahuNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE, windowNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER, flourNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE, damperNO.getText().toString());


            if (sqlHelper.insertData(DatabaseConstant.ADD_DEVICE_TABLE, contentValues) < 0) {
                arrayList.get(position).setAdded(true);
                dialog.dismiss();
            } else {
                arrayList.get(position).setAdded(true);
                arrayList.get(position).setDeviceName(deviceName.getText().toString());
                arrayList.get(position).setAhuNumber(ahuNo.getText().toString());
                arrayList.get(position).setSupplyType(windowNo.getText().toString());
                arrayList.get(position).setFlourNumber(flourNo.getText().toString());
                arrayList.get(position).setDamperType(damperNO.getText().toString());
                Toast.makeText(activity, "Device Added Successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            notifyDataSetChanged();
            AdvertiseTask advertiseTask;
            ByteQueue byteQueue = new ByteQueue();
            byteQueue.push(SAVE_INFO);
            byteQueue.pushU4B(beconDeviceClass.getBeaconUID());
            byteQueue.push(ahu_hex);
            if (windowNo.getText().toString().equalsIgnoreCase("Supply Air")) {
                byteQueue.push(0x00);
            } else if (windowNo.getText().toString().equalsIgnoreCase("Exhaust Air")) {
                byteQueue.push(0x01);
            }
            byteQueue.push(flour_hex);
            if (damperNO.getText().toString().equalsIgnoreCase("Main")) {
                byteQueue.push(0x00);
            } else if (damperNO.getText().toString().equalsIgnoreCase("Backend")) {
                byteQueue.push(0x01);
            }

            advertiseTask = new AdvertiseTask(this, activity, 5 * 1000);
            animatedProgress.setText("Uploading");
            advertiseTask.setByteQueue(byteQueue);
            Log.e("Check>>>>", byteQueue.toString());
            advertiseTask.setSearchRequestCode(GROUP_STATE_COMMAND_RESPONSE);
            advertiseTask.startAdvertising();


        });

        btn_update.setOnClickListener(view -> {
            String ahu_st, flour_st, damper_st, air_st, name_st;
            name_st = deviceName.getText().toString().trim();
            ahu_st = ahuNo.getText().toString().trim();
            flour_st = flourNo.getText().toString().trim();
            damper_st = damperNO.getText().toString().trim();
            air_st = windowNo.getText().toString().trim();
//
            if (name_st.length() == 0) {
                deviceName.setError("Please Enter Name");
                requestFocus(deviceName);
                return;
            } else if (ahu_st.length() == 0) {
                ahuNo.setError("Please Enter Valid Ahu No");
                requestFocus(ahuNo);
                return;
            } else if (air_st.length() == 0) {
                windowNo.setError("Please Select Air No");
                requestFocus(windowNo);
                return;
            } else if (flour_st.length() == 0) {
                flourNo.setError("Please Enter Valid flour No");
                requestFocus(flourNo);
                return;
            } else if (damper_st.length() == 0) {
                damperNO.setError("Please Select Damper");
                requestFocus(damperNO);
                return;
            }
            try {
                String ahu = ahuNo.getText().toString().trim();
                ahu_hex = Integer.parseInt(ahu);
                String flour = flourNo.getText().toString().trim();
                flour_hex = Integer.parseInt(flour);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_UID, beconDeviceClass.getBeaconUID());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_NAME, deviceName.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER, ahuNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE, windowNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER, flourNo.getText().toString());
            contentValues.put(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE, damperNO.getText().toString());


            if (sqlHelper.insertData(DatabaseConstant.ADD_DEVICE_TABLE, contentValues) >0) {
                arrayList.clear();
                arrayList.get(position).setAdded(true);
                dialog.dismiss();
            } else {
                arrayList.get(position).setAdded(true);
                arrayList.get(position).setDeviceName(deviceName.getText().toString());
                arrayList.get(position).setAhuNumber(ahuNo.getText().toString());
                arrayList.get(position).setSupplyType(windowNo.getText().toString());
                arrayList.get(position).setFlourNumber(flourNo.getText().toString());
                arrayList.get(position).setDamperType(damperNO.getText().toString());
                Toast.makeText(activity, "Device Added Successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            notifyDataSetChanged();
            AdvertiseTask advertiseTask;
            ByteQueue byteQueue = new ByteQueue();
            byteQueue.push(SAVE_INFO);
            byteQueue.pushU4B(beconDeviceClass.getBeaconUID());
            byteQueue.push(ahu_hex);
            if (windowNo.getText().toString().equalsIgnoreCase("Supply Air")) {
                byteQueue.push(0x00);
            } else if (windowNo.getText().toString().equalsIgnoreCase("Exhaust Air")) {
                byteQueue.push(0x01);
            }
            byteQueue.push(flour_hex);
            if (damperNO.getText().toString().equalsIgnoreCase("Main")) {
                byteQueue.push(0x00);
            } else if (damperNO.getText().toString().equalsIgnoreCase("Backend")) {
                byteQueue.push(0x01);
            }

            advertiseTask = new AdvertiseTask(this, activity, 5 * 1000);
            animatedProgress.setText("Uploading");
            advertiseTask.setByteQueue(byteQueue);
            Log.e("Check>>>>", byteQueue.toString());
            advertiseTask.setSearchRequestCode(GROUP_STATE_COMMAND_RESPONSE);
            advertiseTask.startAdvertising();


        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onSuccess(String message) {
        animatedProgress.showProgress();
        Log.w(TAG, "Uploading ");
    }


    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Uploading Attribute Failed.", Toast.LENGTH_SHORT).show();
        animatedProgress.hideProgress();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        scannerTask = new ScannerTask(activity, this);
        scannerTask.setRequestCode(resultCode);
        scannerTask.start();
        isAdvertisingFinished = true;
        Log.w(TAG, "Uploading Attribute stop" + resultCode);
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
        @BindView(R.id.add_device)
        Button addDevice;
        @BindView(R.id.btn_save)
        Button btn_save;
        @BindView(R.id.viewDetail)
        ImageView viewDetail;
        @BindView(R.id.expand_detail)
        LinearLayout expand_detail;
        @BindView(R.id.add_device_uid)
        TextView addDeviceUid;
        @BindView(R.id.ahu_no)
        TextView ahu_no;
        @BindView(R.id.air_type)
        TextView air_type;
        @BindView(R.id.floor_no)
        TextView floor_no;
        @BindView(R.id.damper_type)
        TextView damper_type;
        @BindView(R.id.viewDetails)
        ConstraintLayout viewDetails;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

