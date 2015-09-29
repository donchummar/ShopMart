package com.nod.shopmart.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class Product {

    private String name;
    private String min_price_str;
    private String store_count;
    private String avg_rating;
    private String rating_count;


    private ArrayList<HashMap<String, String>> images = new ArrayList<>();
    private HashMap<Integer, String> sortedmap;
    private List<Integer> dimensionList;

    public String getName() {
        return name;
    }

    public ArrayList<HashMap<String, String>> getImages() {
        return images;
    }

    public String getMin_price_str() {
        return min_price_str;
    }

    public String getStore_count() {
        return store_count;
    }

    public String createDeviceSpecificImagemap() {
        HashMap<String, String> imageList = images.get(0);
        dimensionList = createInt(imageList);
        return null;
    }

    private List<Integer> createInt(HashMap<String, String> imageMap) {
        sortedmap = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (String value : imageMap.keySet()) {
            int result = Integer.parseInt(value.substring(0, value.indexOf("x")));
            list.add(result);
            sortedmap.put(result, imageMap.get(value));
        }
        Collections.sort(list,Collections.reverseOrder());
        return list;
    }

    public String getNearestResolution(int key) {
        int start = 0;
        int end = dimensionList.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (key == dimensionList.get(mid) || start == end) {
                return sortedmap.get(dimensionList.get(mid));
            }
            if (key < dimensionList.get(mid)) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return null;
    }


    public String gethighestResolution() {
        return sortedmap.get(dimensionList.get(0));
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public int getRating_count() {
        return Integer.parseInt(rating_count);
    }
}
