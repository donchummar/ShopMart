package com.nod.shopmart.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Don Chummar on 9/25/2015.
 */
public class UrlBuilder {

    public static ArrayList<String> mUrlList = new ArrayList<>();
    public static String mQueryString = "mobiles";

    public static void addTag(String tag) {
        if (!mUrlList.contains(tag))
            mUrlList.add(tag);
    }

    public static void removeTag(String tag) {
        if (mUrlList.contains(tag))
            mUrlList.remove(tag);

    }

    public static void clear() {
        if (mUrlList.size() > 0) {
            mUrlList.clear();
        }
    }

    public static int getSelectedFilterCount() {
        return mUrlList.size();
    }

    public static String createUrl(boolean isNextPageLoad, String nextUrl) {
        if ((mUrlList == null || mUrlList.size() == 0) && !isNextPageLoad) return Constant.LAUNCH_URL;
        StringBuilder url = new StringBuilder();
        url.append(Constant.BASE_URL);
        if (!isNextPageLoad) {
            url.append("tags=" + mQueryString);
            for (String tag : mUrlList) {
                url.append("&tags").append("=" + tag);
            }
            url.append("&page=1");
            url.append("&facet=1");
        } else {
            url.append(nextUrl);
        }
        return url.toString();
    }

    public static String createSearchUrl(String query) {
        if (!TextUtils.isEmpty(query)) {
            mQueryString = query;
            clear();
            StringBuilder url = new StringBuilder();
            url.append(Constant.BASE_URL);
            url.append("tags=" + query);
            url.append("&page=1");
            url.append("&facet=1");
            return url.toString();
        }
        return null;
    }
}
