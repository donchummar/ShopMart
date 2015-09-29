package com.nod.shopmart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nod.shopmart.BuildConfig;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class AppUtils {

    public static int getOSVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager ConnectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectMgr == null)
            return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if (NetInfo == null)
            return false;

        return NetInfo.isConnected();
    }

    public static void log(String tag, String inMsg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(inMsg)) return;
        if (BuildConfig.DEBUG) {
            Log.d(tag, inMsg);
        }
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int height = 0;
        if (activity != null && !activity.isFinishing()) {
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);

            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                height = displayMetrics.heightPixels;
            } else {
                height = displayMetrics.widthPixels;
            }
            return height;
        }
        return height;
    }


    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = 0;
        if (activity != null && !activity.isFinishing()) {
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                width = displayMetrics.widthPixels;
            } else {
                width = displayMetrics.heightPixels;
            }
        }
        return width;
    }

    public static float getDeviceDensityRatio(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (float) dm.densityDpi / DisplayMetrics.DENSITY_MEDIUM;
    }

    public static void zoomInImage(ImageView imageView) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                int imgWidth = imageView.getWidth();
                int imgHeight = imageView.getHeight();
                if (imgWidth > 0) {
                    imageView.setPivotX(imgWidth / 2);
                }
                if (imgHeight > 0) {
                    imageView.setPivotY(imgHeight / 2);
                }
                imageView.setAlpha(0f);
                imageView.setScaleX(0.8f);
                imageView.setScaleY(0.8f);
                imageView.setVisibility(View.VISIBLE);
                imageView.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .setInterpolator(new LinearInterpolator());
            } else {
                imageView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
