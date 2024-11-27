package com.example.permisostelefono

import com.example.permisostelefono.databinding.ActivityChistesBinding


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import java.util.Locale

class ChistesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChistesBinding
    private lateinit var textToSpeech: TextToSpeech
    private val TOUCH_MAX_TIME = 500
    private var touchLastTime: Long = 0
    private lateinit var handler: Handler


    private val chistesArray = arrayOf(
        R.string.chiste1,
        R.string.chiste2,
        R.string.chiste3,
        R.string.chiste4,
        R.string.chiste5,
        R.string.chiste6,
        R.string.chiste7,
        R.string.chiste8,
        R.string.chiste9,
        R.string.chiste10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChistesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTextToSpeech()
        initHander()
        initEvent()
    }

    private fun initHander() {
        handler = Handler(Looper.getMainLooper())
        binding.progressBar.visibility = View.VISIBLE
        binding.btnExample.visibility = View.GONE

        Thread{
            Thread.sleep(3000)
            handler.post{
                binding.progressBar.visibility = View.GONE
                val description = getString(R.string.descripcion)
                speakMeDescription(description)
                binding.btnExample.visibility = View.VISIBLE

            }
        }.start()
    }

    private fun configureTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it != TextToSpeech.ERROR)
                textToSpeech.language = Locale.getDefault()
        })
    }

    private fun initEvent() {
        binding.btnExample.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            if (currentTime - touchLastTime < TOUCH_MAX_TIME) {

                val randomChisteId = chistesArray.random()
                val chiste = getString(randomChisteId)
                executorDoubleTouch(chiste)
            } else {
                speakMeDescription("Botón para escuchar un chiste")
            }
            touchLastTime = currentTime
        }
    }

    private fun speakMeDescription(s: String) {
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun executorDoubleTouch(chiste: String) {
        speakMeDescription(chiste)
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
