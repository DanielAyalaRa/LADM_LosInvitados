package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.google.firebase.firestore.GeoPoint
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio.InicioFragment

class Ubicacion(puntero:InicioFragment) : LocationListener {
    var p = puntero

    override fun onLocationChanged(locationOne: Location) {
        try {
            p.binding.txtCoordenadas.setText("Ubicacion actual:\n${locationOne.latitude}, ${locationOne.longitude}")
            var geoPosicionGPS = GeoPoint(locationOne.latitude, locationOne.longitude)

            for (item in p.position) {
                if (item.estoyEn(geoPosicionGPS)) {
                    p.binding.txtActual.setText("Te encuentras en: ${item.nombre}")
                }
            }
        } catch (e:Exception) {

        }
    }//onLocationChanged

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }
}