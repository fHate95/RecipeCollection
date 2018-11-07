package com.sanechek.recipecollection.data;

public class DishType {

    private String name;
    private String query;

    public DishType() {

    }

    public DishType(String name, String query) {
        this.name = name;
        this.query = query;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
