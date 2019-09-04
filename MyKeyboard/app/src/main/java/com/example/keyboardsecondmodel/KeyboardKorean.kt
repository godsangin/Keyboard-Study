package com.example.keyboardsecondmodel

import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class KeyboardKorean {
    companion object{
        lateinit var koreanLayout: LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener
        var isCaps:Boolean = false
        var buttons:MutableList<Button> = mutableListOf<Button>()
        lateinit var hangulMaker:HangulMaker
        lateinit var vibrator: Vibrator
        lateinit var context:Context

        fun newInstance(context:Context, layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            this.koreanLayout = layoutInflater.inflate(R.layout.keyboard_korean, null) as LinearLayout
            this.inputConnection = inputConnection
            this.keyboardInterationListener = keyboardInterationListener
            this.hangulMaker = HangulMaker(inputConnection)
            this.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            this.context = context

            val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
            val height = sharedPreferences.getInt("keyboardHeight", 100)

            val firstLine = koreanLayout.findViewById<LinearLayout>(R.id.first_line)
            val secondLine = koreanLayout.findViewById<LinearLayout>(R.id.second_line)
            val thirdLine = koreanLayout.findViewById<LinearLayout>(R.id.third_line)
            firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)

            buttons.add(koreanLayout.findViewById<Button>(R.id.key_1))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_2))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_3))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_4))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_5))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_6))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_7))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_8))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_9))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_0))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_q))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_w))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_e))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_r))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_t))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_y))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_u))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_i))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_o))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_p))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_a))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_s))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_d))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_f))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_g))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_h))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_j))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_k))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_l))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_z))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_x))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_c))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_v))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_b))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_n))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_m))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_caps))
            buttons.add(koreanLayout.findViewById<Button>(R.id.del))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_simbols))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_modechange))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_space))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_enter))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_rest))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_dot))
            for(button in buttons){
                button.setOnClickListener(koreanOnClickListener)
            }

            return koreanLayout
        }

        val koreanOnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
                val sound = sharedPreferences.getInt("keyboardSound", -1)
                val vibrate = sharedPreferences.getInt("keyboardVibrate", -1)
                if (vibrate > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(70, vibrate))
                    } else {
                        vibrator.vibrate(70)
                    }
                }
                playClick((view as Button).text.toString().toCharArray().get(0).toInt())


                when (view?.id) {
                    R.id.key_caps -> {
                        modeChange()
                    }
                    R.id.del -> {
                        hangulMaker.delete()
//                        inputConnection.deleteSurroundingText(1, 0)
                    }
                    R.id.key_modechange -> {
                        keyboardInterationListener.modechange(0)
                    }
                    R.id.key_simbols -> {
                        keyboardInterationListener.modechange(2)
                        hangulMaker.directlyCommit()
                    }
                    R.id.key_space -> {
                        playClick('ㅂ'.toInt())
                        hangulMaker.commitSpace()
                    }
                    R.id.key_enter -> {
                        val eventTime = System.currentTimeMillis()
                        val keyEvent = KeyEvent(
                            eventTime,
                            eventTime,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_SEARCH,
                            0,
                            0,
                            0,
                            0,
                            KeyEvent.FLAG_SOFT_KEYBOARD,
                            KeyEvent.FLAG_KEEP_TOUCH_MODE
                        )
                        inputConnection.sendKeyEvent(keyEvent)
//                        (context as Activity).dispatchKeyEvent(keyEvent)
                    }
                    else -> {
                        hangulMaker.commit((view as Button).text.toString().toCharArray().get(0))
                        if (isCaps) {
                            modeChange()
                        }
                    }
                }
            }
        }
        fun modeChange(){
            if(isCaps){
                isCaps = false
                for(button in buttons){
                    when(button.text.toString()){
                        "ㅃ" -> {
                            button.text = "ㅂ"
                        }
                        "ㅉ" -> {
                            button.text = "ㅈ"
                        }
                        "ㄸ" -> {
                            button.text = "ㄷ"
                        }
                        "ㄲ" -> {
                            button.text = "ㄱ"
                        }
                        "ㅆ" -> {
                            button.text = "ㅅ"
                        }
                        "ㅒ" -> {
                            button.text = "ㅐ"
                        }
                        "ㅖ" -> {
                            button.text = "ㅔ"
                        }
                    }
                }
            }
            else{
                isCaps = true
                for(button in buttons){
                    when(button.text.toString()){
                        "ㅂ" -> {
                            button.text = "ㅃ"
                        }
                        "ㅈ" -> {
                            button.text = "ㅉ"
                        }
                        "ㄷ" -> {
                            button.text = "ㄸ"
                        }
                        "ㄱ" -> {
                            button.text = "ㄲ"
                        }
                        "ㅅ" -> {
                            button.text = "ㅆ"
                        }
                        "ㅐ" -> {
                            button.text = "ㅒ"
                        }
                        "ㅔ" -> {
                            button.text = "ㅖ"
                        }
                    }
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