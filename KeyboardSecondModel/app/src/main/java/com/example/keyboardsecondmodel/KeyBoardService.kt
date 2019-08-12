package com.example.keyboardsecondmodel

import android.app.Instrumentation
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout


class KeyBoardService : InputMethodService() {
    
    lateinit var keyboardView:LinearLayout

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        val button = keyboardView.findViewById<Button>(R.id.button1)
        val button2 = keyboardView.findViewById<Button>(R.id.button2)
        button.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Thread(object: Runnable {
                    override fun run() {
                        Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_1)
                    }
                }).start()
            }
        })

        button2.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                keyboardView = layoutInflater.inflate(R.layout.keyboard_view2, null) as LinearLayout
            }
        })
        keyboardView.setOnKeyListener(object: View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, keyEvent: KeyEvent?): Boolean {//이벤트가 사용됬을 경우 true
                val inputConnection = currentInputConnection
                Log.d("keyevent==", keyEvent?.keyCode.toString())
                if(inputConnection == null){
                    return false
                }
                when(keyEvent?.keyCode){
                    KeyEvent.KEYCODE_1 -> {
                        inputConnection.commitText("1", 1)
                    }
                }
                return false
            }
        })
        return keyboardView
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val inputConnection = currentInputConnection
        Log.d("keyevent==", event?.keyCode.toString())
        if(inputConnection == null){
            return false
        }
        when(event?.keyCode){
            KeyEvent.KEYCODE_1 -> {
                inputConnection.commitText("1", 1)
            }
        }
        return false

    }
}
