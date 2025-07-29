package com.example.abbproject.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.abbproject.navigation.Routes
import androidx.compose.ui.res.painterResource
import com.example.abbproject.R




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val resetMessage by viewModel.resetPasswordMessage.collectAsState()


    val rememberedEmail by viewModel.savedEmail.collectAsState(initial = "")
    val rememberedCheck by viewModel.rememberMe.collectAsState(initial = false)

    var email by remember { mutableStateOf(rememberedEmail) }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(rememberedCheck) }


    val isLoading = loginState is LoginState.Loading
    val isError = loginState is LoginState.Error

    LaunchedEffect(Unit) {
        if (viewModel.isUserLoggedIn()) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_android),
            contentDescription = "Google Icon",
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sign in to your Account",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Enter your email and password to log in",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Remember me")
            }

            TextButton(onClick = {
                viewModel.sendPasswordReset(email.trim())
            }) {
                Text("Forgot Password?", style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.login(email.trim(), password.trim(), rememberMe)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Log In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Or", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Google Sign-In (not implemented) */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Google",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(8.dp))
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { /* Facebook Sign-In (not implemented) */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_facebook),
                contentDescription = "Facebook",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(8.dp))
            Text("Continue with Facebook")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don’t have an account? ")
            TextButton(
                onClick = {
                    viewModel.resetState()
                    navController.navigate(Routes.Register.route)
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Sign Up")
            }
        }

        if (isError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (loginState as LoginState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (!resetMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resetMessage ?: "",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
            LaunchedEffect(resetMessage) {
                kotlinx.coroutines.delay(3000)
                viewModel.clearResetMessage()
            }
        }
    }
}


















