package com.devid_academy.feedarticlescompose.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor

@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel) {

    val isLoadingFromVM by splashViewModel.isLoadingStateFlow.collectAsState()

    LaunchedEffect(true) {
        splashViewModel.goToMainOrLogin.collect { direction ->
            direction?.let {
                navController.navigate(it) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
    SplashContent(isLoading = isLoadingFromVM)
}

@Composable
fun SplashContent(isLoading: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FeedArticlesColor),

        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isLoading,
            enter = slideInVertically(
                initialOffsetY = { - 1000 },
                animationSpec = tween(1000)
            ) + fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(700)) + slideOutVertically(
                animationSpec = tween(700),
                targetOffsetY = { -400 }
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription = "Feed Articles",
                modifier = Modifier
                    .size(200.dp)
                    .padding(10.dp)
            )
        }
        AnimatedVisibility(
            visible = isLoading,
            enter = slideInVertically(
                initialOffsetY = { 1000 },
                animationSpec = tween(1000)
            ) + fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(700)) + slideOutVertically(
                animationSpec = tween(700),
                targetOffsetY = { 400 }
            )
        ) {
            Text(
                text = "Feed Articles",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold

            )
        }
    }
}

@Preview
@Composable
fun SplashPreview() {
    SplashContent(true)
}
