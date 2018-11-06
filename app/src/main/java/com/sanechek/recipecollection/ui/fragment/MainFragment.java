package com.sanechek.recipecollection.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.DishTypesAdapter;
import com.sanechek.recipecollection.api.data.city.City;
import com.sanechek.recipecollection.api.data.city.CityResponse;
import com.sanechek.recipecollection.data.DishType;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.util.DisposableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MainFragment extends BaseFragment implements ActivityListener {

    @BindView(R.id.rv_dish_types) RecyclerView rvDishTypes;

    private Realm realm;

    private DishTypesAdapter adapter;

    private final DisposableManager dManager = new DisposableManager(this);


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getDefaultInstance();

        ArrayList<DishType> dishTypes = new ArrayList<>();
        dishTypes.add(new DishType("Salads"));
        dishTypes.add(new DishType("Soups"));
        dishTypes.add(new DishType("Desserts"));
        dishTypes.add(new DishType("Chicken"));
        dishTypes.add(new DishType("Fish"));
        dishTypes.add(new DishType("Beef"));
        dishTypes.add(new DishType("Pork"));

        setRecyclerView(requireContext(), dishTypes);
    }

    private void setRecyclerView(final Context context, final ArrayList<DishType> list) {
        adapter = new DishTypesAdapter(context, list, new DishTypesAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(DishType item) {
                Toast.makeText(requireContext(), "Clicked on " + item.getName(), Toast.LENGTH_SHORT).show();

                ProgressDialog dialog = new ProgressDialog(requireActivity());
                getAppComponent(requireContext()).getApi().getCity("ru")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> dialog.show())
                        .doFinally(dialog::dismiss)
                        .doOnSuccess(MainFragment.this::onSuccess)
                        .doOnError(throwable -> Toast.makeText(context, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show())
                        .subscribe();

//                Disposable d = getAppComponent(requireContext()).getApi().search("fish", 0, 3, BuildConfig.APP_ID, BuildConfig.APP_KEY)
//                Disposable d = getAppComponent(requireContext()).getApi().getCity()
//                        .doOnSubscribe(disposable -> dialog.show())
//                        .doFinally(dialog::dismiss)
//                        .subscribe((cityResponse, throwable) -> {
//                            if (cityResponse != null) {
//                                Toast.makeText(requireContext(), "result size: " + cityResponse.getCities().size(), Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (throwable != null) {
//                                    Toast.makeText(requireContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                dManager.disposeOnPause(d);
            }
        }, realm);

        rvDishTypes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        rvDishTypes.setAdapter(adapter);
    }

    private void onSuccess(List<City> list) {
        Toast.makeText(requireContext(), "Result size: " + list.size(), Toast.LENGTH_SHORT).show();
    }


}