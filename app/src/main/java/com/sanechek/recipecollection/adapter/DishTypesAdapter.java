package com.sanechek.recipecollection.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.DishType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DishTypesAdapter extends RecyclerView.Adapter<DishTypesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<DishType> items;

    private Realm realm;
    private Context context;

    AdapterClickListener clickListener;
    public interface AdapterClickListener {
        void onItemClick(DishType item);
    }

    public DishTypesAdapter(Context context, ArrayList<DishType> items, AdapterClickListener clickListener,
                            Realm realm) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.realm = realm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_dish_type_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItems(items.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_view) CardView itemView;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.iv_background) AppCompatImageView ivBackground;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }


        void bindItems(DishType item) {
            tvName.setText(item.getName());

            Glide.with(context)
                    .load(item.getImageId())
                    .into(ivBackground);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_view:
                    clickListener.onItemClick(items.get(getAdapterPosition()));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<DishType> getItems() {
        return items;
    }

    public DishType getItem(int position) {
        return items.get(position);
    }

    public void addItem(DishType item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(ArrayList<DishType> itemsToAdd) {
        items = new ArrayList<>();
        items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

}
