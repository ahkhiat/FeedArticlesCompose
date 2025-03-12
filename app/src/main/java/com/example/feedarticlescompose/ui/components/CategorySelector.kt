package com.example.feedarticlescompose.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor

@Composable
fun CategorySelector(
    selectedValue: Int,
    onCategorySelected: (Int) -> Unit,
    context: Context
) {
    val categories = listOf(
        1 to context.getString(R.string.btn_sport),
        2 to context.getString(R.string.btn_manga),
        3 to context.getString(R.string.btn_misc)
    )
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        categories.forEach { (value, label) ->
            RadioButton(
                selected = selectedValue == value,
                onClick = { onCategorySelected(value) },
                colors = RadioButtonDefaults.colors(FeedArticlesColor)
            )
            Text(
                text = label,
                modifier = Modifier
                    .clickable { onCategorySelected(value) }
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}