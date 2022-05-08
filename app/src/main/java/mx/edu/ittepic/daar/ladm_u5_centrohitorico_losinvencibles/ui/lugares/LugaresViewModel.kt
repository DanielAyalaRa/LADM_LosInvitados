package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.ui.lugares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LugaresViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de Inicio"
    }
    val text: LiveData<String> = _text
}