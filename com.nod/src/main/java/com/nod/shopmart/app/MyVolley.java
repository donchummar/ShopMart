
package com.nod.shopmart.app;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.nod.shopmart.utils.ImageCacheManager;


public class MyVolley {
	
	private static RequestQueue mRequestQueue;
	private static RequestQueue mImageRequestQueue;
	
	
	private MyVolley() {}
	
	/**
	 * initialize Volley
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context,false);
		mImageRequestQueue = Volley.newRequestQueue(context, true);
		ImageCacheManager.INSTANCE.initImageCache();
	}
	
	public static RequestQueue getRequestQueue(Context context) {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			return mRequestQueue = Volley.newRequestQueue(context,false);
		}
	}
	
	public static RequestQueue getImageRequestQueue() {

		if (mImageRequestQueue != null) {
			return mImageRequestQueue;
		} else {
			throw new IllegalStateException("Image RequestQueue not initialized");
		}
	}
}
