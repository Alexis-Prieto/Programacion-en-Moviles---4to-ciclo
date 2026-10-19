package com.prieto.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prieto.cafecampus.ui.navigation.NavCafe
import com.prieto.cafecampus.ui.theme.CafeCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as CafeApp).repository
        setContent {
            CafeCampusTheme {
                NavCafe(repository = repository)
            }
        }
    }
}