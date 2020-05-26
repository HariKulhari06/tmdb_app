package com.hari.tmdb.groupie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnAsyncUpdateListener;

import java.util.ArrayList;
import java.util.List;

public class PagedGroupAdapter<T, VH extends GroupieViewHolder> extends GroupAdapter<VH> {

    @NonNull
    private final ItemCreator creator;
    @NonNull
    private final AsyncPagedListDiffer<T> differ;
    @NonNull
    private final List<Item> itemList = new ArrayList<>();
    private boolean detectMoves;
    @Nullable
    private OnAsyncUpdateListener onAsyncUpdateListener;

    public PagedGroupAdapter(@NonNull final ItemCreator creator,
                             @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this.creator = creator;

        AsyncDifferConfig<T> config = new AsyncDifferConfig.Builder<>(diffCallback).build();
        ListUpdateCallback listUpdateCallback = new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                for (int counter = 0; counter < count; counter++) {
                    T item = differ.getItem(counter + position);
                    if (item != null) {
                        itemList.add(counter + position, creator.create(item));
                    }
                }

                updatePagedList();
            }

            @Override
            public void onRemoved(int position, int count) {
                for (int counter = 0; counter < count; counter++) {
                    itemList.remove(position);
                }

                updatePagedList();
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                Item item = itemList.remove(fromPosition);
                itemList.add(toPosition, item);

                updatePagedList();
            }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                for (int counter = 0; counter < count; counter++) {
                    T item = differ.getItem(counter + position);
                    if (item != null) {
                        itemList.set(counter + position, creator.create(item));
                    }
                }

                updatePagedList();
            }
        };

        differ = new AsyncPagedListDiffer<>(listUpdateCallback, config);
    }

    @SuppressWarnings("unused")
    public void submitPagedList(@NonNull final PagedList<T> pagedList) {
        submitPagedList(pagedList, true, null);
    }

    @SuppressWarnings("unused")
    public void submitPagedList(@NonNull final PagedList<T> pagedList,
                                @Nullable final OnAsyncUpdateListener onAsyncUpdateListener) {
        submitPagedList(pagedList, true, onAsyncUpdateListener);
    }

    @SuppressWarnings("unused")
    public void submitPagedList(@NonNull final PagedList<T> pagedList,
                                final boolean detectMoves,
                                @Nullable final OnAsyncUpdateListener onAsyncUpdateListener) {
        AsyncPagedListDiffer.PagedListListener<T> updateListener = new AsyncPagedListDiffer.PagedListListener<T>() {
            @Override
            public void onCurrentListChanged(@Nullable PagedList<T> previousList,
                                             @Nullable PagedList<T> currentList) {
                if (currentList != null) {
                    int count = currentList.size();
                    for (int counter = 0; counter < count; counter++) {
                        T item = currentList.get(counter);
                        if (item != null) {
                            itemList.add(counter, creator.create(item));
                        }
                    }
                }

                updatePagedList();
                differ.removePagedListListener(this);
            }
        };
        differ.addPagedListListener(updateListener);

        this.differ.submitList(pagedList);
        this.detectMoves = detectMoves;
        this.onAsyncUpdateListener = onAsyncUpdateListener;
    }

    @NonNull
    @Override
    public Item getItem(int position) {
        // notify differ to scroll
        differ.getItem(position);

        return super.getItem(position);
    }

    private void updatePagedList() {
        updateAsync(itemList, detectMoves, onAsyncUpdateListener);
    }

    public interface ItemCreator<T extends Item> {
        @NonNull
        T create(@NonNull Object item);
    }
}