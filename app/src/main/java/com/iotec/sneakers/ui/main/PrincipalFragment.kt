package com.iotec.sneakers.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.data.Customer
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PrincipalFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actividad = (activity as MainActivity)
        //actividad.showAlert("inicio")
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.vendidos()

            if(lista != null)
            {
                requireActivity().runOnUiThread {
                    val adaptador = VendidosAdapter(lista, {
                        mostrarDetalle(it)
                    })
                    val rvProductos = view.findViewById<RecyclerView>(R.id.rvVendidos)
                    rvProductos.adapter = adaptador
                    val actividad = (activity as MainActivity)
                    //actividad.showAlert(lista[0].brand + " " + lista[0].name)
                    val gridLayout =
                        GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
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

            val lista2 = Product.ofertas()

            if(lista2 != null)
            {
                requireActivity().runOnUiThread {
                    val adaptador = ProductosAdapter(lista2, {
                        mostrarDetalle(it)
                    })
                    val rvProductos = view.findViewById<RecyclerView>(R.id.rvofertas)
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
        findNavController().navigate(R.id.action_nav_principal_to_productoFragment, c)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}