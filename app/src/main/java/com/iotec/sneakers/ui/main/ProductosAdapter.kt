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
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class ProductosAdapter(var lista:MutableList<Product>,
                       val clickListener:(Product)-> Unit): RecyclerView.Adapter<ProductosAdapter.Contenedor>()
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
        fun bind(producto: Product, clickListener: (Product) -> Unit) {
            view.setOnClickListener { clickListener(producto)}
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i:Int):Contenedor  {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.productos, viewGroup, false);
        return (Contenedor(view))
    }

    override fun onBindViewHolder(contenedor:Contenedor, i:Int) {
        contenedor.tvNombre.setText(lista[i].name + " " + lista[i].color)
        contenedor.tvMarca.setText(lista[i].brand)
        contenedor.tvPrecio.setText("$"+lista[i].price.toString())
        contenedor.ivImagen.setImageResource(android.R.drawable.ic_menu_gallery)
        val fileName = lista[i].id.toString()+".jpeg"
        val file = File(contenedor.view.context.filesDir, fileName )
        val scope = CoroutineScope(Job())
        if (file.exists()) {
            scope.launch{
                val bitmap = BitmapFactory.decodeStream(
                    contenedor.view.context.openFileInput(fileName).readBytes().inputStream())
                (contenedor.view.context as Activity).runOnUiThread {
                    contenedor.ivImagen.setImageBitmap(bitmap)
                }
            }
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/images/products/" + fileName)
                bitmap?.let {
                    contenedor.view.context.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    (contenedor.view.context as Activity).runOnUiThread {
                        contenedor.ivImagen.setImageBitmap(bitmap)
                    }
                }
            }
        }
        else{
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/images/products/" + fileName)
                bitmap?.let {
                    contenedor.view.context.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    (contenedor.view.context as Activity).runOnUiThread {
                        contenedor.ivImagen.setImageBitmap(bitmap)
                    }
                }
            }
        }
        contenedor.bind(lista[i], clickListener)
    }


    override fun getItemCount(): Int {
        return lista.size
    }
}
