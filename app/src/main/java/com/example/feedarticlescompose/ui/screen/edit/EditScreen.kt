package com.example.feedarticlescompose.ui.screen.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.ui.screen.components.InputFormTextField
import com.devid_academy.feedarticlescompose.utils.getRadioButtonColors
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.screen.create.ArticleEvent
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor

@Composable
fun EditScreen(
    navController: NavController,
    editViewModel: EditArticleViewModel,
    articleId: String
) {

    val editState by editViewModel.editStateFlow.collectAsState()
    val articleStateFlow by editViewModel.articleStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(articleId) {
        editViewModel.getArticle(articleId.toLong())
    }

    LaunchedEffect(true) {
        editViewModel.editSharedFlow.collect { event ->
            when (event) {
                is ArticleEvent.NavigateToMainScreen -> {
                    navController.navigate(Screen.Main.route) {
                        popUpTo("edit") {
                            inclusive = true
                        }
                    }
                }
                is ArticleEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(context.getString(event.resId))
                }
                else -> {}
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
//            Text(text = "ID de l'article : ${articleId ?: "Aucun ID reçu"}")
            articleStateFlow?.let {article ->
                EditContent(
                    article = article,onEdit = {articleId, articleTitle, articleDesc, articleImageUrl, selectedValueForCategory ->
                        editViewModel.updateArticle(
                            articleId.toString(),
                            articleTitle,
                            articleDesc,
                            articleImageUrl,
                            selectedValueForCategory.toString()
                        )
                        keyboardController?.hide()
                    },
                    onNavigate = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }
    }

}

@Composable
fun EditContent(
    article: ArticleDTO,
    onEdit: (
        articleId: Long,
        articleTitle: String,
        articleDesc: String,
        articleImageUrl: String,
        selectedValueForCategory: Int
    ) -> Unit,
    onNavigate: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val context = LocalContext.current

        var articleId by remember { mutableStateOf(article.id)  }
        var articleTitle by remember { mutableStateOf(article.title) }
        var articleDesc by remember { mutableStateOf(article.description) }
        var articleImageUrl by remember { mutableStateOf(article.urlImage) }
        var selectedValueForCategory by remember { mutableStateOf(article.category) }

        Text(
            text = context.getString(R.string.edit_tv_title),
            style = MaterialTheme.typography.headlineMedium.copy(color = FeedArticlesColor),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(100.dp))
        InputFormTextField(
            value = articleTitle,
            onValueChange = { articleTitle = it },
            label = context.getString(R.string.edit_et_title_article)

        )
        Spacer(modifier = Modifier.height(15.dp))

        InputFormTextField(
            value = articleDesc,
            onValueChange = { articleDesc = it },
            maxLines = 10,
            label = context.getString(R.string.edit_et_description_article),
            modifier = Modifier
                .height(200.dp),
            singleLine = false
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = articleImageUrl,
            onValueChange = { articleImageUrl = it },
            label = context.getString(R.string.edit_et_url_image),
        )

        Spacer(modifier = Modifier.height(30.dp))
        AsyncImage(
            model = articleImageUrl,
            contentDescription = articleTitle,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.feedarticles_logo),
            modifier = Modifier
                .width(100.dp),
            error = painterResource(R.drawable.feedarticles_logo),
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            RadioButton(
                selected = selectedValueForCategory == 1,
                onClick = {
                    selectedValueForCategory = 1
                },
                colors = getRadioButtonColors()
            )
            Text(
                text = context.getString(R.string.btn_sport),
                modifier = Modifier.clickable {
                    selectedValueForCategory = 1
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedValueForCategory == 2,
                onClick = {
                    selectedValueForCategory = 2
                },
                colors = getRadioButtonColors()
            )
            Text(
                text= context.getString(R.string.btn_manga),
                modifier = Modifier.clickable {
                    selectedValueForCategory = 2
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedValueForCategory == 3,
                onClick = {
                    selectedValueForCategory = 3
                },
                colors = getRadioButtonColors()
            )
            Text(
                text= context.getString(R.string.btn_misc),
                modifier = Modifier.clickable {
                    selectedValueForCategory = 3
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                onEdit(
                    articleId,
                    articleTitle,
                    articleDesc,
                    articleImageUrl,
                    selectedValueForCategory)
            },
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint,
                contentColor = Color.White
            )
        ) {
            Text(context.getString(R.string.create_btn_validate))
        }
    }


}

//@Preview(showBackground = true)
//@Composable
//fun PreviewEditContent() {
//    com.example.feedarticlescompose.ui.screen.create.CreateContent(
//        onCreate = { articleId, articleTitle, articleDesc, articleImageUrl, selectedValueForCategory ->
//        },
//        onNavigate = {}
//    )
//}