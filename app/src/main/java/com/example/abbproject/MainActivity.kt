package com.example.abbproject


import androidx.activity.compose.setContent
import com.example.abbproject.ui.theme.AbbProjectTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.abbproject.navigation.NavGraph
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            AbbProjectTheme {
                NavGraph(navController = androidx.navigation.compose.rememberNavController())
            }
        }
    }
}

