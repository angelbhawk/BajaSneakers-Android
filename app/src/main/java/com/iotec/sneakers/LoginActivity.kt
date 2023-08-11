package com.iotec.sneakers

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.iotec.sneakers.R
import com.iotec.sneakers.data.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    var id:Int=0
    var logeado:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.AuthTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    // Firebase
    fun login(email: String, password : String) {
        if(email != null && password != null)
        {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(email, ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
        }
    }

    fun register(email: String, password : String, nombre: String, apellido: String, genero: String) {
        if(email != null && password != null)
        {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){

                        showHome2(email, nombre, apellido, genero, ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
        }
    }

    // Sneakers
    fun load() {
        val scope = CoroutineScope(Job())
        scope.launch {
            val mensaje = Customer.test()
            this@LoginActivity.runOnUiThread {
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("Error")
                builder.setMessage("Se ha producido un error autenticando al usuario")
                builder.setPositiveButton("Aceptar", null)
                if (mensaje == true) {
                    builder.setMessage(""+mensaje)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    builder.setMessage(""+mensaje)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    // Auxiliares
    fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlert(mensaje:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showHome(email: String, provider: ProviderType) {
        val scope = CoroutineScope(Job())
        scope.launch {
            var resultado = Customer.login(email)
            this@LoginActivity.runOnUiThread {
                if(resultado != null) {
                    val homeIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("email", email)
                        putExtra("provider", provider.name)
                    }
                    startActivity(homeIntent)
                }
                else
                {
                    showAlert("xd");
                }
            }
        }
    }

    fun showHome2(email: String, nombre: String, apellido: String, genero: String, provider: ProviderType) {
        val scope = CoroutineScope(Job())
        scope.launch {
            var nuevo = Customer.register(email, nombre, apellido, genero)
            if(nuevo != null)
            {
                var resultado = Customer.login(email)
                this@LoginActivity.runOnUiThread {
                    if(resultado != null) {
                        val homeIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("email", email)
                            putExtra("provider", provider.name)
                        }
                        startActivity(homeIntent)
                    }
                    else
                    {
                        showAlert("xd 4");
                    }
                }
            }
            else
            {
                showAlert("xd 3")
            }
        }
    }
}