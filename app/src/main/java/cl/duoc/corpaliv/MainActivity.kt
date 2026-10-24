package cl.duoc.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import cl.duoc.corpaliv.ui.theme.TemaCorpaliv
import cl.duoc.corpaliv.ui.utils.NavegacionAplicacion

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaCorpaliv {
                val claseAnchoVentana = calculateWindowSizeClass(this).widthSizeClass
                val controladorNavegacion = rememberNavController()

                NavegacionAplicacion(
                    claseAnchoVentana = claseAnchoVentana,
                    controladorNavegacion = controladorNavegacion
                )
            }
        }
    }
}