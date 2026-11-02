package cl.duoc.corpaliv.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import java.util.Locale

class ManejadorUbicacion(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun obtenerUbicacionActual(
        onResultado: (direccion: String, lat: Double, lng: Double) -> Unit,
        onError: (String) -> Unit
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    @Suppress("DEPRECATION")
                    val direcciones = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val direccionTexto = direcciones?.firstOrNull()?.getAddressLine(0)
                        ?: "Lat: ${location.latitude}, Lng: ${location.longitude}"
                    onResultado(direccionTexto, location.latitude, location.longitude)
                } catch (e: Exception) {
                    onResultado("Lat: ${location.latitude}, Lng: ${location.longitude}", location.latitude, location.longitude)
                }
            } else {
                onError("No se pudo obtener la ubicación actual")
            }
        }.addOnFailureListener { exception ->
            onError(exception.localizedMessage ?: "Error al acceder al GPS")
        }
    }
}