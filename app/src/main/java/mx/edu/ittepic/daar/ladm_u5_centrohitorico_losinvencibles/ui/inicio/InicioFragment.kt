package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.R
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Data
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Ubicacion
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentInicioBinding

class InicioFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    var _binding: FragmentInicioBinding? = null
    val binding get() = _binding!!

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    lateinit var map:GoogleMap
    var position = ArrayList<Data>()
    lateinit var locacion : LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Pedimos permisos al usuario de usar su localización
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE_LOCATION)
        }//permisos

        //Obtenemos del sistema el localizador GPS en tiempo real
        locacion = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var ubi = Ubicacion(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, ubi)

        crearMapFragment()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isLocationPermission()= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onMapReady(googleMap: GoogleMap) {
        //Mandar ubicacion al mapa
        map = googleMap
        crearMarcador()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()
    }

    fun crearMarcador() {
        var miUbi = LatLng(21.50718, -104.89303)
        //Crear punto
        map.addMarker(
            MarkerOptions()
                .position(miUbi)
                .title("Aqui estoy")
                .snippet("Información Extra")
                //.icon() Podemos agregar iconos personalizados
        )
        //Animacion de Zoom a la ubicacion seleccionada
        map.animateCamera(CameraUpdateFactory
            .newLatLngZoom(miUbi,18f),2000,null)

    }

    override fun onMyLocationButtonClick(): Boolean {
        mensaje("Boton pulsado")
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        mensaje("Estás en ${p0.latitude}, ${p0.longitude}")
    }

    fun crearMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun enableLocation(){
        if(!::map.isInitialized)
            return
        if(isLocationPermission()){
            // si
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled=true
        }
        else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            mensaje("Ve a ajustes y acepta los permisos")
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                map.isMyLocationEnabled=true
            } else{
                mensaje("Para activar la localizacion vaya a ajustes y otorgue permisos")
            }
            else->{}
        }
    }

    override fun onResume() {
        super.onResume()
        if(!::map.isInitialized) return
        if(!isLocationPermission()){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled=false
            mensaje("Para activar la localizacion ve a ajustes y acepta los permisos")
        }
    }

    fun mensaje(cadena : String) {
        Toast.makeText(requireContext(), cadena, Toast.LENGTH_SHORT).show()
    }

    fun alerta(cadena: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("ATENCIÓN")
            .setMessage(cadena)
            .show()
    }

}