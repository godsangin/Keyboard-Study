package com.example.mykeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethod

class MyKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener{
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

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var isCap:Boolean = false
    private lateinit var context:InputMethodService
    private var mode = 0
    lateinit var inputMethod: InputMethod
    val map:HashMap<Char, Char> = hashMapOf<Char, Char>()
    var expectJung = false
    private var choindex:Char = '\u0000'
    private lateinit var hangulMaker:HangulMaker


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
        mapping()
        return keyboardView
    }


    override fun onKey(i: Int, ints: IntArray?) {

        Log.d("keycode==", i.toChar() + "")
        val inputConnection = currentInputConnection
        if(inputConnection == null){
            return
        }
        playClick(i)

        when(i){
            Keyboard.KEYCODE_DELETE -> {//한글처리
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
            32 -> {
                inputConnection.commitText(" ", 1)
                hangulMaker.clear()
            }
            -2 ->{
                if(mode == 0){
                    keyboard = Keyboard(this, R.xml.key_layout_hangul)
                    keyboardView.keyboard = keyboard
                    hangulMaker = HangulMaker(inputConnection)
                    mode = 1
                }
                else{
                    keyboard = Keyboard(this, R.xml.key_layout)
                    keyboardView.keyboard = keyboard
                    mode = 0
                }

            }
            else -> {
                var code = i.toChar()
                //to
                if(mode == 1){
                    code = map.get(code)!!
                    hangulMaker.commit(code)

                }
                else if((code in 'a'..'z' || code in 'A'..'Z') && isCap){
                    code = code.toUpperCase()
                    inputConnection.commitText(code.toString(), 1)
                }else{
                    inputConnection.commitText(code.toString(), 1)
                }
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

    private fun mapping(){
        map.put('q', 'ㅂ')
        map.put('w', 'ㅈ')
        map.put('e', 'ㄷ')
        map.put('r', 'ㄱ')
        map.put('t', 'ㅅ')
        map.put('y', 'ㅛ')
        map.put('u', 'ㅕ')
        map.put('i', 'ㅑ')
        map.put('o', 'ㅐ')
        map.put('p', 'ㅔ')
        map.put('a', 'ㅁ')
        map.put('s', 'ㄴ')
        map.put('d', 'ㅇ')
        map.put('f', 'ㄹ')
        map.put('g', 'ㅎ')
        map.put('h', 'ㅗ')
        map.put('j', 'ㅓ')
        map.put('k', 'ㅏ')
        map.put('l', 'ㅣ')
        map.put('z', 'ㅋ')
        map.put('x', 'ㅌ')
        map.put('c', 'ㅊ')
        map.put('v', 'ㅍ')
        map.put('b', 'ㅠ')
        map.put('n', 'ㅜ')
        map.put('m', 'ㅡ')

    }

}

