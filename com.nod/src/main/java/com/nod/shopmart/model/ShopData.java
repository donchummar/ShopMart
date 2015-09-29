package com.nod.shopmart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class ShopData implements IDataModel {

    private ArrayList<Folders> folders = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private String prev;
    private String next;
    private String total;

    public ArrayList<Folders> getFolders() {
        return folders;
    }

    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void createImagemap() {
        for (Product product : products){
            product.createDeviceSpecificImagemap();
        }
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
