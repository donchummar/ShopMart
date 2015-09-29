package com.nod.shopmart.app;

import android.app.Application;
import android.content.ComponentCallbacks2;

import com.nod.shopmart.utils.ImageCacheManager;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class ShoppingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(getApplicationContext());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            ImageCacheManager.INSTANCE.evictAll();
        }
    }
}
