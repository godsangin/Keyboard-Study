package com.example.keyboardsecondmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.media.AudioManager
import android.inputmethodservice.Keyboard
import android.content.Context.AUDIO_SERVICE
import androidx.core.content.ContextCompat.getSystemService



class KeyboardEnglish {
    companion object{
        lateinit var englishLayout: LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener
        lateinit var context:Context
        lateinit var vibrator: Vibrator

        var isCaps:Boolean = false
        var buttons:MutableList<Button> = mutableListOf<Button>()

        fun newInstance(context:Context, layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            this.context = context
            this.englishLayout = layoutInflater.inflate(R.layout.keyboard_english, null) as LinearLayout
            this.inputConnection = inputConnection
            this.keyboardInterationListener = keyboardInterationListener
            this.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
            val height = sharedPreferences.getInt("keyboardHeight", 100)

            val firstLine = englishLayout.findViewById<LinearLayout>(R.id.first_line)
            val secondLine = englishLayout.findViewById<LinearLayout>(R.id.second_line)
            val thirdLine = englishLayout.findViewById<LinearLayout>(R.id.third_line)
            firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)


            buttons.add(englishLayout.findViewById<Button>(R.id.key_1))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_2))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_3))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_4))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_5))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_6))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_7))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_8))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_9))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_0))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_q))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_w))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_e))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_r))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_t))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_y))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_u))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_i))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_o))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_p))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_a))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_s))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_d))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_f))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_g))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_h))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_j))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_k))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_l))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_z))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_x))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_c))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_v))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_b))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_n))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_m))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_caps))
            buttons.add(englishLayout.findViewById<Button>(R.id.del))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_simbols))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_modechange))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_enter))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_rest))
            buttons.add(englishLayout.findViewById<Button>(R.id.key_dot))
            for(button in buttons){
                button.setOnClickListener(englishOnClickListener)
            }

            return englishLayout
        }

        val englishOnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
                val sound = sharedPreferences.getInt("keyboardSound", -1)
                val vibrate = sharedPreferences.getInt("keyboardVibrate", -1)
                if(vibrate > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, vibrate))
                    }
                    else{
                        vibrator.vibrate(100)
                    }
                }

                Log.d("view==", view?.id.toString())
                when (view?.id) {
                    R.id.key_caps -> {
                        modeChange()
                    }
                    R.id.del -> {
                        KeyboardEnglish.inputConnection.deleteSurroundingText(1, 0)
                    }
                    R.id.key_modechange -> {
                        keyboardInterationListener.modechange(1)
                    }
                    R.id.key_simbols -> {
                        keyboardInterationListener.modechange(2)
                    }
                    else -> {
                        playClick((view as Button).text.toString().toCharArray().get(0).toInt())
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
            val am = context.getSystemService(AUDIO_SERVICE) as AudioManager?
            when (i) {
                32 -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
                Keyboard.KEYCODE_DONE, 10 -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
                Keyboard.KEYCODE_DELETE -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
                else -> am!!.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, -1.toFloat())
            }
        }


    }
}