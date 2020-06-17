package com.e.firedamper.ServiceModule;

import android.app.Activity;
import android.database.Cursor;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.e.firedamper.DatabaseModule.DatabaseConstant;
import com.e.firedamper.EncodeDecodeModule.ArrayUtilities;
import com.e.firedamper.EncodeDecodeModule.ByteQueue;
import com.e.firedamper.EncodeDecodeModule.MyBase64;
import com.e.firedamper.InterfaceModule.MyBeaconScanner;
import com.e.firedamper.PogoClasses.BeconDeviceClass;
import com.e.firedamper.activity.AppHelper;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class ScanningBeacon implements RangeNotifier {
    MyBeaconScanner myBeaconScanner;
    boolean mAllowRebind;
    ArrayList<BeconDeviceClass> arrayList;
    public static final int SCAN_SUCCESS_CODE = 200;
    public static final int SCAN_FAIL_CODE = 201;
    public static final int SCANNING_TIMEOUT = 15 * 1000;
    public static final String EDDYSTONE_URL_LAYOUT = "s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-21v";
    private BeaconManager mBeaconManager;
    Activity activity;
    String TAG = "ScanningBeacon";
    int scanPeriod = 500;
    int request = 0x4f;
    int resultCode = SCAN_FAIL_CODE;
    String url = "rx";
    Handler handler;
    ByteQueue byteQueue;
    AnimatedProgress animatedProgress;
    Region region;
    private Runnable runnable = this::stop;


    public ScanningBeacon(Activity activity) {

        BeaconManager.setDebug(true);
        BeaconManager.setAndroidLScanningDisabled(true);
        mBeaconManager = BeaconManager.getInstanceForApplication(activity);
        this.activity = activity;
        animatedProgress = new AnimatedProgress(activity);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_URL_LAYOUT));

        mBeaconManager.setBackgroundBetweenScanPeriod(scanPeriod);
        mBeaconManager.setForegroundBetweenScanPeriod(scanPeriod);
        mBeaconManager.setBackgroundScanPeriod(scanPeriod);
        mBeaconManager.setForegroundScanPeriod(scanPeriod);
        mBeaconManager.setBackgroundMode(false);
