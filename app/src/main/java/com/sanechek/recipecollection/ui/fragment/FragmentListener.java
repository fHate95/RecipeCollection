package com.sanechek.recipecollection.ui.fragment;

import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.Menu;

import java.util.ArrayList;
import java.util.List;

/* Интерфейс общения фрагмента с активити */
public interface FragmentListener {

    default void setRefreshMenuItemVisibility(boolean visible) { }
    default List<Menu> getMenu() {
        return new ArrayList<>();
    }
    default void showMainFragment() {};

}
