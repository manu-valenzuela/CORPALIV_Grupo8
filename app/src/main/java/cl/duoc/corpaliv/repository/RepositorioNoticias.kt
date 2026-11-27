package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.Noticia

class RepositorioNoticias {

    // Datos locales hasta que exista el microservicio de noticias
    suspend fun obtenerNoticias(): List<Noticia> {
        return listOf(
            Noticia(
                id = "1",
                titulo = "Proyecto Impulso Laboral Alba Lab",
                resumen = "Formación laboral y acompañamiento para personas con discapacidad múltiple.",
                urlImagen = "",
                fecha = "2026-10-15"
            ),
            Noticia(
                id = "2",
                titulo = "Escuela Jan Van Dijk amplía matrícula",
                resumen = "Acreditación ministerial permite aumentar la cobertura educativa.",
                urlImagen = "",
                fecha = "2026-10-10"
            )
        )
    }
}
