package com.sanechek.recipecollection.ui.fragment;

/* Callback interface (from fragment to activity) */
public interface FragmentListener {

    default void showHideFab() {}
    default void openCloseFab(boolean toggle) {}
    default void onPickImage(int position) {}

}
