package com.iotec.sneakers.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DeseadoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deseado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actividad = (activity as MainActivity)
        //actividad.showAlert("inicio")
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.lista(actividad.getmail())

            if(lista != null)
            {
                requireActivity().runOnUiThread {
                    val adaptador = ProductosAdapter(lista, {
                        mostrarDetalle(it)
                    })
                    val rvProductos = view.findViewById<RecyclerView>(R.id.rvDeseados)
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
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.eliminarl(actividad.getmail(), producto.id.toString())
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DeseadoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}