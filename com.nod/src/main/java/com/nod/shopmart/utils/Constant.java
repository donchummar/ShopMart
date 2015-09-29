package com.nod.shopmart.utils;

import java.util.HashMap;

/**
 * Created by Don Chummar on 9/25/2015.
 */
public class Constant {


    public static final String LDPI_IMAGE = "40x40";
    public static final String MDPI_IMAGE = "75x75";
    public static final String HDPI_IMAGE = "125x125";
    public static final String XHDPI_IMAGE = "180x180";
    public static final String XXHDPI_IMAGE = "360x360";

    public static final HashMap<Double, Integer> imageResoList = new HashMap<>();
    static {
        imageResoList.put(.75,40);
        imageResoList.put(1.0,75);
        imageResoList.put(1.5,125);
        imageResoList.put(2.0,180);
        imageResoList.put(3.0,360);
    }

    public static final String BASE_URL = "http://api.buyingiq.com/v1/search/?";
    public static final String LAUNCH_URL = "http://api.buyingiq.com/v1/search/?tags=mobiles&facet=1&page=1";
}
