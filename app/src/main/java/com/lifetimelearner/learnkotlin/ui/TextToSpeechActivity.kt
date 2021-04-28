package com.lifetimelearner.learnkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.lifetimelearner.learnkotlin.R
import java.util.*

class TextToSpeechActivity : AppCompatActivity() {

    lateinit var speakBtn : MaterialButton
    lateinit var speakText : TextInputEditText
    lateinit var tts : TextToSpeech
    lateinit var ET_pitch : TextInputEditText
    lateinit var ET_speechRate : TextInputEditText
    lateinit var radioGroup : RadioGroup
    lateinit var spinner : Spinner
    private var flushMode = true
    private var speechRate = 1.0f
    private var pitch = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)

        speakText = findViewById(R.id.ET_TTS_Text)
        speakBtn = findViewById(R.id.BTN_TTS_Speak)
        ET_pitch = findViewById(R.id.ET_Pitch)
        ET_speechRate = findViewById(R.id.ET_Speech_Rate)
        radioGroup = findViewById(R.id.RG_Flush_Mode)
        spinner = findViewById(R.id.SPINNER_Select_Language)

        val list = arrayOf("US","UK","ENGLISH","CHINESE")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list)
        spinner.adapter = adapter

        ET_speechRate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    speechRate = 1.0f
                    tts.setSpeechRate(1.0f)
                    return
                }
                speechRate = s.toString().toFloat()
                tts.setSpeechRate(speechRate)
            }
        })

        ET_pitch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    pitch = 1.0f
                    tts.setPitch(1.0f)
                    return
                }
                pitch = s.toString().toFloat()
                tts.setPitch(pitch)
            }
        })

        radioGroup.setOnCheckedChangeListener { _ , checkedId ->
            when (checkedId) {
                R.id.RB_Flush_Mode_On -> flushMode = true
                R.id.RB_Flush_Mode_Off -> flushMode = false
            }
        }

        val text = speakText.text.toString()
        speakBtn.isEnabled = !text.isNullOrEmpty()

        tts = TextToSpeech(applicationContext) {
            if(it == TextToSpeech.SUCCESS) {
                // tts.language = Locale.US
                // tts.setSpeechRate(1.0f)
                val voice = Voice("Alexa",Locale.ENGLISH,Voice.QUALITY_VERY_LOW,Voice.LATENCY_VERY_LOW,false,null)
                tts.voice = voice
            }
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
            }
            override fun onDone(utteranceId: String?) {
            }
            override fun onError(utteranceId: String?) {
            }
        })

        speakText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                speakBtn.isEnabled = !s.isNullOrEmpty()
            }
        })

        speakBtn.setOnClickListener {
            val speakText = speakText.text.toString()
            val flush = if(flushMode) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            tts.speak(speakText,flush,null,"1001")
        }

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(list[position]) {
                    "US" -> tts.language = Locale.US
                    "UK" -> tts.language = Locale.UK
                    "ENGLISH" -> tts.language = Locale.ENGLISH
                    "CHINESE" -> {
                        tts.language = Locale.CHINESE
                        Toast.makeText(applicationContext,"CHINESE",Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

}
