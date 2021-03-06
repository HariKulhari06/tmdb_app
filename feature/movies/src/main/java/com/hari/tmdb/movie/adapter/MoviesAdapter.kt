package com.hari.tmdb.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.MovieItemBinding
import com.hari.tmdb.movie.ui.MainMovieFragmentDirections.Companion.actionMoviesToMovieDetail

class MoviesAdapter : PagedListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(
    diffCallbacks
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.movie_item,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_item
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
        }

    }

    class MoviesViewHolder(private val movieItemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(movieItemBinding.root) {
        fun bind(movie: Movie) {
            with(movieItemBinding) {
                this.movie = movie
                imageViewPoster.setOnClickListener {
                    val extra = FragmentNavigatorExtras(
                        imageViewPoster to imageViewPoster.transitionName
                    )
                    root.findNavController().navigate(
                        actionMoviesToMovieDetail(
                            movieId = movie.id,
                            title = movie.title
                        ),
                        extra
                    )
                }
                imageViewPoster.transitionName = movie.id.toString()
            }
        }
    }


    companion object {
        val diffCallbacks = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }
    }


}