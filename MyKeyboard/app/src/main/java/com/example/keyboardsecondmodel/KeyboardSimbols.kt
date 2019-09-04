package com.example.keyboardsecondmodel

import android.content.Context
import android.inputmethodservice.Keyboard
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout

class KeyboardSimbols {
    companion object{
        lateinit var simbolsLayout: LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener
        var isCaps:Boolean = false
        var buttons:MutableList<Button> = mutableListOf<Button>()
        lateinit var vibrator: Vibrator
        lateinit var context:Context

        fun newInstance(context:Context, layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            this.simbolsLayout = layoutInflater.inflate(R.layout.keyboard_simbols, null) as LinearLayout
            this.inputConnection = inputConnection
            this.keyboardInterationListener = keyboardInterationListener
            this.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            this.context = context

            val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
            val height = sharedPreferences.getInt("keyboardHeight", 100)

            val firstLine = simbolsLayout.findViewById<LinearLayout>(R.id.first_line)
            val secondLine = simbolsLayout.findViewById<LinearLayout>(R.id.second_line)
            val thirdLine = simbolsLayout.findViewById<LinearLayout>(R.id.third_line)
            firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)

            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_0))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_2))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_3))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_4))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_5))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_6))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_7))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_8))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_9))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_0))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_plus))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_multiply))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_division))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_equal))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_slash))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_won))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_leftangle))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_rightangle))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_heart))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_star))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_exclamation))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_at))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_sharp))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_tilde))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_percent))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_circumflex))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_ampersand))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_asterisk))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_leftparenthesis))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_rightparenthesis))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_caps))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_minus))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_apostrophe))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_quotationmark))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_colon))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_semicolon))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_restin))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_questionmark))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.del))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_spacial))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_modechange))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_rest))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_space))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_dot))
            buttons.add(simbolsLayout.findViewById<Button>(R.id.key_enter))
            for(button in buttons){
                button.setOnClickListener(simbolsOnClickListener)
            }

            return simbolsLayout
        }

        val simbolsOnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
                val sound = sharedPreferences.getInt("keyboardSound", -1)
                val vibrate = sharedPreferences.getInt("keyboardVibrate", -1)
                if(vibrate > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(70, vibrate))
                    }
                    else{
                        vibrator.vibrate(70)
                    }
                }
                playClick((view as Button).text.toString().toCharArray().get(0).toInt())


                when (view?.id) {
                    R.id.key_caps -> {
                        modeChange()
                    }
                    R.id.key_space -> {
                        inputConnection.commitText(" ", 1)
                    }
                    R.id.del -> {
                        inputConnection.deleteSurroundingText(1, 0)
                    }
                    R.id.key_modechange -> {
                        keyboardInterationListener.modechange(1)
                    }
                    else -> {
                        inputConnection.commitText((view as Button).text.toString(), 1)
                    }
                }
            }
        }

        fun modeChange(){
            if(isCaps){
                isCaps = false
                for(button in buttons){
                    button.setText(button.text.toString().toLowerCase())
                }
            }
            else{
                isCaps = true
                for(button in buttons){
                    button.setText(button.text.toString().toUpperCase())
                }
            }
        }

        private fun playClick(i: Int) {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
            when (i) {
                32 -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
                Keyboard.KEYCODE_DONE, 10 -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
                Keyboard.KEYCODE_DELETE -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
                else -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, -1.toFloat())
            }
        }

    }
}