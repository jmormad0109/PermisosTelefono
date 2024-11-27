package com.example.permisostelefono

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.permisostelefono.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var enlaceVista: ActivityMainBinding

    override fun onCreate(estadoGuardado: Bundle?) {
        super.onCreate(estadoGuardado)

        enlaceVista = ActivityMainBinding.inflate(layoutInflater)
        setContentView(enlaceVista.root)

        enlaceVista.btnAct2.setOnClickListener {
            val intento = Intent(this, SecondActivity::class.java)
            startActivity(intento)
        }

        enlaceVista.btnCorreo.setOnClickListener {
            abrirCorreo()
        }

        enlaceVista.btnMapa.setOnClickListener {
            abrirGoogleMaps()
        }

        enlaceVista.btnNavegador.setOnClickListener {
            abrirNavegador()
        }

        enlaceVista.btnDados.setOnClickListener {
            val intento = Intent(this, DadosActivity::class.java)
            startActivity(intento)
        }

        enlaceVista.btnChistes.setOnClickListener {
            val intento = Intent(this, ChistesActivity::class.java)
            startActivity(intento)
        }
    }

    private fun abrirCorreo() {
        val correoIntento = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("juliomorenomadueno@gmail.com"))
        }

        if (correoIntento.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(correoIntento, "Elige una aplicación de correo"))
        } else {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirGoogleMaps() {
        val ubicacion = Uri.parse("https://www.google.com/maps")
        val mapaIntento = Intent(Intent.ACTION_VIEW, ubicacion)

        startActivity(mapaIntento)
    }

    private fun abrirNavegador() {
        val url = "https://www.google.com"
        val navegadorIntento = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(navegadorIntento);

    }
}
