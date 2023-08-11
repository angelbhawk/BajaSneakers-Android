package com.iotec.sneakers.ui.main

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.WebServiceREST
import com.iotec.sneakers.data.Customer
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class ProductoFragment : Fragment() {

    lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_producto, container, false)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombre = view.findViewById<TextView>(R.id.tvTitulo)
        val tvDescri = view.findViewById<TextView>(R.id.tvDescripcion)
        val tvPrecio = view.findViewById<TextView>(R.id.tvPre)
        val ivFoto = view.findViewById<ImageView>(R.id.ivFoto)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregar)
        val ivCal = view.findViewById<ImageView>(R.id.ivCal)

        val actividad = (activity as MainActivity)
        //actividad.showAlert("inicio")

        btnAgregar.setOnClickListener{
            val scope = CoroutineScope(Job())
            scope.launch {
                val resultado = Product.agregarc(actividad.getmail(),id)
                if(resultado != false)
                {
                    actividad.runOnUiThread {
                        actividad.showAlert("Haz agregado un producto a tu carrito")
                    }
                }
            }
        }

        ivCal.setOnClickListener{
            val scope = CoroutineScope(Job())
            scope.launch {
                val resultado = Product.agregarl(actividad.getmail(),id)
                if(resultado != false)
                {
                    actividad.runOnUiThread {
                        actividad.showAlert("Haz agregado un producto a tu lista de deseados")
                    }

                }
            }
        }

        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.consultar(id)
            if(lista != null)
            {
                tvNombre.text = lista.name
                tvDescri.text = lista.description
                tvPrecio.text = lista.price

                val actividad = (activity as MainActivity)
                val fileName = lista.id.toString()+".jpeg"
                val file = File(actividad.filesDir, fileName )
                val scope = CoroutineScope(Job())
                if (file.exists()) {
                    scope.launch{
                        val bitmap = BitmapFactory.decodeStream(
                            actividad.openFileInput(fileName).readBytes().inputStream())
                        actividad.runOnUiThread {
                            ivFoto.setImageBitmap(bitmap)
                        }
                    }
                    scope.launch {
                        val bitmap = WebServiceREST.getImage(url + "/storage/images/products/" + fileName)
                        bitmap?.let {
                            actividad.openFileOutput(fileName, Context.MODE_PRIVATE)
                                .use { fos ->
                                    it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                                }
                            actividad.runOnUiThread {
                                ivFoto.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
                else{
                    scope.launch {
                        val bitmap = WebServiceREST.getImage(url + "/storage/images/products/" + fileName)
                        bitmap?.let {
                            actividad.openFileOutput(fileName, Context.MODE_PRIVATE)
                                .use { fos ->
                                    it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                                }
                            actividad.runOnUiThread {

                                ivFoto.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProductoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}