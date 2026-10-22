package cl.duoc.corpaliv.ui.theme

// Importaciones necesarias para componentes visuales, diseño y colores
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.corpaliv.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // 1. Estructura principal con Scaffold (Barra superior y contenedor de pantalla)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Corpaliv",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                // Punto 5: Uso de colores del sistema mediante MaterialTheme
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // 2. Contenedor vertical (Column) para organizar los elementos
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            // Punto 4: Espaciado uniforme de 16.dp entre componentes
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // Punto 6: Alineación horizontal centrada para todos los elementos
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección: Encabezado de la pantalla
            Text(
                text = "¡Bienvenido a Corpaliv!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary // Punto 5: Color dinámico del tema
            )

            // Sección: Imagen principal del logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Fit
            )

            // Punto 8: Nuevo elemento visual - Línea divisoria
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Punto 8: Nuevo elemento visual - Tarjeta de información (Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant // Punto 5
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información del Proyecto",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Esta interfaz utiliza los componentes oficial de Material Design 3 en Jetpack Compose.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Sección: Botón de interacción
            Button(
                onClick = { /* Acción futura */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Presióname",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

// Vista previa para visualizar la pantalla en el editor de Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}