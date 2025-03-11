package com.devid_academy.feedarticlescompose.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.MaterialTheme
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

//
//fun returnCreateCategoryInt(selectedResId: Int): Int {
//    return when (selectedResId) {
//        R.id.create_radio_btn1 -> 1
//        R.id.create_radio_btn2 -> 2
//        R.id.create_radio_btn3 -> 3
//        else -> -1
//    }
//}
//
//fun returnEditCategoryInt(selectedResId: Int): Int {
//    return when (selectedResId) {
//        R.id.edit_radio_btn1 -> 1
//        R.id.edit_radio_btn2 -> 2
//        R.id.edit_radio_btn3 -> 3
//        else -> -1
//    }
//}



fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val dateToTransform = inputFormat.parse(date)
    return outputFormat.format(dateToTransform!!)
}

fun makeToast(context: Context ,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