//            mBeaconManager.setBackgroundBetweenScanPeriod(0);
//            mBeaconManager.setBackgroundScanPeriod(1100);
//            mBeaconManager.setForegroundBetweenScanPeriod(0l);
        try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//            mBeaconManager.se
        arrayList = new ArrayList<>();

    }

    public void setMyBeaconScanner(MyBeaconScanner myBeaconScanner) {
        this.myBeaconScanner = myBeaconScanner;
    }

    public MyBeaconScanner getMyBeaconScanner() {
        return myBeaconScanner;
    }

    public void start() {

//    animatedProgress.showProgress();
        region = new Region("all-beacons-region", null, null, null);
        try {
//                Log.w(TAG, "onBeaconServiceConnect try");
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.w(TAG, "onBeaconServiceConnect catch" + e.getMessage());
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
        handler();
    }

    public void startWithHandler() {
//    animatedProgress.showProgress();
        region = new Region("all-beacons-region", null, null, null);
        try {
//                Log.w(TAG, "onBeaconServiceConnect try");
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.w(TAG, "onBeaconServiceConnect catch" + e.getMessage());
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
        handler();
    }

    public void stop() {
//            arrayList.clear();
        if (region != null) {
            try {
                mBeaconManager.stopRangingBeaconsInRegion(region);
                mBeaconManager.stopMonitoringBeaconsInRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.w(TAG, "stopping error" + e.toString());
            }
        }
        mBeaconManager.removeAllRangeNotifiers();
        if (handler != null)
            handler.removeCallbacks(runnable);
//    animatedProgress.hideProgress();


    }

    public void setRequestCode(int request) {
        this.request = request;
    }

    public int getRequest() {
        return request;
    }


//        From the discussion here, and especially this answer, this is the function I currently use:

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.w(TAG, "didRangeBeaconsInRegion" + beacons.size());
        for (Beacon beacon : beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
                byte[] bytes = beacon.getId1().toByteArray();
                byte ONE = bytes[0];
                Log.w("Byte", ONE + "");
                String receivedString = null;
                receivedString = new String(bytes, 0, bytes.length, StandardCharsets.US_ASCII);

                Log.e(TAG, "I just received: " + receivedString);

                if (receivedString.toLowerCase().contains("tx")) {
                    String[] splitUrl = receivedString.split("tx");
                    if (splitUrl.length > 1) {
                        byte[] encodeId1 = MyBase64.decode(splitUrl[1]);
                        ByteQueue byteQueue1 = new ByteQueue(encodeId1);
                        byteQueue1.push(encodeId1);
                        ByteQueue byteQueue2 = new ByteQueue(encodeId1);
                        byteQueue2.push(encodeId1);
                        ByteQueue byteQueue3 = new ByteQueue(encodeId1);
                        byteQueue3.push(encodeId1);
                        int methodType = byteQueue1.pop();
                        Log.e("MethodType", methodType + "");
                        if (methodType == 0x4f) {
                            byte[] bytes1 = byteQueue1.pop4B();
                            ArrayUtilities.reverse(bytes1);
                            String nodeUid = bytesToHex(bytes1);

                            byte[] bytes2 = byteQueue1.pop4B();
                            ArrayUtilities.reverse(bytes2);
                            String fullAddress = bytesToHex(bytes2);
                            Log.e("Faizan", fullAddress);

                            byte[] bytes3 = byteQueue1.pop9B();
                            ArrayUtilities.reverse(bytes3);
                            String faizan = bytesToHex(bytes3);
                            Log.e("FZ>>", faizan);

                            String ahuNo = "", airType = "", floorNo = "", damperType = "";
                            ahuNo = fullAddress.substring(6, 8);
                            Log.e("Ahuno", ahuNo + "");
                            airType = fullAddress.substring(4, 6);
                            Log.e("AirType", airType);
                            floorNo = fullAddress.substring(2, 4);
                            Log.e("FloorNo", floorNo);
                            damperType = fullAddress.substring(0, 2);
                            Log.e("DamperType", damperType);


                            BigInteger bi = new BigInteger(nodeUid, 16);
                            Log.e("Scann", bi + "");
                            Log.e("ScanningBeacon", nodeUid + ",");
                            BeconDeviceClass beconDeviceClass = new BeconDeviceClass();
                            beconDeviceClass.setBeaconUID(bi.longValue());
                            beconDeviceClass.setDeviceUid(nodeUid);
                            beconDeviceClass.setSensor_ahuNo(ahuNo);
                            beconDeviceClass.setSensor_airtype(airType);
                            beconDeviceClass.setSensor_floorNo(floorNo);
                            beconDeviceClass.setSensor_damperType(damperType);
                            if (!hasBeacon(beconDeviceClass)) {
                                Cursor cursor = AppHelper.sqlHelper.getLightDetails(beconDeviceClass.getBeaconUID());
                                if (cursor != null && cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    String beconName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_NAME));
                                    String ahuNumber = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER));
                                    String air = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE));
                                    String floor = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER));
                                    String damper = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE));

                                    Log.e("BeaconName", beconName + ",");
                                    beconDeviceClass.setDeviceName(beconName);
                                    beconDeviceClass.setAhuNumber(ahuNumber);
                                    beconDeviceClass.setSupplyType(air);
                                    beconDeviceClass.setFlourNumber(floor);
                                    beconDeviceClass.setDamperType(damper);
                                    beconDeviceClass.setAdded(true);
//
                                }
                                arrayList.add(beconDeviceClass);
                            }
                            //                        00DAE335,00,01,00,00,
