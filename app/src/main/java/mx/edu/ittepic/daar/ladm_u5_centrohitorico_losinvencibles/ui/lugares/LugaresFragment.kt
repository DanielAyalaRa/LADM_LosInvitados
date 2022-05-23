package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.lugares

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Ubicacion
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentLugaresBinding
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.inicio.InicioFragment


class LugaresFragment : Fragment() {

    var _binding: FragmentLugaresBinding? = null
    val binding get() = _binding!!
    private val baseRemota = FirebaseFirestore.getInstance().collection("Lugares")

    var contador = 0
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var locacion : LocationManager
    var latitudIn :Double? = 0.0
    var longitudeIn :Double? = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLugaresBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                InicioFragment.REQUEST_CODE_LOCATION
            )
        }//permisos

        //Obtenemos del sistema el localizador GPS en tiempo real
        locacion = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var ubi = Ubicacion(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, ubi)

        latitudIn = arguments?.getDouble("latitud")
        longitudeIn = arguments?.getDouble("longitud")

        binding.txtLatitud.setText(latitudIn.toString())
        binding.txtLongitud.setText(longitudeIn.toString())
        //alerta("Latitud: $latitudIn \n$longitudeIn")

        binding.btnTomarFoto.setOnClickListener {
            if (contador == 2) {
                alerta("Solo tienes permitido agregar 3 fotos")
                return@setOnClickListener
            }
            dispararFoto()
            contador++
        }

        binding.btnInsertar.setOnClickListener {
            if(validarCampos()){
                val datos = hashMapOf(
                    "descripcion" to binding.txtDescripcion.text.toString(),
                    "latitud" to binding.txtLatitud.text.toString().toDouble(),
                    "longitud" to binding.txtLongitud.text.toString().toDouble(),
                    "lugar" to binding.txtLugar.text.toString(),
                    "categoria" to binding.txtCategoria.text.toString(),
                    "estrella" to binding.estrellaBar.rating.toString().toFloat()
                )
                baseRemota.add(datos)
                    .addOnSuccessListener {
                        mensaje("Se guardo el lugar")
                        binding.txtDescripcion.text?.clear()
                        binding.txtLugar.text?.clear()
                        binding.txtLatitud.text?.clear()
                        binding.txtLongitud.text?.clear()
                        binding.txtCategoria.text?.clear()
                        binding.estrellaBar.rating = 0.0F
                    }
                    .addOnFailureListener {
                        alerta("Error... \n${it.message}")
                    }
            }// final del if para ingresar campos
            alerta("Campos vacios... revisalos")
            return@setOnClickListener
        }

        return root
    }

    private fun validarCampos(): Boolean {
        val lugar = binding.txtLugar.text
        val descripcion = binding.txtDescripcion.text
        val categoria = binding.txtCategoria.text
        val lat = binding.txtLatitud.text
        val long = binding.txtLongitud.text
        if((lugar.toString().isEmpty() || lugar.toString().isBlank()) && (descripcion.toString().isEmpty() || descripcion.toString().isBlank()) &&
                (categoria.toString().isEmpty() || categoria.toString().isBlank())){
            return false
        }
        if(lat.toString().isEmpty() || lat.toString().isBlank()) return false
        if(long.toString().isEmpty() || long.toString().isBlank()) return false

        return true
    }

    private fun dispararFoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            data?.extras?.let { bundle ->
                val imageBitmap = bundle.get("data") as Bitmap
                binding.foto.setImageBitmap(imageBitmap)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun alerta(cadena: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("ATENCIÓN")
            .setMessage(cadena)
            .show()
    }

    fun mensaje(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }
}
