package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.ActivityMainBinding
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.lugares.LugaresFragment

class MainActivity : AppCompatActivity(), Comunicator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.show()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_lugares, R.id.navigation_inicio, R.id.navigation_galeria
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun passData(coordenada: Double, coordenada2: Double) {
        val bundle = Bundle()
        bundle.putDouble("latitud",coordenada)
        bundle.putDouble("longitud",coordenada2)
        val transaction = this.supportFragmentManager.beginTransaction()
        val placeFragment = LugaresFragment()
        placeFragment.arguments = bundle
        transaction.commit()
    }
}

interface Comunicator {
    fun passData(coordenada:Double,coordenada2: Double)
}
