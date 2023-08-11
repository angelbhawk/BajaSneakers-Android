package com.iotec.sneakers.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iotec.sneakers.LoginActivity
import com.iotec.sneakers.R

class RegDatosFragment : Fragment() {

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
        val i = inflater.inflate(R.layout.fragment_reg_datos, container, false)
        val actividad = (activity as LoginActivity)

        val btnSiguiente = i.findViewById<Button>(R.id.btnNext2)
        val etNombre = i.findViewById<EditText>(R.id.etRFirstName)
        val etApellidos = i.findViewById<EditText>(R.id.etRLastName)
        val generoSel = i.findViewById<Spinner>(R.id.spTallas)
        val tvRegres = i.findViewById<TextView>(R.id.tvHave2)

        btnSiguiente.setOnClickListener {
            if(etNombre.text.toString() != "" && etApellidos.text.toString() != "" && generoSel.selectedItem.toString() != "")
            {
                val c = Bundle()
                c.putString("firstname", etNombre.text.toString())
                c.putString("lastname", etApellidos.text.toString())
                c.putString("gender", generoSel.selectedItem.toString())
                findNavController().navigate(R.id.action_regDatosFragment_to_regAccesFragment, c)
            }
            else
            {
                actividad.showAlert()
            }
        }

        tvRegres.setOnClickListener {
            findNavController().navigate(R.id.action_regDatosFragment_to_loginFragment)
        }

        return i
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegDatosFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}