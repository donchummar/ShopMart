package com.nod.shopmart.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Don Chummar on 9/26/2015.
 */
public class Images implements IDataModel {

    @SerializedName("40x40")
    private String pic40x40;

    @SerializedName("180x180")
    private String pic180x180;

    @SerializedName("75x75")
    private String pic75x75;

    @SerializedName("125x125")
    private String pic125x125;

    @SerializedName("360x360")
    private String pic360x360;

    public String getPic180x180() {
        return pic180x180;
    }

    public String getPic360x360() {
        return pic360x360;
    }

    public String getPic75x75() {
        return pic75x75;
    }

    public String getPic40x40() {
        return pic40x40;
    }

    public String getPic125x125() {
        return pic125x125;
    }
}
