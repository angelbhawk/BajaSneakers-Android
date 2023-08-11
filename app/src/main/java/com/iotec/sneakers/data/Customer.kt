package com.iotec.sneakers.data
import com.google.gson.Gson
import com.iotec.sneakers.WebServiceREST
import com.iotec.sneakers.url

class Customer (
    var id:Int = 0,
    var email:String = "",
    var first_name:String = "",
    var last_name:String = "",
    var gender:String = ""
    ) {
        companion object{
            suspend fun login(correo:String):Customer?
            {
                val respuesta = WebServiceREST.get(url+"/login/"+correo)
                if (respuesta!="false") {
                    return Gson().fromJson<Customer>(respuesta, Customer::class.java)
                }
                return null
            }
            suspend fun register(correo:String, nombre:String, apellido:String, genero:String):Customer?
            {
                val respuesta = WebServiceREST.get(url+"/register/"+correo+"/"+nombre+"/"+apellido+"/"+genero)
                if (respuesta!="false") {
                    return Gson().fromJson<Customer>(respuesta, Customer::class.java)
                }
                return null
            }
            suspend fun buscar(correo:String):Customer?
            {
                val respuesta = WebServiceREST.get(url+"/cliente/"+correo)
                if (respuesta!="false") {
                    return Gson().fromJson<Customer>(respuesta, Customer::class.java)
                }
                return null
            }
            suspend fun test(): Boolean? {
                val respuesta = WebServiceREST.get(url+"/test")
                if (respuesta!="false") {
                    return true;
                }
                return null
            }
        }
    }