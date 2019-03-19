package com.sanechek.recipecollection.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.DataHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

/* Адапет списка рецептов */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Hit> items;
    private Context context;

    /* Листенер кликов */
    private AdapterClickListener clickListener;
    public interface AdapterClickListener {
        void onItemClick(Hit item, int position);
    }

    public RecipeAdapter(Context context, AdapterClickListener clickListener) {
        this.items = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItems(items.get(position));
    }

    /* ViewHolder устанавливает view для элемента, имплиментит View.OnClickListener для обработки кликов */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.iv_photo) CircleImageView ivPhoto;
        @BindView(R.id.rv_favorite) RelativeLayout ivFavorite;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        /* Заполнение views */
        private void bindItems(Hit item) {
            tvName.setText(item.getRecipe().getLabel());
            Glide.with(context)
                    .load(item.getRecipe().getImage())
                    .into(ivPhoto);
            if (DataHelper.getFavoriteById(Realm.getDefaultInstance(), item.getRecipe().getUri()) != null) {
                ivFavorite.setVisibility(View.VISIBLE);
            } else {
                ivFavorite.setVisibility(View.INVISIBLE);
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_view:
                    clickListener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    /* Получение элемента по позиции */
    public Hit getItem(int position) {
        return items.get(position);
    }
    /* Обновление списка */
    public void updateItems(List<Hit> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }
    /* Очистка списка */
    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

}
