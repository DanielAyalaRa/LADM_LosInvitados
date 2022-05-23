package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.clases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        //var itemImage = itemView.findViewById<ImageView>(R.id.imagen)
        var itemTitulo =  itemView.findViewById<TextView>(R.id.txtTitulo)
        var itemCategoria =  itemView.findViewById<TextView>(R.id.txtCategoria)
        var itemEstrellas =  itemView.findViewById<TextView>(R.id.txtEstrellas)
        var itemDescripcion =  itemView.findViewById<TextView>(R.id.txtDescripcion)

        fun render(lugar: Lugar) {
            //itemImage.setImageResource(lugar.imagen)
            itemTitulo.setText(lugar.lugar)
            itemCategoria.setText(lugar.categoria)
            itemEstrellas.setText(lugar.estrellas)
            itemDescripcion.setText(lugar.descripcion)
        }
    }
}