//                        FF00000100DAE3355F,DAE3355F,01,00,00,FF,
                        } else {

                            if (methodType == 0x51 || methodType == 0x52) {
                                byte[] bytes1 = byteQueue1.pop4B();
                                ArrayUtilities.reverse(bytes1);
                                String relayUid = bytesToHex(bytes1);
//                                String nodeUid = secondUid.substring(0, secondUid.length() - 2);
                                Log.e("SecondTime_UID", relayUid);

                                BigInteger vi = new BigInteger(relayUid, 16);
                                Log.e("Scann", vi + "");

                                byte[] bytes2 = byteQueue1.pop4B();
                                ArrayUtilities.reverse(bytes2);
                                String fullAddress = bytesToHex(bytes2);
                                Log.e("Faizan", fullAddress);
//
                                String ahuNo = "", airType = "", floorNo = "", damperType = "";

                                String normalR1 = "", normalR2 = "", fireR1 = "", fireR2 = "";
                                normalR1 = fullAddress.substring(6, 8);
                                Log.e("Ahuno", normalR1 + "");
                                normalR2 = fullAddress.substring(4, 6);
                                Log.e("AirType", normalR2);
                                fireR1 = fullAddress.substring(2, 4);
                                Log.e("FloorNo", fireR1);
                                fireR2 = fullAddress.substring(0, 2);
                                Log.e("DamperType", fireR2);
                                Log.e("Attribute", fullAddress + "," + ahuNo + "," + airType + "," + floorNo + "," + damperType + ",");

                                BeconDeviceClass beconDeviceClass = new BeconDeviceClass();
                                beconDeviceClass.setNormal_relay1(normalR1);
                                beconDeviceClass.setNormal_relay2(normalR2);
                                beconDeviceClass.setFire_relay1(fireR1);
                                beconDeviceClass.setFire_relay2(fireR2);
//                                beconDeviceClass.setBeaconUID(vi.longValue());
                                beconDeviceClass.setRelay_uid(relayUid);
//                                beconDeviceClass.setSensor_ahuNo(ahuNo);
//                                beconDeviceClass.setSensor_airtype(airType);
//                                beconDeviceClass.setSensor_floorNo(floorNo);
//                                beconDeviceClass.setSensor_damperType(damperType);

                                if (!hasBeacon(beconDeviceClass)) {
                                    Cursor cursor = AppHelper.sqlHelper.getLightDetails(beconDeviceClass.getBeaconUID());
                                    if (cursor != null && cursor.getCount() > 0) {
                                        cursor.moveToFirst();
                                        String beconName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_NAME));

                                        Log.e("BeaconName", beconName + ",");
                                        beconDeviceClass.setDeviceName(beconName);

                                        beconDeviceClass.setAdded(true);

                                    }
                                    arrayList.add(beconDeviceClass);
                                }
                            } else {
                                if (methodType == 0x53) {
                                    byte[] bytes1 = byteQueue1.pop6B();
                                    ArrayUtilities.reverse(bytes1);
                                    String fullbytes = bytesToHex(bytes1);
//                                    120A00DAE335
                                    Log.e("Test_UID", fullbytes);

                                    byte[] bytes2=byteQueue2.pop4B();
                                    ArrayUtilities.reverse(bytes2);
                                    String testUid=bytesToHex(bytes2);

                                    BigInteger vi = new BigInteger(testUid, 16);
                                    Log.e("ScannTEST", vi + "");


                                    String fuullAdreess = fullbytes;

                                    String forwordLatency = "", backwordLatency = "";
                                    forwordLatency = fuullAdreess.substring(2, 4);
                                    Log.e("FLatency", forwordLatency);
                                    backwordLatency = fuullAdreess.substring(0, 2);
                                    Log.e("BLatency", backwordLatency);
                                    BeconDeviceClass beconDeviceClass = new BeconDeviceClass();
                                    beconDeviceClass.setForword_latency(forwordLatency);
                                    beconDeviceClass.setBackword_latency(backwordLatency);

                                    if (!hasBeacon(beconDeviceClass)) {
                                        Cursor cursor = AppHelper.sqlHelper.getLightDetails(beconDeviceClass.getBeaconUID());
                                        if (cursor != null && cursor.getCount() > 0) {
                                            cursor.moveToFirst();
                                            String beconName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_NAME));

                                            Log.e("BeaconName", beconName + ",");
                                            beconDeviceClass.setDeviceName(beconName);

                                            beconDeviceClass.setAdded(true);

                                        }
                                        arrayList.add(beconDeviceClass);
                                    }


                                }
                            }

                        }


                    }


                }
            }
        }
        if (myBeaconScanner == null)
            return;
        if (arrayList.size() < 1) {
            myBeaconScanner.noBeaconFound();
            return;
        }
        myBeaconScanner.onBeaconFound(arrayList);

    }

    private void handler() {
        handler = new Handler();

        handler.postDelayed(runnable, SCANNING_TIMEOUT);
    }

    boolean hasBeacon(BeconDeviceClass beconDeviceClass) {
        int i = 0;
        for (BeconDeviceClass beconDeviceClass1 : arrayList) {
            if (beconDeviceClass1.getBeaconUID() == beconDeviceClass.getBeaconUID()) {
                Log.w("Has", "hash");
                return true;
            } else
                i++;

        }
        return i != arrayList.size();

    }
}



