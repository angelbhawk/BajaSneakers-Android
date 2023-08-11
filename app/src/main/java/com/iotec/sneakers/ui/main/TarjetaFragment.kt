package com.iotec.sneakers.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.iotec.sneakers.R

class TarjetaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_tarjeta, container, false)

        val btntvolver = v.findViewById<Button>(R.id.btnRegre)
        val btntlist = v.findViewById<Button>(R.id.btnList)

        btntvolver.setOnClickListener{
            findNavController().navigate(R.id.action_tarjetaFragment_to_direccionFragment)
        }

        btntlist.setOnClickListener{
            findNavController().navigate(R.id.action_tarjetaFragment_to_compraFragment)
        }

        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TarjetaFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}