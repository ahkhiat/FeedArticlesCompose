package com.devid_academy.feedarticlescompose.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.feedarticlescompose.R
import java.text.SimpleDateFormat
import java.util.Locale

fun getCategoryName(context: Context, categoryId: Int?): String {
    return when (categoryId) {
        1 -> context.getString(R.string.btn_sport)
        2 -> context.getString(R.string.btn_manga)
        3 -> context.getString(R.string.btn_misc)
        else -> context.getString(R.string.undefined)
    }
}

@Composable
fun getRadioButtonColors() = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.surfaceTint
)


fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val dateToTransform = inputFormat.parse(date)
    return outputFormat.format(dateToTransform!!)
}

sealed class ArticleEvent {
    data object NavigateToMainScreen: ArticleEvent()
    data class ShowSnackBar(val resId: Int): ArticleEvent()
}

sealed class AuthEvent {
    data object NavigateToMainScreen: AuthEvent()
    data class ShowSnackBar(val resId: Int): AuthEvent()
}
