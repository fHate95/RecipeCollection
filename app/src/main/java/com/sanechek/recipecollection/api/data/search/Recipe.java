package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;

import java.util.List;

public class Recipe extends BaseResponse {

    @SerializedName("uri")
    @Expose
    private String uri;

//    @SerializedName("")
//    @Expose
//    private

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("yield")
    @Expose
    private int yield;

    @SerializedName("calories")
    @Expose
    private float calories;

    @SerializedName("totalWeight")
    @Expose
    private float totalWeight;

    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients;

    @SerializedName("totalNutrients")
    @Expose
    private List<NutrientInfo> totalNutrients;

    @SerializedName("totalDaily")
    @Expose
    private List<NutrientInfo> totalDaily;

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

    public List<NutrientInfo> getTotalNutrients() {
        return totalNutrients;
    }

    public List<NutrientInfo> getTotalDaily() {
        return totalDaily;
    }

    //    TODO: enum serializing
//    @SerializedName("dietLabels")
//    @Expose
//    private enum dietLabels{};

//    @SerializedName("")
//    @Expose
//    private
}
