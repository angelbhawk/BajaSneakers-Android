package com.iotec.sneakers.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CompraFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_compra, container, false)

        val button = v.findViewById<Button>(R.id.btnFinalizar)

        button.setOnClickListener{
            findNavController().navigate(R.id.action_compraFragment_to_nav_principal)
        }

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var final = 0f;
        val total = view.findViewById<TextView>(R.id.tvfinalf)
        val btnComprar = view.findViewById<Button>(R.id.btnFinalizar)

        val actividad = (activity as MainActivity)
        //actividad.showAlert("inicio")

        btnComprar.setOnClickListener{
            val scope = CoroutineScope(Job())
            scope.launch {
                val lista = Product.comprar(actividad.getmail(), final.toString())
            }
        }

        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.carrito(actividad.getmail())

            if(lista != null)
            {
                for (elemento in lista)
                {
                    final += elemento.price.toFloat()
                }


                requireActivity().runOnUiThread {
                    total.text = final.toString()
                    val adaptador = ProductosAdapter(lista, {
                        mostrarDetalle(it)
                    })
                    val rvProductos = view.findViewById<RecyclerView>(R.id.rvCompra)
                    rvProductos.adapter = adaptador
                    val actividad = (activity as MainActivity)
                    //actividad.showAlert(lista[0].brand + " " + lista[0].name)
                    val gridLayout =
                        GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                    rvProductos.setLayoutManager(gridLayout)
                }
            }
            else
            {
                requireActivity().runOnUiThread {
                    val actividad = (activity as MainActivity)
                    actividad.showAlert("vacio")
                }

            }
        }


    }


    fun mostrarDetalle(producto: Product){
        val actividad = (activity as MainActivity)
        val c = Bundle()
        c.putString("id", producto.id.toString())
        findNavController().navigate(R.id.action_deseadoFragment_to_productoFragment, c)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CompraFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}