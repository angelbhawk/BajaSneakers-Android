package com.iotec.sneakers.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iotec.sneakers.LoginActivity
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.databinding.FragmentGalleryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CatalogoFragment : Fragment() {

    private lateinit var root: View;
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_gallery, container, false)

        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buscar = view.findViewById<ImageView>(R.id.btnBuscar)
        val barra = view.findViewById<EditText>(R.id.etBuscar)

        val actividad = (activity as MainActivity)
        //actividad.showAlert("inicio")

        buscar.setOnClickListener{
            val scope = CoroutineScope(Job())
            scope.launch {
                val lista = Product.buscar(barra.text.toString())

                if(lista != null)
                {
                    requireActivity().runOnUiThread {
                        val adaptador = ProductosAdapter(lista, {
                            mostrarDetalle(it)
                        })
                        val rvProductos = view.findViewById<RecyclerView>(R.id.rvProductos)
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
                        actividad.showAlert("No se han encontrado resultados")
                    }

                }

            }
        }

        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Product.todos()

            if(lista != null)
            {
                requireActivity().runOnUiThread {
                    val adaptador = ProductosAdapter(lista, {
                        mostrarDetalle(it)
                    })
                    val rvProductos = view.findViewById<RecyclerView>(R.id.rvProductos)
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
        findNavController().navigate(R.id.action_nav_busqueda_to_productoFragment, c)

    }
}