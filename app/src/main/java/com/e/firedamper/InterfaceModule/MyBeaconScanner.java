package com.e.firedamper.InterfaceModule;



import com.e.firedamper.PogoClasses.BeconDeviceClass;

import java.util.ArrayList;

public interface MyBeaconScanner {
    void onBeaconFound(ArrayList<BeconDeviceClass> byteQueue);
    void noBeaconFound();
}
