package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;

public class Hit {

    @SerializedName("recipe")
    private Recipe recipe;

    @SerializedName("bookmarked")
    private boolean bookmarked;

    @SerializedName("bought")
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
