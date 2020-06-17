package com.e.firedamper.constant;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class Constants {
    public static final String NORMAL_R1_RADIO_TYPE = "normal_radio_r1_type";
    public static final String NORMAL_R2_RADIO_TYPE = "normal_radio_r2_type";
    public static final String FIRE_R1_RADIO_TYPE = "fire_radio_r1_type";
    public static final String FIRE_R2_RADIO_TYPE = "fire_radio_r2_type";

    public static final String CHECK_TYPE = "check_type";


    public final static String MAIN_KEY="HelperLoad";
    public final static String PINS_DETAIL_KEY="PinDetail";
    public final static String DEVICE_DETAIL_KEY="DeviceDetail";
    public final static String READ_FUNCTION_KEY="FunctionDetail";
    public final static int MY_NETWORK_CODE=101;
    public final static int SMART_DEVICE_CODE=102;
    public final static int DASHBOARD_CODE=103;
    public final static int LARGE_SCAN_CODE=104;
    public final static int SHOW_ATTRIBUTE=105;
    public final static int EDIT_DEVICE=109;
    public final static int SET_PINS=110;
    public final static int READ_FUNCTION=111;
    public final static int ADD_FUNCTION=112;
    public final static int TEST_CODE=113;
    public static String PWM="PWM Driver Cenzer";             ///0x00
    public static String PWM_INFERRIX="PWM Driver Inferrix";  ///0x02
    public static String VCC="0-10 V Controller Cenzer";      ///0x01
    public static String VCC_INFERRIX="0-10 V Controller Inferrix";///0x03
    public static String INFERRIX_SMART_DERIVE="Inferrix Derive Type";///0x04
    public static Drawable generateBackgroundWithShadow(View view, @ColorRes int backgroundColor,
                                                        @DimenRes int cornerRadius,
                                                        @ColorRes int shadowColor,
                                                        @DimenRes int elevation,
                                                        int shadowGravity) {
        float cornerRadiusValue = view.getContext().getResources().getDimension(cornerRadius);
        int elevationValue = (int) view.getContext().getResources().getDimension(elevation);
        int shadowColorValue = ContextCompat.getColor(view.getContext(),shadowColor);
        int backgroundColorValue = ContextCompat.getColor(view.getContext(),backgroundColor);

        float[] outerRadius = {cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue};

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(cornerRadiusValue, 10, 10, 50);

        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = elevationValue;
        shapeDrawablePadding.right = elevationValue;

        int DY;
        switch (shadowGravity) {
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevationValue*2;
                shapeDrawablePadding.bottom = elevationValue;
                DY = -1*elevationValue/3;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue*2;
                DY = elevationValue/3;
                break;
        }
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);
        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadiusValue/3, 0, DY, shadowColorValue);

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevationValue, elevationValue*2, elevationValue, elevationValue*2);

        return drawable;

    }

}
