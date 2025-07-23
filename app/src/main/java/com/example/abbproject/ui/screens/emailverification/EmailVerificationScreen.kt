package com.example.abbproject.ui.screens.emailverification

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.abbproject.navigation.Routes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun EmailVerifyScreen(
    navController: NavController,
    viewModel: EmailVerifyViewModel = hiltViewModel()
) {
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "your@email.com"
    var isVerifiedScreen by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (!isVerifiedScreen) {
            viewModel.reloadUser { verified ->
                isVerifiedScreen = verified
            }
            delay(3000)
        }
    }

    LaunchedEffect(Unit) {
        delay(60_000)
        val user = Firebase.auth.currentUser
        user?.reload()
        if (user != null && !user.isEmailVerified) {
            user.delete()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (!isVerifiedScreen) {
            Text(
                text = "📨",
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Please verify your email",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We sent an email to\n$email",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Just click on the link in that email to complete your signup. If you don't see it, check your spam folder.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Still can’t find the email?",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.resendVerification { success, error ->
                    message = if (success) "Verification email resent." else error ?: "An error occurred."
                }
            }) {
                Text("Resend Verification Email")
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = message, color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Text(
                text = "✅",
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Email Verified",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your email address was successfully verified.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Emailverify.route) { inclusive = true }
                }
            }) {
                Text("Back to Login")
            }
        }
    }
}
