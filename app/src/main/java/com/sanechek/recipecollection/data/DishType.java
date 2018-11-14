package com.sanechek.recipecollection.data;

/* Элемент меню (вид блюд).
* Запрос к api выполняется по параметру query данного элемента */
public class DishType {

    private String name;
    private String query;
    private int imageId;

    public DishType() {

    }

    public DishType(String name, String query, int imageId) {
        this.name = name;
        this.query = query;
        this.imageId = imageId;
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

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
