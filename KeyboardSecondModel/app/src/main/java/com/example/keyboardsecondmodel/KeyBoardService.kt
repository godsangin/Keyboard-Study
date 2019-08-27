package com.example.keyboardsecondmodel

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout


class KeyBoardService : InputMethodService(){
    lateinit var keyboardView:LinearLayout
    lateinit var keyboardFrame:FrameLayout

    lateinit var numPad:LinearLayout
    val keyboardInterationListener = object:KeyboardInterationListener{
        //inputconnection이 null일경우 재요청하는 부분 필요함
        override fun modechange(mode: Int) {
            when(mode){
                0 ->{
                    Log.d("modechange==", "clicked0")
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardEnglish.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
                1 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardKorean.newInstance(layoutInflater, currentInputConnection, this) )
                }
                2 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardSimbols.newInstance(layoutInflater, currentInputConnection, this))
                }
            }
        }
    }


    override fun onCreateInputView(): View {
        //onclick에서 바로 변경하자
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        keyboardFrame = keyboardView.findViewById(R.id.keyboard_frame)
        numPad = KeyboardNumpad.newInstance(layoutInflater, currentInputConnection, keyboardInterationListener)
        keyboardFrame.addView(KeyboardEnglish.newInstance(applicationContext, layoutInflater, currentInputConnection, keyboardInterationListener))

        return keyboardView
    }

    override fun updateInputViewShown() {
        super.updateInputViewShown()
        keyboardFrame.removeAllViews()
        keyboardFrame.addView(KeyboardEnglish.newInstance(applicationContext, layoutInflater, currentInputConnection, keyboardInterationListener))
    }


}
