package com.devid_academy.feedarticlescompose.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.ui.screen.components.InputFormTextField
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor
import kotlinx.coroutines.flow.collect

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val loginState by loginViewModel.loginState.collectAsState()

    LaunchedEffect(true) {
        loginViewModel.directionSharedFlow.collect { direction ->
            direction?.let {
                navController.navigate(it) {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }
        }
    }
    LoginContent(
        onLogin = { login, mdp ->
            loginViewModel.verifyLogin(login, mdp)
        },
        onNavigate = {
            navController.navigate(Screen.Register.route)
        }
    )
}

@Composable
fun LoginContent(
    onLogin: (login: String, mdp: String) -> Unit,
    onNavigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val context = LocalContext.current

        var loginForm by remember { mutableStateOf("") }
        var mdpForm by remember { mutableStateOf("") }

        Text(
            text = context.getString(R.string.login_tv_title),
            style = MaterialTheme.typography.headlineMedium.copy(color = FeedArticlesColor),
            fontWeight = FontWeight.Bold
        )



        Spacer(modifier = Modifier.height(100.dp))
        InputFormTextField(
            value = loginForm,
            onValueChange = { loginForm = it },
            label = context.getString(R.string.login_et_name)
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = mdpForm,
            onValueChange = { mdpForm = it },
            label = context.getString(R.string.login_et_password),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(200.dp))
        Button(
            onClick = {
                onLogin(loginForm, mdpForm)
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = Color.White
            )
        ) {
            Text(context.getString(R.string.login_btn_login))
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = context.getString(R.string.login_tv_not_registered),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.clickable {
                onNavigate()
            }
        )
    }
}

@Composable
@Preview
fun LoginPreview() {
    LoginContent(
        onLogin = {_, _ -> },
        onNavigate = {}
    )
}

