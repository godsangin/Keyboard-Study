package com.example.keyboardsecondmodel

import android.content.Context
import android.os.Vibrator
import android.util.Log
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

        fun newInstance(layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            this.koreanLayout = layoutInflater.inflate(R.layout.keyboard_korean, null) as LinearLayout
            this.inputConnection = inputConnection
            this.keyboardInterationListener = keyboardInterationListener
            this.hangulMaker = HangulMaker(inputConnection)

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
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_enter))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_rest))
            buttons.add(koreanLayout.findViewById<Button>(R.id.key_dot))
            for(button in buttons){
                button.setOnClickListener(englishOnClickListener)
            }

            return koreanLayout
        }

        val englishOnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                Log.d("view==", view?.id.toString())
                when (view?.id) {
                    R.id.key_caps -> {
                        modeChange()
                    }
                    R.id.del -> {
                        KeyboardEnglish.inputConnection.deleteSurroundingText(1, 0)
                    }
                    R.id.key_modechange -> {
                        keyboardInterationListener.modechange(0)
                    }
                    R.id.key_simbols -> {
                        keyboardInterationListener.modechange(2)
                    }
                    else -> {
                        hangulMaker.commit((view as Button).text.toString().toCharArray().get(0))
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

    }
}