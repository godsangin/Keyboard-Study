package com.myhome.rpgkeyboard.keyboardview

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.inputmethodservice.Keyboard
import android.media.AudioManager
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.*
import com.myhome.rpgkeyboard.setting.custom.CheckGridItem
import com.myhome.rpgkeyboard.setting.custom.CustomViewAdapter

class KeyboardKorean constructor(var context:Context, var layoutInflater: LayoutInflater, var inputConnection: InputConnection, var keyboardInterationListener: KeyboardInterationListener){

    lateinit var koreanLayout: LinearLayout
    var isCaps:Boolean = false
    var buttons:MutableList<Button> = mutableListOf<Button>()
    var animationImageViews:MutableList<ImageView> = mutableListOf()
    lateinit var hangulMaker: HangulMaker
    lateinit var vibrator: Vibrator
    lateinit var sharedPreferences:SharedPreferences
    val numpadText = listOf<String>("1","2","3","4","5","6","7","8","9","0")
    val firstLineText = listOf<String>("ㅂ","ㅈ","ㄷ","ㄱ","ㅅ","ㅛ","ㅕ","ㅑ","ㅐ","ㅔ")
    val secondLineText = listOf<String>("ㅁ","ㄴ","ㅇ","ㄹ","ㅎ","ㅗ","ㅓ","ㅏ","ㅣ")
    val thirdLineText = listOf<String>("CAPS","ㅋ","ㅌ","ㅊ","ㅍ","ㅠ","ㅜ","ㅡ","DEL")
    val fourthLineText = listOf<String>("!#1","한/영",",","space",".","Enter")
    val myKeysText = ArrayList<List<String>>()
    val layoutLines = ArrayList<LinearLayout>()
    var downView:View? = null
    var animationMode:Int = 0

    fun init():LinearLayout{
        koreanLayout = layoutInflater.inflate(R.layout.keyboard_action, null) as LinearLayout
        inputConnection = inputConnection
        hangulMaker = HangulMaker(inputConnection)
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        context = context

        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)

        val height = sharedPreferences.getInt("keyboardHeight", 100)
        animationMode = sharedPreferences.getInt("theme", 0)
        val config = context.getResources().configuration

        val numpadLine = koreanLayout.findViewById<LinearLayout>(
            R.id.numpad_line
        )
        val firstLine = koreanLayout.findViewById<LinearLayout>(
            R.id.first_line
        )
        val secondLine = koreanLayout.findViewById<LinearLayout>(
            R.id.second_line
        )
        val thirdLine = koreanLayout.findViewById<LinearLayout>(
            R.id.third_line
        )
        val fourthLine = koreanLayout.findViewById<LinearLayout>(
            R.id.fourth_line
        )

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (height*0.7).toInt())
            secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (height*0.7).toInt())
            thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (height*0.7).toInt())
        }else{
            firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        }

        myKeysText.clear()
        myKeysText.add(numpadText)
        myKeysText.add(firstLineText)
        myKeysText.add(secondLineText)
        myKeysText.add(thirdLineText)
        myKeysText.add(fourthLineText)

        layoutLines.clear()
        layoutLines.add(numpadLine)
        layoutLines.add(firstLine)
        layoutLines.add(secondLine)
        layoutLines.add(thirdLine)
        layoutLines.add(fourthLine)
        setLayoutComponents()

        return koreanLayout
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
    private fun setLayoutComponents(){
        val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
        val sound = sharedPreferences.getInt("keyboardSound", -1)
        val vibrate = sharedPreferences.getInt("keyboardVibrate", -1)
        for(line in layoutLines.indices){
            val children = layoutLines[line].children.toList()
            val myText = myKeysText[line]
            for(item in children.indices){
                val actionButton = children[item].findViewById<Button>(R.id.key_button)
                actionButton.text = myText[item]
                buttons.add(actionButton)

                val clickListener = (View.OnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        inputConnection.requestCursorUpdates(InputConnection.CURSOR_UPDATE_IMMEDIATE)
                    }
                    if(vibrate > 0){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, vibrate))
                        }
                        else{
                            vibrator.vibrate(50)
                        }
                    }
                    val cursorcs:CharSequence? =  inputConnection.getSelectedText(InputConnection.GET_TEXT_WITH_STYLES)
                    if(cursorcs != null && cursorcs.length >= 2){

                        val eventTime = SystemClock.uptimeMillis()
                        inputConnection.finishComposingText()
                        inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD))
                        inputConnection.sendKeyEvent(KeyEvent(SystemClock.uptimeMillis(), eventTime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD))
                        inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD))
                        inputConnection.sendKeyEvent(KeyEvent(SystemClock.uptimeMillis(), eventTime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD))

                        hangulMaker.clear()
                    }
                    else{
                        when (actionButton.text.toString()) {
                            "CAPS" -> {
                                modeChange()
                            }
                            "DEL" -> {
                                hangulMaker.delete()
                            }
                            "한/영" -> {
                                wholeAnimationStop()
                                keyboardInterationListener.modechange(0)
                                hangulMaker.directlyCommit()
                            }
                            "!#1" -> {
                                wholeAnimationStop()
                                keyboardInterationListener.modechange(2)
                                hangulMaker.directlyCommit()
                            }
                            "space" -> {
                                playClick('ㅂ'.toInt())
                                hangulMaker.commitSpace()
                            }
                            "Enter" -> {
                                hangulMaker.directlyCommit()
                                val eventTime = SystemClock.uptimeMillis()
                                inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime,
                                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                                    KeyEvent.FLAG_SOFT_KEYBOARD))
                                inputConnection.sendKeyEvent(KeyEvent(SystemClock.uptimeMillis(), eventTime,
                                    KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                                    KeyEvent.FLAG_SOFT_KEYBOARD))
                            }
                            else -> {
                                playClick(
                                    actionButton.text.toString().toCharArray().get(
                                        0
                                    ).toInt()
                                )
                                hangulMaker.commit(actionButton.text.toString().toCharArray().get(0))
                                if(isCaps){
                                    modeChange()
                                }
                            }
                        }
                    }
                })

                actionButton.setOnClickListener(clickListener)
                children[item].setOnClickListener(clickListener)
                val handler = Handler()
                val initailInterval = 500
                val normalInterval = 100
                val handlerRunnable = object: Runnable{
                    override fun run() {
                        handler.postDelayed(this, normalInterval.toLong())
                        clickListener.onClick(downView)
                    }
                }

                val onTouchListener = object:View.OnTouchListener{
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                        when(motionEvent?.getAction()){
                            MotionEvent.ACTION_DOWN -> {
                                handler.removeCallbacks(handlerRunnable)
                                handler.postDelayed(handlerRunnable, initailInterval.toLong())
                                downView = view!!
                                clickListener.onClick(view)
                                return true
                            }
                            MotionEvent.ACTION_UP -> {
                                handler.removeCallbacks(handlerRunnable)
                                downView = null
                                return true
                            }
                            MotionEvent.ACTION_CANCEL -> {
                                handler.removeCallbacks(handlerRunnable)
                                downView = null
                                return true
                            }
                        }
                        return false
                    }
                }
                actionButton.setOnTouchListener(onTouchListener)
            }
        }
    }

    fun wholeAnimationStop(){
        for(actionImage in animationImageViews){
            actionImage.clearAnimation()
            actionImage.visibility = View.GONE
        }
        animationImageViews.clear()
    }

}