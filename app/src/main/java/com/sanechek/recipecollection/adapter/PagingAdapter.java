package com.sanechek.recipecollection.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.DataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class PagingAdapter extends PagedListAdapter<Hit, PagingAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private AdapterClickListener clickListener;
    public interface AdapterClickListener {
        void onItemClick(Hit item, int position);
    }

    public PagingAdapter(@NonNull DiffUtil.ItemCallback<Hit> diffCallback, Context context,
                         AdapterClickListener clickListener) {
        super(diffCallback);
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.rv_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItems(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.iv_photo) CircleImageView ivPhoto;
        @BindView(R.id.iv_favorite) AppCompatImageView ivFavorite;

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
            if (DataHelper.getFavoriteById(Realm.getDefaultInstance(), item.getRecipe().getUri()) != null) {
                ivFavorite.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.item_view:
                    clickListener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                    break;
            }
        }
    }
}
