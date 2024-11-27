package com.example.permisostelefono

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.permisostelefono.databinding.ActivityDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.EditText

class DadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDadosBinding
    private var suma: Int = 0
    private var tiempoEntreTiradas: Long = 1000

    private val carasDados = listOf(
        R.drawable.dado1,
        R.drawable.dado2,
        R.drawable.dado3,
        R.drawable.dado4,
        R.drawable.dado5,
        R.drawable.dado6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEventListeners()
    }

    private fun initEventListeners() {
        binding.imageViewCubilete.setOnClickListener {
            lanzarJuego()
        }

        binding.radioGroupTiempo.setOnCheckedChangeListener { _, checkedId ->
            tiempoEntreTiradas = when (checkedId) {
                R.id.radioButton1Seg -> {
                    showToast("Tiempo entre tiradas: 1 segundo")
                    1000
                }
                R.id.radioButton3Seg -> {
                    showToast("Tiempo entre tiradas: 3 segundos")
                    3000
                }
                else -> 1000
            }
        }
    }

    private fun lanzarJuego() {
        programarTiradas()
    }

    private fun programarTiradas() {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val numeroTiradas = 5

        for (i in 1..numeroTiradas) {
            schedulerExecutor.schedule(
                {
                    realizarTirada()
                },
                tiempoEntreTiradas * i, TimeUnit.MILLISECONDS
            )
        }


        schedulerExecutor.schedule(
            {
                verificarNumeroUsuario()
            },
            tiempoEntreTiradas * (numeroTiradas + 1), TimeUnit.MILLISECONDS
        )

        schedulerExecutor.shutdown()
    }

    private fun realizarTirada() {
        val dados = arrayOf(
            Random.nextInt(1, 7),
            Random.nextInt(1, 7),
            Random.nextInt(1, 7)
        )


        suma = dados.sum()


        runOnUiThread {
            animarRotacion(binding.imageView1)
            animarRotacion(binding.imageView2)
            animarRotacion(binding.imageView3)

            actualizarDado(binding.imageView1, dados[0])
            actualizarDado(binding.imageView2, dados[1])
            actualizarDado(binding.imageView3, dados[2])
        }
    }

    private fun actualizarDado(imageView: ImageView, cara: Int) {
        imageView.setImageResource(carasDados[cara - 1])
    }

    private fun verificarNumeroUsuario() {
        val numeroIntroducido = binding.etxtNumero.text.toString().toIntOrNull()

        if (numeroIntroducido != null && numeroIntroducido in 3..18) {
            if (numeroIntroducido == suma) {
                showToast("¡FELICIDADES, has acertado el número!")
            } else {
                showToast("No has acertado el número, prueba otra vez.")
            }
        } else {
            showToast("Por favor, ingresa un número válido entre 3 y 18.")
        }
    }

    private fun animarRotacion(imageView: ImageView) {
        val rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        rotation.duration = 500
        rotation.interpolator = DecelerateInterpolator()
        rotation.start()
    }

    private fun showToast(mensaje: String) {
        runOnUiThread {
            android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
