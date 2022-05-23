package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.R

class CustomAdapter (private val data:List<Lugar>) : RecyclerView.Adapter<CustomAdapter.LugarViewHolder>() {

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
        var itemImage = itemView.findViewById<ImageView>(R.id.icono)
        var itemTitulo =  itemView.findViewById<TextView>(R.id.txtTitulo)
        var itemCategoria =  itemView.findViewById<TextView>(R.id.txtCategoria)
        var itemDescripcion =  itemView.findViewById<TextView>(R.id.txtDescripcion)
        var itemEstrellas = itemView.findViewById<RatingBar>(R.id.txtEstrellas)

        fun render(lugar: Lugar) {
            itemTitulo.setText(lugar.lugar)
            itemCategoria.setText(lugar.categoria)
            itemEstrellas.rating = lugar.estrellas
            itemDescripcion.setText(lugar.descripcion)

            when(lugar.categoria) {
                "Iglesias" -> itemImage.setImageResource(R.drawable.iglesia)
                "Restaurantes" -> itemImage.setImageResource(R.drawable.restaurante)
                "Hoteles" -> itemImage.setImageResource(R.drawable.hotel)
                "Plazas" -> itemImage.setImageResource(R.drawable.plaza)
                "Museos" -> itemImage.setImageResource(R.drawable.museo)
                "Edificios Administrativos" -> itemImage.setImageResource(R.drawable.edificio)
                "Tiendas Departamentales" -> itemImage.setImageResource(R.drawable.tienda)
                "Centrales" -> itemImage.setImageResource(R.drawable.bus)
                "Cafes" -> itemImage.setImageResource(R.drawable.cafe)
                "Mercados" -> itemImage.setImageResource(R.drawable.mercado)
                else -> BitmapDescriptorFactory.fromResource(R.drawable.marcador)
            }
        }
    }
}