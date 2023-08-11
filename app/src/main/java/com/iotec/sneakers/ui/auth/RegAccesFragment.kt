package com.iotec.sneakers.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.iotec.sneakers.LoginActivity
import com.iotec.sneakers.R

class RegAccesFragment : Fragment() {
    lateinit var nombre    : String
    lateinit var apellidos : String
    lateinit var genero    : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nombre = it.getString("firstname").toString()
            apellidos = it.getString("lastname").toString()
            genero = it.getString("gender").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val i = inflater.inflate(R.layout.fragment_reg_acces, container, false)
        val actividad = (activity as LoginActivity)

        val btnRegistrar = i.findViewById<Button>(R.id.btnNext)
        val etCorreo = i.findViewById<EditText>(R.id.etREmail)
        val etCcorre = i.findViewById<EditText>(R.id.etCEmail)
        val etContra = i.findViewById<EditText>(R.id.etRPass)
        val etCcontr = i.findViewById<EditText>(R.id.etCPass)
        val tvRegres = i.findViewById<TextView>(R.id.tvHave)

        btnRegistrar.setOnClickListener {
            if(etCorreo.text.toString() != "" && etCcorre.text.toString() != "" && etContra.text.toString() != "" && etCcontr.text.toString() != "")
            {
                if(etCorreo.text.toString() == etCcorre.text.toString() && etContra.text.toString() == etCcontr.text.toString())
                {
                    actividad.register(etCorreo.text.toString(), etContra.text.toString(), nombre, apellidos, genero)
                }
                else
                {
                    actividad.showAlert("xd 2")
                }
            }
            else
            {
                actividad.showAlert("xd 1")
            }
        }

        tvRegres.setOnClickListener {
            findNavController().navigate(R.id.action_regAccesFragment_to_regDatosFragment)
        }

        return i
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegAccesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}