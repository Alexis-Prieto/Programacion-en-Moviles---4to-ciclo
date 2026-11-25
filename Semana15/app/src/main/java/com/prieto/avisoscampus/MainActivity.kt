package com.prieto.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prieto.avisoscampus.data.AvisosRepositoryMemoria
import com.prieto.avisoscampus.ui.PantallaAvisos
import com.prieto.avisoscampus.ui.theme.AvisosCampusTheme
import com.prieto.avisoscampus.viewmodel.AvisosViewModel
import com.prieto.avisoscampus.viewmodel.AvisosViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvisosCampusTheme {
                val factory = remember {
                    AvisosViewModelFactory(
                        AvisosRepositoryMemoria()
                    )
                }
                val vm: AvisosViewModel = viewModel(
                    factory = factory
                )
                PantallaAvisos(vm)
            }
        }
    }
}