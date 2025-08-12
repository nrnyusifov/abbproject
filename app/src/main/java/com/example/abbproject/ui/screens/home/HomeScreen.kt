package com.example.abbproject.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.abbproject.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        user?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                val imageBitmap = remember(it.profileImageBase64) {
                    if (it.profileImageBase64.isNotBlank()) {
                        try {
                            val bytes = android.util.Base64.decode(it.profileImageBase64, android.util.Base64.DEFAULT)
                            android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    } else null
                }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                            .clickable {
                                navController.navigate(Routes.Profile.route)
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                            .clickable {
                                navController.navigate(Routes.Profile.route)
                            }
                            .padding(12.dp)
                    )
                }


                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Hello, ${it.firstName}!",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Nice to see you again 😊",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } ?: Text("Loading user info...")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate(Routes.Account.route)
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Account")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Routes.History.route)
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("History")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.signOut()
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out")
        }
    }
}



