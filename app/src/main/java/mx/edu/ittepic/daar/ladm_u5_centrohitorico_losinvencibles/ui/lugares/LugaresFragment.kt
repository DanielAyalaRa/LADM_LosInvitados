package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.lugares

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Imagen
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentLugaresBinding


open class LugaresFragment : Fragment() {

    private var _binding: FragmentLugaresBinding? = null
    private val binding get() = _binding!!
    private val baseRemota = FirebaseFirestore.getInstance().collection("Lugares")

    var listaImagenes = ArrayList<Imagen>()
    var contador = 0
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lugaresViewModel =
            ViewModelProvider(this).get(LugaresViewModel::class.java)
        _binding = FragmentLugaresBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTomarFoto.setOnClickListener {
            if (contador == 2) {
                alerta("Solo tienes permitido agregar 3 fotos")
                return@setOnClickListener
            }
            dispararFoto()
            contador++
        }

        binding.btnInsertar.setOnClickListener {
            val datos = hashMapOf(
                "descripcion" to binding.txtDescripcion.text.toString(),
                "latitud" to binding.txtLatitud.text.toString().toDouble(),
                "longitud" to binding.txtLongitud.text.toString().toDouble(),
                "lugar" to binding.txtLugar.text.toString()
            )
            baseRemota.add(datos)
                .addOnSuccessListener {
                    mensaje("Se guardo el lugar")
                    binding.txtDescripcion.text?.clear()
                    binding.txtLugar.text?.clear()
                    binding.txtLatitud.text?.clear()
                    binding.txtLongitud.text?.clear()
                }
                .addOnFailureListener {
                    alerta("Error... \n${it.message}")
                }
        }

        return root
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
