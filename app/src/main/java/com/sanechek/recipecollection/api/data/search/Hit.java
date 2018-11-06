package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;

    @SerializedName("bookmarked")
    @Expose
    private boolean bookmarked;

    @SerializedName("bought")
    @Expose
    private boolean bought;

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public boolean isBought() {
        return bought;
    }
}
