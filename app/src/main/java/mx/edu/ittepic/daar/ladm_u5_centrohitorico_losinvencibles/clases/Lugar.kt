package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.content.Context

class Lugar (este : Context) {
    var lugar = ""
    var descripcion = ""
    var categoria = ""
    var estrellas = 5.0F
    var latitud = 0.0
    var longitud = 0.0

    fun contenido() : String{
        return "Lugar: ${lugar}\nCategoria: ${categoria}, Estatus: ★★★★★\nDescipcion: ${descripcion}"
    }
}