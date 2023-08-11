package com.iotec.sneakers.ui.main

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.R
import com.iotec.sneakers.WebServiceREST
import com.iotec.sneakers.data.Order
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class OrdenesAdapter(var lista:MutableList<Order>,
                     val clickListener:(Order)-> Unit): RecyclerView.Adapter<OrdenesAdapter.Contenedor>()
{
    class Contenedor(val view: View): RecyclerView.ViewHolder(view) {
        val tvNombre:TextView
        val ivImagen:ImageView
        val tvPrecio:TextView
        val tvMarca:TextView
        init {
            tvNombre = view.findViewById(R.id.tvNombreC)
            ivImagen = view.findViewById(R.id.ivImagen)
            tvPrecio = view.findViewById(R.id.tvPrecio)
            tvMarca = view.findViewById(R.id.tvBrand)
        }
        fun bind(orden: Order, clickListener: (Order) -> Unit) {
            view.setOnClickListener { clickListener(orden)}
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i:Int):Contenedor  {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.productos, viewGroup, false);
        return (Contenedor(view))
    }

    override fun onBindViewHolder(contenedor:Contenedor, i:Int) {
        contenedor.tvNombre.setText(lista[i].dateapplication)
        contenedor.tvMarca.setText(lista[i].status)
        contenedor.tvPrecio.setText("$"+lista[i].amount.toString())
        contenedor.ivImagen.setImageResource(android.R.drawable.ic_dialog_info)
    }


    override fun getItemCount(): Int {
        return lista.size
    }
}
