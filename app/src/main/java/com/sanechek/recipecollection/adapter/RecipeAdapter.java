package com.sanechek.recipecollection.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Hit> items;

    private Realm realm;
    private Context context;

    AdapterClickListener clickListener;
    public interface AdapterClickListener {
        void onItemClick(Hit item);
    }

    public RecipeAdapter(Context context, ArrayList<Hit> items, AdapterClickListener clickListener,
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
        View view = inflater.inflate(R.layout.rv_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItems(items.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.iv_photo) CircleImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bindItems(Hit item) {
            tvName.setText(item.getRecipe().getLabel());
            Glide.with(context)
                    .load(item.getRecipe().getImage())
                    .into(ivPhoto);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
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

    public ArrayList<Hit> getItems() {
        return items;
    }

    public Hit getItem(int position) {
        return items.get(position);
    }

    public void refresh(ArrayList<Hit> itemsToAdd) {
        items = new ArrayList<>();
        items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Hit> itemsToAdd) {
        items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

}
