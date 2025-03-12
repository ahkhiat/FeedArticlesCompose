package com.example.feedarticlescompose.ui.screen.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.ui.screen.auth.AuthViewModel
import com.devid_academy.feedarticlescompose.utils.ArticleEvent
import com.devid_academy.feedarticlescompose.utils.formatDate
import com.devid_academy.feedarticlescompose.utils.getCategoryName
import com.devid_academy.feedarticlescompose.utils.getRadioButtonColors
import com.example.feedarticlescompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel
    ) {

    val articlesList by mainViewModel.articles.collectAsState()
    val filteredArticlesList by mainViewModel.filteredArticles.collectAsState()

    val direction by authViewModel.directionStateFlow.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()

    val currentUserId by mainViewModel.currentUserId.collectAsState()

    var selectedArticle by remember { mutableStateOf<ArticleDTO?>(null) }
    var selectedValueForCategory by remember { mutableStateOf(0) }

    val context = LocalContext.current

    LaunchedEffect(true) {
        mainViewModel.mainSharedFlow.collect { direction ->
            direction?.let {
                when {
                    it == Screen.Login.route -> {
                        navController.navigate(it) {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }
                    it.startsWith("edit/") -> {
                        navController.navigate(it)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Create.route)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Menu",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.PowerSettingsNew,
                            contentDescription = "Recherche",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                modifier = Modifier.height(35.dp)
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(50.dp)
            ) {
                val categories = listOf(
                    0 to R.string.btn_all,
                    1 to R.string.btn_sport,
                    2 to R.string.btn_manga,
                    3 to R.string.btn_misc
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    categories.forEach { (categoryId, stringRes) ->
                        RadioButton(
                            selected = selectedValueForCategory == categoryId,
                            onClick = {
                                selectedValueForCategory = categoryId
                                mainViewModel.setSelectedCategory(categoryId)
                            },
                            colors = getRadioButtonColors()
                        )
                        Text(
                            text = context.getString(stringRes),
                            modifier = Modifier.clickable {
                                selectedValueForCategory = categoryId
                                mainViewModel.setSelectedCategory(categoryId)
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MainContent(
                    filteredArticlesList,
                    currentUserId,
                    onClick = {
                        selectedArticle = if (selectedArticle == it) null else it
                        selectedArticle?.idUser?.let { idUser ->
                            mainViewModel.navigateIfUserIsOwner(idUser, selectedArticle!!.id)
                        }
                    },
                    selectedArticle = selectedArticle,
                    isRefreshing = isLoading,
                    onRefresh = {
                        mainViewModel.getArticles()
                    },
                    onDismiss = {
                        mainViewModel.deleteArticle(it)
                    }
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
@ExperimentalMaterial3Api
fun MainContent(
    articlesList: List<ArticleDTO>,
    currentUserId: Long,
    onClick: (ArticleDTO) -> Unit,
    selectedArticle: ArticleDTO?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: (Long) -> Unit
) {

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(
                    items = articlesList,
                    key = { it.id }
                ) {article ->
                    val isSelected = article == selectedArticle
                    val scale by animateFloatAsState(if(isSelected) 1.5f else 1f, label = "")

                    val dismissState = rememberDismissState()

                    LaunchedEffect(dismissState.currentValue) {
                        if (dismissState.currentValue == DismissValue.DismissedToEnd ||
                            dismissState.currentValue == DismissValue.DismissedToStart)
                        {
                            onDismiss(article.id)
                        }
                    }
                    if(currentUserId == article.idUser) {
                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = { DismissBackground(dismissState) },
                            dismissContent = {
                                ItemView(
                                    article = article,
                                    isSelected = isSelected,
                                    onClick = { onClick(article) },
                                    scale = scale
                                )
                            }
                        )
                    } else {
                        ItemView(
                            article = article,
                            isSelected = isSelected,
                            onClick = { onClick(article) },
                            scale = scale
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemView(
    article: ArticleDTO,
    isSelected: Boolean,
    onClick: (ArticleDTO) -> Unit,
    scale: Float,
) {
    val context = LocalContext.current

    val backgroundColor = when (article.category) {
        1 -> MaterialTheme.colorScheme.onPrimary
        2 -> MaterialTheme.colorScheme.onSecondary
        3 -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.surface
    }

    val imageSize by animateDpAsState(
        targetValue = if(isSelected) 80.dp else 50.dp,
        animationSpec = tween(durationMillis = 300),
        label = "imageAnimation"
    )
    Box(
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
    ){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
            ),
            modifier = Modifier
                .width(380.dp)
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            onClick = {
                onClick(article)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                verticalAlignment = if (isSelected) Alignment.Top else Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AsyncImage(
                        model = article.urlImage,
                        contentDescription = article.title,
                        contentScale = ContentScale.FillHeight,
                        placeholder = painterResource(R.drawable.feedarticles_logo),
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        error = painterResource(R.drawable.feedarticles_logo),
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 10.dp,
                            top = if (isSelected) 25.dp else 0.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    Text(
                        text = article.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    if(isSelected) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Flip",
                        )
                    }
                }
            }
            if (isSelected) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = context.getString(R.string.date_item)+ " " + formatDate(article.createdAt!!) ,
                        fontSize = 10.sp,
                        modifier = Modifier

                    )
                    Text(
                        text = "Cat. " + getCategoryName(LocalContext.current, article.category),
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = article.description,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        targetValue = if (dismissState.targetValue == DismissValue.Default) Color.Transparent else Color.Red,
        label = "dismissBackground"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color)
        ,
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Supprimer",
            tint = Color.White
        )
    }
}


@Preview
@Composable
fun ItemViewPreview() {
   ItemView(ArticleDTO(
       id = 1L,
       title = "Justin Gaethje a un nouvel adversaire pour l'UFC 313 avec Rafael Fiziev ",
       description = "Découvrez les dernières tendances en matière de technologie mobile, de l'IA aux nouveaux appareils pliables. Un aperçu fascinant de ce que l'avenir réserve dans l'univers des smartphones et des gadgets.",
       urlImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJxo2NFiYcR35GzCk5T3nxA7rGlSsXvIfJwg&s",
       category = 2,
       createdAt = "2025-03-10T14:00:00Z",
       idUser = 123L
   ),
       isSelected = false,
       onClick = {},
       scale = 1.0f
   )
}
