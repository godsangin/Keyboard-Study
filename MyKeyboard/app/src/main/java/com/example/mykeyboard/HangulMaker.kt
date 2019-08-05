package com.example.mykeyboard

import android.util.Log
import android.view.inputmethod.InputConnection

class HangulMaker {

    private var cho: Char = '\u0000'
    private var jun: Char = '\u0000'
    private var jon: Char = '\u0000'

    private val chos: List<Int> = listOf(0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141,0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e)
    private val juns:List<Int> = listOf(0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163)
    private val jons:List<Int> = listOf(0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e)

    /**
     * 0:""
     * 1: 모음 입력상태
     * 2: 모음 + 자음 입력상태
     * 3: 모음 + 자음 + 모음입력상태(초 중 종성)
     * 초성과 종성에 들어갈 수 있는 문자가 다르기 때문에 필요에 맞게 수정이 필요함.(chos != jons)
     */
    private var state = 0
    private lateinit var inputConnection:InputConnection

    constructor(inputConnection: InputConnection){
        this.inputConnection = inputConnection
    }
    fun clear(){

        cho = '\u0000'
        jun = '\u0000'
        jon = '\u0000'
    }

    fun makeHan():Char{
        val choIndex = chos.indexOf(cho.toInt())
        val junIndex = juns.indexOf(jun.toInt())
        val jonIndex = jons.indexOf(jon.toInt())
        Log.d("indexes==", choIndex.toString() + " " + junIndex.toString() + " " + jonIndex)

        val makeResult = 0xAC00 + 28 * 21 * (choIndex) + 28 * (junIndex)  + jonIndex

        return makeResult.toChar()
    }

    fun commit(c:Char){
        Log.d("makeresult==", makeHan().toString())
        when(state){//초정과 종성을 비교하지 않았음(업데이트 필요)
            0 -> {
                if(juns.indexOf(c.toInt()) >= 0){
                    inputConnection.commitText(c.toString(), 1)
                    clear()
                }else{//초성일 경우
                    state = 1
                    cho = c
                    inputConnection.setComposingText(cho.toString(), 1)
                }
            }
            1 -> {
                if(chos.indexOf(c.toInt()) >= 0){
                    inputConnection.commitText(cho.toString(), 1)
                    clear()
                    cho = c
                    inputConnection.setComposingText(cho.toString(), 1)
                }else{//중성일 경우
                    state = 2
                    jun = c
                    inputConnection.setComposingText(makeHan().toString(), 1)
                }
            }
            2 -> {
                if(juns.indexOf(c.toInt()) >= 0){
                    state = 0
                    inputConnection.commitText(makeHan().toString(), 1)
                    inputConnection.commitText(c.toString(), 1)
                    clear()
                }
                else{//종성이 들어왔을 경우
                    state = 3
                    jon = c
                    inputConnection.setComposingText(makeHan().toString(), 1)
                }
            }
            3 -> {
                if(chos.indexOf(c.toInt()) >= 0){
                    state = 1
                    inputConnection.commitText(makeHan().toString(), 1)
                    clear()
                    cho = c
                    Log.d("cho==", cho.toString())
                    inputConnection.setComposingText(cho.toString(), 1)
                }
                else{//중성이 들어올 경우
                    state = 2
                    val temp = jon
                    jon = '\u0000'
                    inputConnection.commitText(makeHan().toString(), 1)
                    clear()
                    cho = temp
                    jun = c
                    inputConnection.setComposingText(makeHan().toString(), 1)

                }
            }
        }
    }

}