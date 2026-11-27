package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.Noticia

class RepositorioNoticias {

    suspend fun obtenerNoticias(): List<Noticia> {
        // En una implementación real, aquí se consume una API o Base de Datos
        return listOf(
            Noticia(
                id = "1",
                titulo = "Nueva campaña de recaudo 2026",
                resumen = "Acompáñanos en la gran colecta anual de CORPALIV.",
                urlImagen = "https://ejemplo.com/imagen1.jpg",
                fecha = "2026-10-15"
            ),
            Noticia(
                id = "2",
                titulo = "Talleres para la comunidad",
                resumen = "Inscripciones abiertas para talleres de integración.",
                urlImagen = "https://ejemplo.com/imagen2.jpg",
                fecha = "2026-10-10"
            )
        )
    }
}