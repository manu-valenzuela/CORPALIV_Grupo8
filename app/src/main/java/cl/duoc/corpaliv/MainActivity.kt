package cl.duoc.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.corpaliv.ui.theme.CORPALIV_Grupo8Theme
import cl.duoc.corpaliv.ui.theme.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CORPALIV_Grupo8Theme {
                // Mostramos la pantalla principal completa
                HomeScreen2()
            }
        }
    }
}

// Vista previa directamente desde MainActivity
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CORPALIV_Grupo8Theme {
        HomeScreen()
    }
}