package com.hari.tmdb.movie.item

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import coil.transform.CircleCropTransformation
import com.hari.tmdb.model.People
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemPeopleDetailBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder


class PeopleDetailItem @AssistedInject constructor(
    @Assisted private val peopleData: People,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemPeopleDetailBinding>(peopleData.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    override fun getLayout(): Int = R.layout.item_people_detail

    override fun bind(viewBinding: ItemPeopleDetailBinding, position: Int) {
        with(viewBinding) {
            people = peopleData
            textKnownAs.text = peopleData.alsoKnownAs.joinToString { it.replace("\"", "") }
            imageRequestDisposables.clear()

            imageRequestDisposables += Coil.load(
                profileImage.context,
                "https://image.tmdb.org/t/p/w500/${peopleData.profilePath}"
            ) {
                crossfade(true)
                placeholder(R.drawable.placeholder_72dp)
                transformations(CircleCropTransformation())
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    profileImage.setImageDrawable(it)
                }
            }

            homePage.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(peopleData.homepage)
                root.context.startActivity(intent)
            }

        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemPeopleDetailBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            peopleData: People
        ): PeopleDetailItem
    }
}