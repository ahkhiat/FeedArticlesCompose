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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun RegisterScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()

//    LaunchedEffect(direction) {
//        direction?.let{
//            navController.navigate(it)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Inscription", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        InputFormTextField(email, {email = it},"Nom d\'utilisateur")
            Spacer(modifier = Modifier.height(8.dp))
        InputFormTextField(password, {password = it},"Mot de passe", true)
            Spacer(modifier = Modifier.height(16.dp))
        InputFormTextField(firstname, {firstname = it},"Prénom")
            Spacer(modifier = Modifier.height(8.dp))
        InputFormTextField(lastname, {lastname = it},"Nom")
            Spacer(modifier = Modifier.height(8.dp))
        InputFormTextField(birthdate, {birthdate = it},"Date de naissance")
            Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            loginViewModel.verifyLogin(email, password)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("S\'inscrire")
        }
            Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Déja inscrit ? Connectez-vous !",
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navController.navigate(Screen.Login.route)
            }
        )
    }

}
