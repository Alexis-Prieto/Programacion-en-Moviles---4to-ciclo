package com.prieto.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prieto.campusgps.data.LugaresRepositoryLocal
import com.prieto.campusgps.data.UbicacionRepositoryGoogle
import com.prieto.campusgps.ui.PantallaMapa
import com.prieto.campusgps.ui.theme.CampusGPSTheme
import com.prieto.campusgps.viewmodel.CampusViewModelFactory
import com.prieto.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext),
            LugaresRepositoryLocal()
        )
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(factory = factory)
                PantallaMapa(vm)
            }
        }
    }
}