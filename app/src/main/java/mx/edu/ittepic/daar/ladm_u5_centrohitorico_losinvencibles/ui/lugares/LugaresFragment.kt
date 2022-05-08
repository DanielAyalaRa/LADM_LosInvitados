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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Imagen
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentLugaresBinding


open class LugaresFragment : Fragment() {

    private var _binding: FragmentLugaresBinding? = null
    private val binding get() = _binding!!

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
}
