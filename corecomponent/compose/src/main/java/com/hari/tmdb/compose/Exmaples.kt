package com.hari.tmdb.compose

import androidx.compose.Composable
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeightIn
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp

class Exmaples {


    @Composable
    fun textView() {
        Column(modifier = Modifier.padding(10.dp)) {
            val imageModifier = Modifier
                .preferredHeightIn(maxHeight = 180.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(5.dp))
            Image(
                imageResource(id = R.drawable.ic_baseline_android_24),
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
            Text(text = "Hari Singh Kulhari", style = MaterialTheme.typography.h6)
            Text(text = "Ranosys", style = MaterialTheme.typography.body2)
            Text(text = "Android Developer", style = MaterialTheme.typography.body2)
        }

    }

    @Preview
    @Composable
    fun perviewText() {
        textView()
    }


}