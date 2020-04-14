package com.hari.tmdb.movie.item;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.Group;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupDataObserver;
import com.xwray.groupie.Item;

/**
 * A group that contains a single carousel item and is empty when the carousel is empty
 **/
public class CarouselGroup implements Group {

    private boolean isEmpty;
    private GroupDataObserver groupDataObserver;
    private CarouselItem carouselItem;

    public CarouselGroup(RecyclerView.ItemDecoration itemDecoration, GroupAdapter adapter) {
        carouselItem = new CarouselItem(itemDecoration, adapter);
        isEmpty = adapter.getItemCount() == 0;
        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                boolean empty = adapter.getItemCount() == 0;
                if (empty && !isEmpty) {
                    isEmpty = true;
                    groupDataObserver.onItemRemoved(carouselItem, 0);
                }
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                boolean empty = adapter.getItemCount() == 0;
                if (isEmpty && !empty) {
                    isEmpty = false;
                    groupDataObserver.onItemInserted(carouselItem, 0);
                }
            }
        };
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public int getItemCount() {
        return isEmpty ? 0 : 1;
    }

    @NonNull
    @Override
    public Item getItem(int position) {
        if (position == 0 && !isEmpty) return carouselItem;
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public int getPosition(@NonNull Item item) {
        return item == carouselItem && !isEmpty ? 0 : -1;
    }

    @Override
    public void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        this.groupDataObserver = groupDataObserver;
    }

    @Override
    public void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        this.groupDataObserver = null;
    }
}
