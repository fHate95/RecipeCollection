package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;

import java.util.List;

public class Recipe extends BaseResponse {

    @SerializedName("uri")
    private String uri;

    @SerializedName("label")
    private String label;

    @SerializedName("image")
    private String image;

    @SerializedName("source")
    private String source;

    @SerializedName("url")
    private String url;

    @SerializedName("yield")
    private int yield;

    @SerializedName("calories")
    private float calories;

    @SerializedName("totalWeight")
    private float totalWeight;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("dietLabels")
    private String[] dietLabels;

    @SerializedName("healthLabels")
    private String[] healthLabels;

//    @SerializedName("totalNutrients")
//    @Expose
//    private List<NutrientInfo> totalNutrients;
//
//    @SerializedName("totalDaily")
//    @Expose
//    private List<NutrientInfo> totalDaily;

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public int getYield() {
        return yield;
    }

    public float getCalories() {
        return calories;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }

    //    public List<NutrientInfo> getTotalNutrients() {
//        return totalNutrients;
//    }
//
//    public List<NutrientInfo> getTotalDaily() {
//        return totalDaily;
//    }

//    @SerializedName("")
//    @Expose
//    private
}
