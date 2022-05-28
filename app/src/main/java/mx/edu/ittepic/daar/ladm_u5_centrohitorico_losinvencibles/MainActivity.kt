package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.acercade,menu)
        return true
    }// match del activity desde el XML

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var integrantes = "Integrantes:\n" +
                "16400887 DANIEL ALEJANDRO AYALA RAMIREZ\n" +
                "16400985 BLANCA ESTEFANI RAMIREZ BARAJAS\n" +
                "17401063 CARLOS EDUARDO ROBLES LOPEZ\n" +
                "17400978 CARLOS URIEL FREGOSO ESPERICUETA\n"
        when(item.itemId){
            R.id.acercade -> {
                AlertDialog.Builder(this)
                    .setTitle("EQUIPO: LOS INVITADOS")
                    .setMessage(integrantes)
                    .setPositiveButton("Salir"){_,_ ->}
                    .show()
            }
            R.id.cerrarsesión -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,Login::class.java))
                finish()
            }
            R.id.salir -> {
                finish()
            }
        }
        return true
    }
}

interface Comunicator {
    fun passData(coordenada:Double,coordenada2: Double)
}
