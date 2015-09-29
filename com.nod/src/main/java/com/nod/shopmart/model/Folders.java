package com.nod.shopmart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don Chummar on 9/24/2015.
 */
public class Folders implements IDataModel {

    private String uri;
    private String name;
    private List<Facets> facets = new ArrayList<>();

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public List<Facets> getFacets() {
        return facets;
    }
}
