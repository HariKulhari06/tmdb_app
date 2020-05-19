package com.hari.tmdb.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hari.tmdb.model.LoadingState
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ListItemLoadingBinding

class LoadingStateAdapter(val retry: () -> Unit) :
    RecyclerView.Adapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    private var loadingState: LoadingState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingStateViewHolder {
        return LoadingStateViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_loading,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_loading
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, position: Int) {
        loadingState?.let { state ->
            holder.bind(state, retry)
        }
    }


    class LoadingStateViewHolder(val binding: ListItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadingState: LoadingState, retry: () -> Unit) {
            with(binding) {
                state = loadingState
                retryButton.setOnClickListener { retry.invoke() }
            }
        }
    }

    fun hasExtraRow() = loadingState != null && loadingState != LoadingState.Loading

    override fun getItemCount(): Int {
        return if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: LoadingState?) {
        val previousState = this.loadingState
        val hadExtraRow = hasExtraRow()
        this.loadingState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

}