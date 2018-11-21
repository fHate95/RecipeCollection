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
import com.sanechek.recipecollection.data.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Favorite> items;

    private Context context;

    AdapterClickListener clickListener;
    public interface AdapterClickListener {
        void onItemClick(Favorite item);
    }

    public FavoriteAdapter(Context context, ArrayList<Favorite> items, AdapterClickListener clickListener) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
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

        void bindItems(Favorite item) {
            tvName.setText(item.getLabel());
            Glide.with(context)
                    .load(item.getImage())
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

    public ArrayList<Favorite> getItems() {
        return items;
    }

    public Favorite getItem(int position) {
        return items.get(position);
    }

    public void refresh(ArrayList<Favorite> itemsToAdd) {
        items = new ArrayList<>();
        items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Favorite> itemsToAdd) {
        items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

}
