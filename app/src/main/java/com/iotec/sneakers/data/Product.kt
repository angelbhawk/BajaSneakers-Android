package com.iotec.sneakers.data
import com.google.gson.Gson
import com.iotec.sneakers.WebServiceREST
import com.iotec.sneakers.url
import java.io.Serializable

class Product (
    var id:Int = 0,
    var name:String = "",
    var price:String = "",
    var brand:String = "",
    var gender:String = "",
    var description:String = "",
    var type:String = "",
    var discount:String = "",
    var color: String
): Serializable {
    companion object{
        suspend fun buscar(text:String): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/showProducts/"+text)
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }
        suspend fun todos(): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/showProducts/0")
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }
        suspend fun vendidos(): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/populares")
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }
        suspend fun ofertas(): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/ofertados")
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }
        suspend fun consultar(id:String): Product?
        {
            val respuesta = WebServiceREST.get(url+"/showProduct/"+id)
            if (respuesta!="false") {
                return Gson().fromJson<Product>(respuesta, Product::class.java)
            }
            return null
        }

        suspend fun lista(email:String): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/mostrarl/"+email)
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }

        suspend fun carrito(email:String): MutableList<Product>?
        {
            val respuesta = WebServiceREST.get(url+"/mostrarc/"+email)
            if (respuesta!="false") {
                val lista = Gson().fromJson<Array<Product>>(respuesta, Array<Product>::class.java)
                return lista.toMutableList()
            }
            return null
        }

        suspend fun agregarl(email:String, id:String): Boolean?
        {
            val respuesta = WebServiceREST.get(url+"/agregarl/"+email+"/"+id)
            return true
        }

        suspend fun agregarc(email:String, id:String): Boolean?
        {
            val respuesta = WebServiceREST.get(url+"/agregarc/"+email+"/"+id+"/1")
            return true
        }

        suspend fun comprar(email: String, monto: String): Boolean?
        {
            val respuesta = WebServiceREST.get(url+"/ordenar/"+email+"/"+monto)
            return true
        }

        suspend fun eliminarc(email: String, id: String): Boolean?
        {
            val respuesta = WebServiceREST.get(url+"/quitarc/"+email+"/"+id)
            return true
        }

        suspend fun eliminarl(email: String, id: String): Boolean?
        {
            val respuesta = WebServiceREST.get(url+"/quitarl/"+email+"/"+id)
            return true
        }
    }
}