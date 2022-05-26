package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.Comunicator
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.R
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Lugar
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentInicioBinding
import kotlin.math.*

class InicioFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {
    var _binding: FragmentInicioBinding? = null
    val binding get() = _binding!!

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    private val baseRemota = FirebaseFirestore.getInstance().collection("Lugares")
    var listaId = ArrayList<String>()
    lateinit var map:GoogleMap
    private lateinit var comm : Comunicator
    var latitud = 0.0
    var longitud = 0.0

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

        crearMapFragment()

        comm = activity as Comunicator

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
        //new permission for the click on marken and over the info window
        map.setOnInfoWindowClickListener(this)
        map.setOnMarkerClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        enableLocation()
    }

    fun crearMarcador() {
        latitud = requireActivity().intent.extras!!.getDouble("LATITUD")!!
        longitud = requireActivity().intent.extras!!.getDouble("LONGITUD")!!

        var miUbi = LatLng(latitud,longitud)
        map.animateCamera(CameraUpdateFactory
            .newLatLngZoom(miUbi,18f),2000,null)
        // Hacemos un zoom a la plaza principal de Tepic*/

        baseRemota
            .addSnapshotListener { query, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }
                listaId.clear()
                val lugar = Lugar(requireContext())

                for (documento in query!!) {
                    lugar.lugar = documento.getString("lugar").toString()
                    lugar.descripcion = documento.getString("descripcion").toString()
                    lugar.categoria = documento.getString("categoria").toString()
                    lugar.latitud = documento.getDouble("latitud")!!
                    lugar.longitud = documento.getDouble("longitud")!!

                    val Ubi = LatLng(lugar.latitud, lugar.longitud)
                    var icono = BitmapDescriptorFactory.fromResource(R.drawable.marcador)

                    when(lugar.categoria) {
                        "Iglesias" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.iglesia)
                        "Restaurantes" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.restaurante)
                        "Hoteles" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.hotel)
                        "Plazas" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.plaza)
                        "Museos" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.museo)
                        "Edificios Administrativos" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.edificio)
                        "Tiendas Departamentales" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.tienda)
                        "Centrales" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.bus)
                        "Cafes" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.cafe)
                        "Mercados" -> icono = BitmapDescriptorFactory.fromResource(R.drawable.mercado)
                        else -> BitmapDescriptorFactory.fromResource(R.drawable.marcador)
                    }

                    var distancia = calcularDistancia(miUbi.latitude, miUbi.longitude, Ubi.latitude,Ubi.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(Ubi)
                            .title(lugar.lugar)
                            .snippet("Distancia: ${distancia}km")
                            .icon(icono)
                            // TODO Aqui hariamos un when para separar cada una de las categorias (CHECK)
                    )
                    listaId.add(documento.id.toString())
                }
            }
    }

    override fun onMyLocationButtonClick(): Boolean {
        //mensaje("Boton pulsado")
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        mensaje("Estás en ${p0.latitude}, ${p0.longitude}")
        comm.passData(p0.latitude,p0.longitude)
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

    private fun calcularDistancia(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Double{
        // Convertir todas las coordenadas a radianes
        var lat1 = gradosARadianes(lat1)
        var lon1 = gradosARadianes(lon1)
        var lat2 = gradosARadianes(lat2)
        var lon2 = gradosARadianes(lon2)
        // Aplicar fórmula
        val RADIO_TIERRA_EN_KILOMETROS = 6371
        var lonDelta = (lon2 - lon1)
        var latDelta = (lat2 - lat1)
        val distancia = RADIO_TIERRA_EN_KILOMETROS * 2 * Math.asin(sqrt(cos(lat1) * cos(lat2) * Math.pow(sin(lonDelta / 2), 2.0) + Math.pow(sin(latDelta / 2), 2.0)))
        val d = (distancia * 10000.0).roundToInt() / 10000.0
        return d
    }

    fun gradosARadianes(grados: Double): Double {
        return grados * PI / 180
    }

    fun mensaje(cadena : String) {
        Toast.makeText(requireContext(), cadena, Toast.LENGTH_SHORT).show()
    }

    // Sección para la construccion de los extras en la ventana
    override fun onInfoWindowClick(p0: Marker) {
        popUpExtrainfo(p0.title!!)
    }

    private fun popUpExtrainfo(idMarker: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val extraInofPop = inflater.inflate(R.layout.location_popup,null)
        val lugarPop = extraInofPop.findViewById<TextInputEditText>(R.id.txtLugarPopUp)
        val descripcionPop = extraInofPop.findViewById<TextInputEditText>(R.id.txtDescripcionPopUp)
        val categoriaPop = extraInofPop.findViewById<TextInputEditText>(R.id.txtCategoriaPopUp)
        val estrellasPop = extraInofPop.findViewById<RatingBar>(R.id.estrellaBarPopUp)

        lugarPop.isEnabled = false
        descripcionPop.isEnabled=false
        categoriaPop.isEnabled=false
        estrellasPop.isEnabled = false

        baseRemota.whereEqualTo("lugar", idMarker).get().addOnSuccessListener {
            for(place in it){
                lugarPop.setText(place.getString("lugar").toString())
                descripcionPop.setText(place.getString("descripcion").toString())
                categoriaPop.setText(place.getString("categoria").toString())
                estrellasPop.rating = place.getLong("estrella").toString().toFloat()
            }// fin del for para el llenado de info
        }// fin del add on success

        with(builder){
                setPositiveButton("Salir"){_,_ ->}
                .setView(extraInofPop)
                .show()
        }

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        mensaje("Presione la ventana de información para mas detalles")
        return false
    }

}