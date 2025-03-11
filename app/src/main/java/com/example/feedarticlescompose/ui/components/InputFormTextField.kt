package com.devid_academy.feedarticlescompose.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFormTextField(
        value: String,
        onValueChange: (String) -> Unit,
        maxLines: Int? = null,
        label: String,
        visualTransformation: Boolean? = false,
        modifier: Modifier = Modifier,
        singleLine: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = maxLines ?: 1,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White),
        visualTransformation = if(visualTransformation!!) PasswordVisualTransformation()
                                    else VisualTransformation.None,
        placeholder = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        singleLine = singleLine
    )
}

@Preview
@Composable
fun InputPreview() {
    val textValue = remember { mutableStateOf("demo") }

    InputFormTextField(
        value = textValue.value,
        onValueChange = { textValue.value = it },
        label = "demo"
    )
}