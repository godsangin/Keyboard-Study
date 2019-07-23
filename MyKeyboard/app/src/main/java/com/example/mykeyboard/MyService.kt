package com.example.mykeyboard

import android.app.Service
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.*
import android.os.IBinder
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.media.AudioManager
import android.inputmethodservice.Keyboard



class MyService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var isCap:Boolean = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.key_layout)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
//        keyboardView.setOnKeyListener(object: View.OnKeyListener{
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
        return keyboardView
    }

    override fun swipeRight() {

    }

    override fun onPress(p0: Int) {

    }

    override fun onRelease(p0: Int) {

    }

    override fun swipeLeft() {

    }

    override fun swipeUp() {

    }

    override fun swipeDown() {
    }

    override fun onKey(i: Int, ints: IntArray?) {
        val inputConnection = currentInputConnection
        if(inputConnection == null){
            return
        }
        playClick(i)

        when(i){
            Keyboard.KEYCODE_DELETE -> {
                val selectedText: CharSequence? = inputConnection.getSelectedText(0)
                if (selectedText == null || TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0)
                } else {
                    inputConnection.commitText("", 1)
                }
            }
            Keyboard.KEYCODE_SHIFT -> {
                isCap = !isCap
                keyboard.isShifted = isCap
                keyboardView.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> {
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            200 ->{
                //.? 특수기호 키보드로 변경
            }
            201 ->{
                //한영 변경
            }
            202 ->{
                //MODE Change  학습한 키보드경계선 출력
            }
            else -> {
                var code = i.toChar()
                //to
                if((code in 'a'..'z' || code in 'A'..'Z') && isCap){
                    code = code.toUpperCase()
                }
                inputConnection.commitText(code.toString(), 1)
            }
        }
    }

    override fun onText(p0: CharSequence?) {

    }

    private fun playClick(i: Int) {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (i) {
            32 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

}
