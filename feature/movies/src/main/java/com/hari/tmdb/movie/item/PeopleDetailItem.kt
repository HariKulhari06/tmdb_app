package com.hari.tmdb.movie.item

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hari.tmdb.model.People
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemPeopleDetailBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem


class PeopleDetailItem @AssistedInject constructor(
    @Assisted private val peopleData: People,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemPeopleDetailBinding>(peopleData.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_people_detail

    override fun bind(viewBinding: ItemPeopleDetailBinding, position: Int) {
        with(viewBinding) {
            people = peopleData
            textKnownAs.text = peopleData.alsoKnownAs.joinToString { it.replace("\"", "") }
            homePage.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(peopleData.homepage)
                root.context.startActivity(intent)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            peopleData: People
        ): PeopleDetailItem
    }
}