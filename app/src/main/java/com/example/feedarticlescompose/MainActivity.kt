package com.example.feedarticlescompose

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.devid_academy.feedarticlescompose.ui.navigation.MyAppNavigation
import com.example.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//            window.statusBarColor = Color.Transparent.toArgb()
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }

        setContent {
            FeedArticlesComposeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FeedArticlesComposeTheme {
        Greeting("Android")
    }
}