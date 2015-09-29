package com.nod.shopmart.model;

import java.io.Serializable;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class Facets implements IDataModel {

    private String label;
    private String tag;
    private String count;
    private boolean is_selected;

    public String getLabel() {
        return label;
    }

    public String getTag() {
        return tag;
    }

    public String getCount() {
        return count;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setSelected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
