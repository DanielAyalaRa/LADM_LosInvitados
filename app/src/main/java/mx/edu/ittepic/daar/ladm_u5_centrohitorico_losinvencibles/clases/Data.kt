package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio.InicioFragment

class Data(puntero: InicioFragment) : LocationListener {
    var p = puntero

    override fun onLocationChanged(locationOne: Location) {
        try {
            p.binding.latitud.setText(locationOne.latitude.toString())
            p.binding.longitud.setText(locationOne.longitude.toString())
        } catch (e:Exception) {

        }
    }//onLocationChanged

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }
}