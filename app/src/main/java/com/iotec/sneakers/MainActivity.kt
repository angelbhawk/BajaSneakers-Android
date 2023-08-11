package com.iotec.sneakers

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.iotec.sneakers.data.Customer
import com.iotec.sneakers.data.Product
import com.iotec.sneakers.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

enum class ProviderType{
    BASIC
}

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_principal, R.id.nav_busqueda, R.id.nav_carrito//, R.id.deseadoFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val ivIcono = navView.getHeaderView(0).findViewById<ImageView>(R.id.ivIcono)

        val tvNombre = navView.getHeaderView(0).findViewById<TextView>(R.id.tvNombreC)
        val tvCorreo = navView.getHeaderView(0).findViewById<TextView>(R.id.tvCorreoE)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        if(email != null)
        {
            val scope = CoroutineScope(Job())
            scope.launch {
                var cus = Customer.buscar(email ?: "")
                if(cus != null)
                {
                    var yn = false;
                    while(true)
                    {
                        yn = false;
                        Thread.sleep(15_000)
                        var cus = Product.lista(email ?: "")

                        if (cus != null) {
                            if (cus.count() > 0) {
                                this@MainActivity.runOnUiThread {
                                    crearNotificacion("Tienes productos de tu lista de deseados en descuento")

                                }
                            }
                        }


                    }
                }
            }
        }

        // email ?: ""
        val scope = CoroutineScope(Job())
        scope.launch {
            var resultado = Customer.login(email ?: "")
            var customerr = Customer.buscar(email?:"")

            this@MainActivity.runOnUiThread {
                if(resultado != null) {
                    tvNombre.text = resultado.first_name + " " + resultado.last_name
                    tvCorreo.text = resultado.email
                    if(customerr!= null)
                    {
                        updateIcon(customerr);
                    }
                    else
                        updateIcon(resultado);
                }
                else
                {
                    showAlert("xd");
                }
            }
        }

        val RESULT_OK = 1;

        var cameraActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //if (result.resultCode == RESULT_OK) {
                val fileName = "tmp.jpg"
                val bitmap = result.data?.extras?.get("data")
                if (bitmap is Bitmap) {
                    this@MainActivity.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    val main = this@MainActivity as MainActivity
                    val scope = CoroutineScope(Job())
                    scope.launch {
                        val respuesta = WebServiceREST.postFile(
                            url + "/modificarImagen",
                            listOf(
                                Parametro("email", email ?: "")
                            ), "imagen",  fileName,
                            this@MainActivity.openFileInput(fileName)
                        )
                        if (respuesta == "ok") {
                            // cambios

                            val bitmap = BitmapFactory.decodeStream(
                                this@MainActivity.openFileInput(fileName).readBytes().inputStream())
                            this@MainActivity.runOnUiThread {
                                ivIcono.setImageBitmap(bitmap)
                            }
                        }
                        else
                        {
                            Log.e("myTag", "aaaa"+respuesta);
                        }
                    }
                }
                //ivCamara.setImageBitmap(imageBitmap)
                //}
            }

        fun dispatchTakePictureIntent() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this@MainActivity.packageManager) != null) {
                cameraActivityResultLauncher.launch(takePictureIntent)
            }
        }

        ivIcono.setOnClickListener{
            val scope = CoroutineScope(Job())
            scope.launch {
                this@MainActivity.runOnUiThread {
                    val main = this@MainActivity as MainActivity
                    dispatchTakePictureIntent()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showAlert(mensaje:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun getmail():String
    {
        val bundle = intent.extras
        val email = bundle?.getString("email")
        return email?:"";
    }

    fun updateIcon(cliente:Customer){
        val fileName = cliente.id.toString()+".jpeg"
        val file = File(filesDir, fileName )
        val scope = CoroutineScope(Job())
        if (file.exists()) {
            scope.launch{
                val bitmap = BitmapFactory.decodeStream(
                    openFileInput(fileName).readBytes().inputStream())
                this@MainActivity.runOnUiThread {
                    val ivIcono = findViewById<ImageView>(R.id.ivIcono)
                    ivIcono.setImageBitmap(bitmap)
                }
            }
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/images/customers/" + fileName)
                bitmap?.let {
                    openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    this@MainActivity.runOnUiThread {
                        val ivIcono = findViewById<ImageView>(R.id.ivIcono)
                        ivIcono.setImageBitmap(bitmap)
                    }
                }
            }
        }
        else{
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/images/customers/" + fileName)
                bitmap?.let {
                    openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    this@MainActivity.runOnUiThread {
                        val ivIcono = findViewById<ImageView>(R.id.ivIcono)
                        ivIcono.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    val CHANNEL_ID = "Canal1"
    val NOTIFICATION_ID = 123

    private fun createNotificationChannel() {
        val name = "Canal1"
        val descriptionText = "Descripcion del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun crearNotificacion(messageBody: String) {
        createNotificationChannel()
        val intent = Intent(this, MainActivity::class.java)
        intent.extras?.putString("Mensaje", messageBody)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val resultIntent: PendingIntent =
            PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationSoundURI: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mNotificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this,"Canal")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Practica")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(resultIntent)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build())
    }
}