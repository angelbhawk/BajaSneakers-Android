package com.iotec.sneakers.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iotec.sneakers.LoginActivity
import com.iotec.sneakers.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val actividad = (activity as LoginActivity)
        actividad.logeado = false

        val i = inflater.inflate(R.layout.fragment_login, container, false)

        val etCorreo = i.findViewById<EditText>(R.id.username)
        val etPassword = i.findViewById<EditText>(R.id.password)
        val btnLogin = i.findViewById<Button>(R.id.login)
        val tvRegister = i.findViewById<TextView>(R.id.register)

        btnLogin.setOnClickListener {
            try {
                actividad.login(etCorreo.text.toString(), etPassword.text.toString())
            }
            catch (e : Exception) {
                actividad.showAlert();
            }
        }

        tvRegister.setOnClickListener {
            try {
                actividad.load();
                findNavController().navigate(R.id.action_loginFragment_to_regDatosFragment)
            }
            catch (e : Exception) {
                actividad.showAlert();
            }
        }

        return i
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}