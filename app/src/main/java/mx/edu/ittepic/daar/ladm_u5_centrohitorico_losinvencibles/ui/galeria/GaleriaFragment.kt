package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.galeria

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.R
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.CustomAdapter
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases.Lugar
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.FragmentGaleriaBinding

class GaleriaFragment : Fragment() {

    private var _binding: FragmentGaleriaBinding? = null
    private val binding get() = _binding!!
    private val baseRemota = FirebaseFirestore.getInstance().collection("Lugares")
    var vectorLugares = ArrayList<Lugar>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galeriaViewModel =
            ViewModelProvider(this).get(GaleriaViewModel::class.java)

        _binding = FragmentGaleriaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        baseRemota
            .addSnapshotListener { query, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }
                vectorLugares.clear()

                for (documento in query!!) {
                    var lugar = Lugar(requireContext())
                    lugar.lugar = documento.getString("lugar").toString()
                    lugar.descripcion = documento.getString("descripcion").toString()
                    lugar.categoria = documento.getString("categoria").toString()
                    lugar.latitud = documento.getDouble("latitud")!!
                    lugar.longitud = documento.getDouble("longitud")!!

                    vectorLugares.add(lugar)
                }

                val adapter = CustomAdapter(vectorLugares)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
            }

        // Agregar elementos de filtro
        val categoria = resources.getStringArray(R.array.categorias)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoria)
        binding.spCategoria.setAdapter(arrayAdapter)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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