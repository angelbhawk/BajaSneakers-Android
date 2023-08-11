package com.iotec.sneakers.ui.main

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.iotec.sneakers.MainActivity
import com.iotec.sneakers.R
import com.iotec.sneakers.databinding.ActivityMainBinding
import java.util.*

class DireccionFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var ciudad = ""
    private var pais = ""
    private var direccion = ""
    private var estado = ""
    private var colonia = ""
    private var copo = ""
    private var calle1 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_direccion, container, false)

        val actividad = (activity as MainActivity)
        //Cambie aqui
        val gps = v.findViewById<ImageButton>(R.id.btnGps)
        val tpais = v.findViewById<TextView>(R.id.etPais)
        val tciudad = v.findViewById<TextView>(R.id.etCiudad)
        val tcolonia = v.findViewById<TextView>(R.id.etColonia)
        val tcopo = v.findViewById<TextView>(R.id.etCodigo)
        val testado = v.findViewById<TextView>(R.id.etEstado)
        val tcalle1 = v.findViewById<TextView>(R.id.etCalle1)
        val tcalle2 = v.findViewById<TextView>(R.id.etCalle2)
        val btndvolver = v.findViewById<Button>(R.id.btnVolver)
        val btndlisto = v.findViewById<Button>(R.id.btnListo)

        //Agregue esto
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(actividad)
        gps.setOnClickListener{
            if(ContextCompat.checkSelfPermission(actividad,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(actividad, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }else{
                val actividad = (activity as MainActivity)
                if (ActivityCompat.checkSelfPermission(
                        actividad,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        actividad,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if(location != null){ //Aqui los asigne
                        getCity(location.latitude, location.longitude)
                        tciudad.text = ciudad.toString() //te digo pues, no se que pdo jaja
                        tpais.text = pais.toString()
                        tcolonia.text = colonia.toString();
                        tcopo.text = copo.toString()
                        testado.text = estado
                        /*binding.direccion.text = direccion
                        binding.latitud.text = location.latitude.toString()
                        binding.longitud.text = location.longitude.toString()*/
                    }
                }
            }
        }

        btndvolver.setOnClickListener{
            findNavController().navigate(R.id.action_direccionFragment_to_nav_carrito)
        }

        btndlisto.setOnClickListener{
            findNavController().navigate(R.id.action_direccionFragment_to_tarjetaFragment)
        }

        return v
    }
    private fun getLocation(){ //este no, bueno si. Hace toda la chamba

    }

    //esto tambien we agregalo
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation()
            }
        }
    }
    companion object{ //esto tambien we
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    //Aqui se calculan los datos pai
    private fun getCity(lat:Double, long:Double){
        val actividad = (activity as MainActivity)
        try {
            val geoCoder = Geocoder(actividad, Locale.getDefault())
            val address = geoCoder.getFromLocation(lat,long,3)
            if(address != null){
                //aqui se asignan
                direccion = address[0].getAddressLine(0) //este te da el dato completo
                pais = address[0].countryName
                ciudad = address[0].locality
                colonia = address[0].subLocality
                copo = address[0].postalCode
                estado = address[0].adminArea
            }
        }catch (e:Exception){
            Toast.makeText(actividad, "Cargando Ciudad", Toast.LENGTH_SHORT).show()
        }
    }
}