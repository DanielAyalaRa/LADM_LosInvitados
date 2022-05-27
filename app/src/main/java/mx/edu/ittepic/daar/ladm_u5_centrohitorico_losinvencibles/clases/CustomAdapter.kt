package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.storage.FirebaseStorage
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.R
import java.io.File

class CustomAdapter (private val data:List<Lugar>) : RecyclerView.Adapter<CustomAdapter.LugarViewHolder>() {
    var listaURL = ArrayList<String>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LugarViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_view, viewGroup, false)
        return LugarViewHolder(v)
    }

    override fun onBindViewHolder(holder : LugarViewHolder, i: Int) {
        val item = data[i]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class LugarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemIcono = itemView.findViewById<ImageView>(R.id.icono)
        var itemTitulo =  itemView.findViewById<TextView>(R.id.txtTitulo)
        var itemCategoria =  itemView.findViewById<TextView>(R.id.txtCategoria)
        var itemDescripcion =  itemView.findViewById<TextView>(R.id.txtDescripcion)
        var itemEstrellas = itemView.findViewById<TextView>(R.id.txtEstrellas)
        var itemImagen1 = itemView.findViewById<ImageView>(R.id.foto1)
        var itemImagen2 = itemView.findViewById<ImageView>(R.id.foto2)
        var itemImagen3 = itemView.findViewById<ImageView>(R.id.foto3)

        fun render(lugar: Lugar) {
            var ruta = "${lugar.categoria}/${lugar.lugar}"
            val storageRef = FirebaseStorage.getInstance().reference.child(ruta)

            storageRef.listAll()
                .addOnSuccessListener {
                    listaURL.clear()
                    var i = 0
                    it.items.forEach {
                        //listaURL.add(it.name)
                        if (i == 0) {
                            llenar(ruta,it.name,itemImagen1)
                        }
                        if (i == 1) {
                            llenar(ruta,it.name,itemImagen2)
                        }
                        if (i == 2) {
                            llenar(ruta,it.name,itemImagen3)
                        }
                        i++
                    } // fin del for
                } // fin de la consulta

            itemTitulo.setText(lugar.lugar)
            itemCategoria.setText(lugar.categoria)
            itemEstrellas.setText("${lugar.estrellas}★")
            itemDescripcion.setText(lugar.descripcion)

            when(lugar.categoria) {
                "Iglesias" -> itemIcono.setImageResource(R.drawable.iglesia)
                "Restaurantes" -> itemIcono.setImageResource(R.drawable.restaurante)
                "Hoteles" -> itemIcono.setImageResource(R.drawable.hotel)
                "Plazas" -> itemIcono.setImageResource(R.drawable.plaza)
                "Museos" -> itemIcono.setImageResource(R.drawable.museo)
                "Edificios Administrativos" -> itemIcono.setImageResource(R.drawable.edificio)
                "Tiendas Departamentales" -> itemIcono.setImageResource(R.drawable.tienda)
                "Centrales" -> itemIcono.setImageResource(R.drawable.bus)
                "Cafes" -> itemIcono.setImageResource(R.drawable.cafe)
                "Mercados" -> itemIcono.setImageResource(R.drawable.mercado)
                else -> BitmapDescriptorFactory.fromResource(R.drawable.marcador)
            }
        }
    }
    private fun llenar(ruta : String,nombre : String, itemImagen : ImageView) {
        val foto = FirebaseStorage.getInstance().reference.child("${ruta}/${nombre}")

        val archivoTemporal = File.createTempFile("imagenTemp",".jpg")

        foto.getFile(archivoTemporal)
            .addOnSuccessListener {
                val imagenBits = BitmapFactory.decodeFile(archivoTemporal.absolutePath)
                itemImagen.setImageBitmap(imagenBits)
            }
    }
}