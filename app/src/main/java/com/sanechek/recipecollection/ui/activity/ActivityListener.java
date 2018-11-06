package com.sanechek.recipecollection.ui.activity;

/* Callback interface (from activity to fragment) */
public interface ActivityListener {

    default void onReadStoragePermissionGranted() {}
    default void onBackgroundImagePicked(String uri, int position) {}

}
