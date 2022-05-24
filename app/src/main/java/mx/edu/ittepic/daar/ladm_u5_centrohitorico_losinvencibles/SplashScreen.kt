package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.ActivitySplashScreenBinding
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio.InicioFragment

class SplashScreen : AppCompatActivity() {
    lateinit var binding : ActivitySplashScreenBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                InicioFragment.REQUEST_CODE_LOCATION
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location ->
                var latitud = location.latitude
                var longitud = location.longitude
                binding.inicio.alpha = 0f
                binding.inicio.animate().setDuration(1500).alpha(1f).withEndAction {
                    val i = Intent(this,MainActivity::class.java)
                    i.putExtra("LATITUD",latitud)
                    i.putExtra("LONGITUD",longitud)
                    startActivity(i)
                    finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }
}