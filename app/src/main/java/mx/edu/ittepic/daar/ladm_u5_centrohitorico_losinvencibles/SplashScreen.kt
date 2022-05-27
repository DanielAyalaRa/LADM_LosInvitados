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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inicio.alpha = 0f
        binding.inicio.animate().setDuration(1500).alpha(1f).withEndAction {
            val i = Intent(this,Login::class.java)
            startActivity(i)
            finish()
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