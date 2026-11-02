package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.corpaliv.R

@Composable
fun PantallaSeleccionRolCompacta(
    alSeleccionarSocioInvitado: () -> Unit,
    alSeleccionarFuncionario: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_corpaliv),
                contentDescription = "Logo CORPALIV",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Fundación Corpaliv",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF1E1E1E)
                )
            )

            Text(
                text = "Compromiso - Respeto - Empatía - Solidaridad",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
            )

            Button(
                onClick = alSeleccionarSocioInvitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Acceder como Socio / Invitado",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = alSeleccionarFuncionario,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Acceder como Funcionario",
                    fontSize = 16.sp
                )
            }
        }
    }
}