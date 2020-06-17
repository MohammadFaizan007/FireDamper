package com.e.firedamper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.e.firedamper.R;
import com.e.firedamper.constant.Constants;
import com.e.firedamper.fragment.AddDeviceFragment;
import com.e.firedamper.fragment.AddFunctionFragment;
import com.e.firedamper.fragment.AllDeviceListFragment;
import com.e.firedamper.fragment.DiPinFragment;
import com.e.firedamper.fragment.EditDeviceFragment;
import com.e.firedamper.fragment.LatencyTestFragment;
import com.e.firedamper.fragment.ReArrangeAttributeFragment;
import com.e.firedamper.fragment.ReadFunctionFragment;
import com.e.firedamper.fragment.ShowAttributeFragment;
import com.e.firedamper.fragment.TestFragment;

import butterknife.BindView;

public class HelperActivity extends AppCompatActivity /*implements BeaconConsumer, RangeNotifier*/ {
    Fragment fragment;
    int fragmentLoadCode;
    private static String TAG = "MyActivity";
    @BindView(R.id.title)
    TextView title_tv;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//
        }
        setContentView(R.layout.activity_helper);
        Intent intent = getIntent();
        String Test = intent.getStringExtra("Unique_Key");
        String key1 = intent.getStringExtra("first_key");
        String key2 = intent.getStringExtra("second_key");
        String key3 = intent.getStringExtra("third_key");
        String key4 = intent.getStringExtra("fourth_key");
        String choose_key = intent.getStringExtra("choose_Key");
        String name = intent.getStringExtra("name");
        String ahu = intent.getStringExtra("ahu");
        String air = intent.getStringExtra("air");
        String floor = intent.getStringExtra("floor");
        String damper = intent.getStringExtra("deamper");
        String uid = intent.getStringExtra("uid");
        int pos = intent.getIntExtra("pos", 0);
        if (intent == null) {
            finish();
            return;
        }
        if (intent.hasExtra(Constants.MAIN_KEY)) {
            switch (intent.getIntExtra(Constants.MAIN_KEY, 0)) {
                case Constants.MY_NETWORK_CODE:
                    setTitle("All Device");
                    loadFragment(new AllDeviceListFragment());
                    break;

                case Constants.SMART_DEVICE_CODE:
                    AddDeviceFragment addDeviceFragment = new AddDeviceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY1", key1);
                    bundle.putString("KEY2", key2);
                    bundle.putString("KEY3", key3);
                    bundle.putString("KEY4", key4);
                    addDeviceFragment.setArguments(bundle);
                    setTitle("Damper Device");
                    loadFragment(addDeviceFragment);
                    break;

                case Constants.LARGE_SCAN_CODE:
                    LatencyTestFragment latencyTestFragment = new LatencyTestFragment();
                    Bundle fz = new Bundle();
                    fz.putString("KEY1", key1);
                    fz.putString("KEY2", key2);
                    fz.putString("KEY3", key3);
                    fz.putString("KEY4", key4);
                    latencyTestFragment.setArguments(fz);
                    setTitle("Latency Test");
                    loadFragment(latencyTestFragment);
                    break;

                case Constants.DASHBOARD_CODE:
                    setTitle("Functionality");
                    loadFragment(new ReArrangeAttributeFragment());
                    break;
//
                case Constants.SHOW_ATTRIBUTE:
                    ShowAttributeFragment showAttributeFragment = new ShowAttributeFragment();
                    showAttributeFragment.setData(intent.getParcelableExtra(Constants.DEVICE_DETAIL_KEY));
                    loadFragment(showAttributeFragment);
                    setTitle("Attribute");
//                    loadFragment(new ShowAttributeFragment());
                    break;
//
                case Constants.SET_PINS:
                    DiPinFragment diPinFragment = new DiPinFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("choose_key", choose_key);
                    bundle2.putString("name", name);
                    bundle2.putInt("pos", pos);
                    diPinFragment.setPinData(intent.getParcelableExtra(Constants.PINS_DETAIL_KEY));
                    diPinFragment.setArguments(bundle2);
                    setTitle("Relays Details");
                    loadFragment(diPinFragment);
                    break;

                case Constants.EDIT_DEVICE:
                    EditDeviceFragment editDeviceFragment = new EditDeviceFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("KEY1", key1);
                    bundle1.putString("KEY2", key2);
                    bundle1.putString("KEY3", key3);
                    bundle1.putString("KEY4", key4);
                    editDeviceFragment.setRelayData(intent.getParcelableExtra(Constants.DEVICE_DETAIL_KEY));
                    editDeviceFragment.setArguments(bundle1);
                    setTitle("Relay Status");
                    loadFragment(editDeviceFragment);
                    break;
                case Constants.TEST_CODE:
                    TestFragment testFragment = new TestFragment();
                    testFragment.setTestData(intent.getParcelableExtra(Constants.DEVICE_DETAIL_KEY));
                    setTitle("Test Result");
                    loadFragment(testFragment);
                    break;
                case Constants.READ_FUNCTION:
                    ReadFunctionFragment readFunctionFragment = new ReadFunctionFragment();
                    readFunctionFragment.setFunctionData(intent.getParcelableExtra(Constants.DEVICE_DETAIL_KEY));
                    setTitle("Functionality Status");
                    loadFragment(readFunctionFragment);
                    break;
//
                case Constants.ADD_FUNCTION:
                    AddFunctionFragment addFunctionFragment = new AddFunctionFragment();
                    setTitle("ADD Functionality");
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("name", name);
                    bundle3.putInt("pos", pos);
                    addFunctionFragment.setFunData(intent.getParcelableExtra(Constants.DEVICE_DETAIL_KEY));
                    addFunctionFragment.setArguments(bundle3);
                    loadFragment(addFunctionFragment);

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        menu.findItem(R.id.miProfile).setIcon(resizeImage(R.drawable.inferrixlogo,108,100));
        return true;
    }

//    private Drawable resizeImage(int resId, int w, int h) {
//        // load the origial Bitmap
//        Bitmap BitmapOrg = BitmapFactory.decodeResource(getResources(), resId);
//        int width = BitmapOrg.getWidth();
//        int height = BitmapOrg.getHeight();
//        int newWidth = w;
//        int newHeight = h;
//        // calculate the scale
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // create a matrix for the manipulation
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,width, height, matrix, true);
//        return new BitmapDrawable(resizedBitmap);
//    }


    public void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getSimpleName();
        Log.w("LoadFragment", backStateName + " " + fragment.getClass().getName());
        this.fragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.main_frame_layout, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commitAllowingStateLoss();
//            return;
        } else {
            for (int i = manager.getBackStackEntryCount() - 1; i >= 0; i--) {
                Log.w("ClassName", manager.getBackStackEntryAt(i).getName());
                if (!manager.getBackStackEntryAt(i).getName().equalsIgnoreCase(backStateName)) {
                    Log.w("ClassName", manager.getBackStackEntryAt(i).getName());
                    manager.popBackStack();
                } else {
                    manager.popBackStack();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment, backStateName);
                    fragmentTransaction.addToBackStack(backStateName);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
                }
//                if (i==0)
            }
        }


    }


}
