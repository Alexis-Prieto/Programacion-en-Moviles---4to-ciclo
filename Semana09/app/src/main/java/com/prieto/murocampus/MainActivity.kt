package com.prieto.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.prieto.murocampus.data.PostRepositoryRemote
import com.prieto.murocampus.data.remote.RetrofitClient
import com.prieto.murocampus.ui.navigation.AppNav
import com.prieto.murocampus.ui.theme.MuroCampusTheme
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PostRepositoryRemote(RetrofitClient.api)
        val factory = PostsViewModelFactory(repository)

        val vm = ViewModelProvider(
            this,
            factory
        )[PostsViewModel::class.java]

        setContent {
            MuroCampusTheme {
                AppNav(vm)
            }
        }
    }
}