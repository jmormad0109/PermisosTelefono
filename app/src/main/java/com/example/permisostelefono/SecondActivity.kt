package com.example.permisostelefono

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permisostelefono.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var enlaceVista: ActivitySecondBinding
    private val CODIGO_SOLICITUD_LLAMADA = 1
    private var permisoDenegadoPreviamente = false  

    override fun onCreate(estadoGuardado: Bundle?) {
        super.onCreate(estadoGuardado)
        enlaceVista = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(enlaceVista.root)

        enlaceVista.btnLlamar.setOnClickListener {
            val numeroTelefono = enlaceVista.txtPhone.text.toString()
            if (numeroTelefono.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        realizarLlamada(numeroTelefono)
                    } else {
                        if (permisoDenegadoPreviamente) {
                            abrirConfiguracionApp()
                        } else {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), CODIGO_SOLICITUD_LLAMADA)
                        }
                    }
                } else {
                    realizarLlamada(numeroTelefono)
                }
            } else {
                Toast.makeText(this, "Introduce un número de teléfono válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun realizarLlamada(numeroTelefono: String) {
        val intento = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numeroTelefono"))
        startActivity(intento)
    }

    override fun onRequestPermissionsResult(codigoSolicitud: Int, permisos: Array<out String>, resultados: IntArray) {
        super.onRequestPermissionsResult(codigoSolicitud, permisos, resultados)

        if (codigoSolicitud == CODIGO_SOLICITUD_LLAMADA) {
            if (resultados.isNotEmpty() && resultados[0] == PackageManager.PERMISSION_GRANTED) {
                val numeroTelefono = enlaceVista.txtPhone.text.toString()
                realizarLlamada(numeroTelefono)
            } else {
                permisoDenegadoPreviamente = true
                Toast.makeText(this, "Permiso de llamada denegado. Necesitas habilitarlo manualmente en Configuración.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirConfiguracionApp() {
        Toast.makeText(this, "Permiso necesario. Actívelo en Configuración", Toast.LENGTH_LONG).show()
        val intento = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intento)
    }
}

