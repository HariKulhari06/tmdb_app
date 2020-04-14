package com.hari.tmdb.movie.item;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hari.tmdb.movie.R;
import com.hari.tmdb.movie.databinding.ItemCarouselBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.databinding.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;


/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
public class CarouselItem extends BindableItem<ItemCarouselBinding> {

    private GroupAdapter adapter;
    private RecyclerView.ItemDecoration carouselDecoration;

    CarouselItem(RecyclerView.ItemDecoration itemDecoration, GroupAdapter adapter) {
        this.carouselDecoration = itemDecoration;
        this.adapter = adapter;
    }

    @NotNull
    @Override
    public GroupieViewHolder<ItemCarouselBinding> createViewHolder(@NonNull View itemView) {
        GroupieViewHolder<ItemCarouselBinding> viewHolder = super.createViewHolder(itemView);
        RecyclerView recyclerView = viewHolder.binding.recyclerView;
        recyclerView.addItemDecoration(carouselDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        return viewHolder;
    }

    @Override
    public void bind(@NonNull ItemCarouselBinding viewBinding, int position) {
        viewBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_carousel;
    }

}
