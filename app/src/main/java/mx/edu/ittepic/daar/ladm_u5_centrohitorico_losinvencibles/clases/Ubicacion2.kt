package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.google.firebase.firestore.GeoPoint
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.lugares.LugaresFragment

class Ubicacion2(puntero:LugaresFragment) : LocationListener {
    var p = puntero

    override fun onLocationChanged(locationOne: Location) {
        try {
            p.binding.txtLatitud.setText(locationOne.latitude.toString())
            p.binding.txtLongitud.setText(locationOne.longitude.toString())
        } catch (e:Exception) { }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }
}