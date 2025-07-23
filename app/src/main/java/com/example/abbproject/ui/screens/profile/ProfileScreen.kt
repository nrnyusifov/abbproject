package com.example.abbproject.ui.screens.profile


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: com.example.abbproject.ui.screens.home.HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let {
            AsyncImage(
                model = it.imageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${it.firstName} ${it.lastName}", style = MaterialTheme.typography.titleLarge)
            Text(text = it.email, style = MaterialTheme.typography.bodyMedium)
        } ?: Text("Loading...")
    }
}
