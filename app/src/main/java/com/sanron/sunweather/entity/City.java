package com.sanron.sunweather.entity;

import java.io.Serializable;

/**
 * 城市
 */
public class City implements Serializable {
    /**
     */
    private static final long serialVersionUID = 1L;
    private City parent;
    private String id;
    private String name;

    public City getParent() {
        return parent;
    }

    public void setParent(City parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City)) {
            return false;
        }
        return id.equals(((City) o).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
