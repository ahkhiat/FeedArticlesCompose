package com.devid_academy.feedarticlescompose.ui.screen.auth

import android.provider.Settings.Global.getString
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.devid_academy.feedarticlescompose.ui.navigation.Screen
import com.devid_academy.feedarticlescompose.ui.screen.components.InputFormTextField
import com.example.feedarticlescompose.ui.theme.FeedArticlesColor

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel) {

    val registerState by registerViewModel.registerStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(true) {
        registerViewModel.registerSharedFlow.collect { event ->
            when (event) {
                is AuthEvent.NavigateToMainScreen -> {
                    navController.navigate(Screen.Main.route) {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                }
                is AuthEvent.ShowSnackBar -> {
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

            RegisterContent(
                onRegister = { login, mdp, mdpConfirm ->
                    registerViewModel.register(login, mdp, mdpConfirm)
                    keyboardController?.hide()
                },
                onNavigate = {
                    navController.navigate(Screen.Login.route)
                }
            )

        }
    }

}

@Composable
fun RegisterContent(
    onRegister: (
        login: String,
        mdp: String,
        mdpConfirm: String
    ) -> Unit,
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
        var mdpConfirmForm by remember { mutableStateOf("") }

        Text(
            text = context.getString(R.string.register_tv_title),
            style = MaterialTheme.typography.headlineMedium.copy(color = FeedArticlesColor),
            fontWeight = FontWeight.Bold
        )



        Spacer(modifier = Modifier.height(100.dp))
        InputFormTextField(
            value = loginForm,
            onValueChange = { loginForm = it },
            label = context.getString(R.string.register_et_name)

        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = mdpForm,
            onValueChange = { mdpForm = it },
            label = context.getString(R.string.register_et_password),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = mdpConfirmForm,
            onValueChange = { mdpConfirmForm = it },
            label = context.getString(R.string.register_et_password_confirm),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(170.dp))
        Button(
            onClick = {
                onRegister(loginForm, mdpForm, mdpConfirmForm)
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint,
                contentColor = Color.White
            )
        ) {
            Text(context.getString(R.string.register_tv_signup))
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = context.getString(R.string.register_tv_already_registered),
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier.clickable {
                onNavigate()
            }
        )
    }
}
