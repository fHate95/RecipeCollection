package com.sanechek.recipecollection.ui.activity;

/* Callback interface (Общение фрагментов с активити) */
public interface ActivityListener {

    default void onReadStoragePermissionGranted() {}
    default void onBackgroundImagePicked(String uri, int position) {}

}
