package com.e.firedamper.PogoClasses;

public class BeconDeviceClass {
    String deviceUid = "";
    String deviceName = "";
    String supplyType = "";
    String damperType = "";
    long beaconUID = 0;
    int deriveType = 0;
    String ahuNumber = "";
    String flourNumber = "";

    String sensor_ahuNo = "";
    String sensor_airtype ="";
    String sensor_floorNo = "";
    String sensor_damperType = "";

    String normal_relay1 = "";
    String normal_relay2 = "";
    String fire_relay1 = "";
    String fire_relay2 = "";
    String relay_uid = "";

    String forword_latency = "";
    String backword_latency = "";

//    int sensor_ahuNo = 0;
//    int sensor_airtype = 0;
//    int sensor_floorNo = 0;
//    int sensor_damperType = 0;
    boolean isAdded = false;

    public void setDeriveType(int deriveType) {
        this.deriveType = deriveType;
    }

    public int getDeriveType() {
        return deriveType;
    }
    public void setSensor_ahuNo(String sensor_ahuNo) {
        this.sensor_ahuNo = sensor_ahuNo;
    }

    public String getSensor_ahuNo() {
        return sensor_ahuNo;
    }


    public void setSensor_airtype(String sensor_airtype) {
        this.sensor_airtype = sensor_airtype;
    }

    public String getSensor_airtype() {
        return sensor_airtype;
    }

    public void setSensor_floorNo(String sensor_floorNo) {
        this.sensor_floorNo = sensor_floorNo;
    }

    public String getSensor_floorNo() {
        return sensor_floorNo;
    }
    public void setSensor_damperType(String sensor_damperType) {
        this.sensor_damperType = sensor_damperType;
    }

    public String getSensor_damperType() {
        return sensor_damperType;
    }



    public void setAdded(boolean added) {
        isAdded = added;
    }

    public boolean isAdded() {
        return isAdded;
    }


    public void setAhuNumber(String ahuNumber) {
        this.ahuNumber = ahuNumber;
    }

    public String getAhuNumber() {
        return ahuNumber;
    }

    public void setFlourNumber(String flourNumber) {
        this.flourNumber = flourNumber;
    }

    public String getFlourNumber() {
        return flourNumber;
    }

    public void setBeaconUID(long beaconUID) {
        this.beaconUID = beaconUID;
    }

    public long getBeaconUID() {
        return beaconUID;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getRelay_uid() {
        return relay_uid;
    }

    public void setRelay_uid(String relay_uid) {
        this.relay_uid = relay_uid;
    }


    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }


    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public String getSupplyType() {
        return supplyType;
    }


    public void setDamperType(String damperType) {
        this.damperType = damperType;
    }

    public String getDamperType() {
        return damperType;
    }

    public void setNormal_relay1(String normal_relay1) {
        this.normal_relay1 = normal_relay1;
    }

    public String getNormal_relay1() {
        return normal_relay1;
    }


    public void setNormal_relay2(String normal_relay2) {
        this.normal_relay2 = normal_relay2;
    }

    public String getNormal_relay2() {
        return normal_relay2;
    }

    public void setFire_relay1(String fire_relay1) {
        this.fire_relay1 = fire_relay1;
    }

    public String getFire_relay1() {
        return fire_relay1;
    }

    public void setFire_relay2(String fire_relay2) {
        this.fire_relay2 = fire_relay2;
    }

    public String getFire_relay2() {
        return fire_relay2;
    }

    public void setForword_latency(String forword_latency) {
        this.forword_latency = forword_latency;
    }

    public String getForword_latency() {
        return forword_latency;
    }

    public void setBackword_latency(String backword_latency) {
        this.backword_latency = backword_latency;
    }

    public String getBackword_latency() {
        return backword_latency;
    }



}
