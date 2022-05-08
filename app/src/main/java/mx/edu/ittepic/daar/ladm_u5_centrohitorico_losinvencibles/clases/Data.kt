package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import com.google.firebase.firestore.GeoPoint

class Data {
    var nombre : String = ""
    var posicion1 : GeoPoint = GeoPoint(0.0,0.0)
    var posicion2 : GeoPoint = GeoPoint(0.0,0.0)
    var informacion : String = ""

    //Sobreescribimos el metodo toString() para mostrar los datos del objeto Data
    override fun toString(): String {
        return nombre+"\n"+posicion1.latitude+","+posicion1.longitude+"\n"+
                posicion2.latitude+","+posicion2.longitude+"\n"
    }

    //Función para saber en que punto geografico nos encontramos
    fun estoyEn(posicionActual : GeoPoint) : Boolean{
        if (posicionActual.latitude >= posicion1.latitude &&
            posicionActual.latitude <= posicion2.latitude){
            if (invertir(posicionActual.longitude) >= invertir(posicion1.longitude) &&
                invertir(posicionActual.longitude) <= invertir(posicion2.longitude)){
                return true
            }
        }
        return false
    }//estoyEn

    //Función para invertir los valores negaticos d la longitud en un punto geografico
    private fun invertir(valor : Double) : Double{
        return valor*-1
    }//invertir
}