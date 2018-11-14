package com.sanechek.recipecollection.ui.fragment;

/* Интерфейс общения фрагмента с активити */
public interface FragmentListener {

    default void showHideFab() {}
    default void openCloseFab(boolean toggle) {}
    default void onPickImage(int position) {}

}
