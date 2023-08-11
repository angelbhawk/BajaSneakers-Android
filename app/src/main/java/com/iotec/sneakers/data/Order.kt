package com.iotec.sneakers.data

import com.google.gson.Gson
import com.iotec.sneakers.WebServiceREST
import com.iotec.sneakers.url
import java.io.Serializable

class Order (
    var id:Int = 0,
    var amount:String = "",
    var status:String = "",
    var dateapplication:String = "",
    var customerid:String = ""
): Serializable {
    companion object {
        suspend fun obtener(text: String): MutableList<Order>? {
            val respuesta = WebServiceREST.get(url + "/ordenes/" + text)
            if (respuesta != "false") {
                val lista = Gson().fromJson<Array<Order>>(respuesta, Array<Order>::class.java)
                return lista.toMutableList()
            }
            return null
        }
    }
}