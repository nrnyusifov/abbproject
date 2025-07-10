package com.example.abbproject.ui.screens.emailverification

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.abbproject.navigation.Routes


@Composable
fun EmailVerifyScreen(
    navController: NavController,
    viewModel: EmailVerifyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Verification link sent.")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.reloadUser { isVerified ->
                if (isVerified) {
                    Toast.makeText(context, "Registration successfully completed!", Toast.LENGTH_LONG).show()

                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Emailverify.route) { inclusive = true }
                    }

                } else {
                    message = "Email is not verified yet."
                }
            }
        }) {
            Text("I’ve Verified")
        }


        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            viewModel.resendVerification { success, error ->
                message = if (success) "Email is sent again." else error ?: "Error occured."
            }
        }) {
            Text("Send again")
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(message)
        }
    }
}


@Preview
@Composable
fun PreviewEmailVerificationScreen() {
    EmailVerifyScreen(navController = rememberNavController())
}

