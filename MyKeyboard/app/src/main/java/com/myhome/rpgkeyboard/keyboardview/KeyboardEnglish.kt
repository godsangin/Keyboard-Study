package com.myhome.rpgkeyboard.keyboardview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.media.AudioManager
import android.inputmethodservice.Keyboard
import android.content.Context.AUDIO_SERVICE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.*
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.animation.Animation
import android.widget.ImageView
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.*
import com.myhome.rpgkeyboard.setting.custom.CheckGridItem
import com.myhome.rpgkeyboard.setting.custom.CustomViewAdapter


class KeyboardEnglish {
    companion object{
        lateinit var englishLayout: LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener
        lateinit var context:Context
        lateinit var vibrator: Vibrator
        lateinit var sharedPreferences:SharedPreferences


        var isCaps:Boolean = false
        var buttons:MutableList<Button> = mutableListOf<Button>()
        var animationImageViews:MutableList<ImageView> = mutableListOf()

        val numpadText = listOf<String>("1","2","3","4","5","6","7","8","9","0")
        val firstLineText = listOf<String>("q","w","e","r","t","y","u","i","o","p")
        val secondLineText = listOf<String>("a","s","d","f","g","h","j","k","l")
        val thirdLineText = listOf<String>("CAPS","z","x","c","v","b","n","m","DEL")
        val fourthLineText = listOf<String>("!#1","한/영",",","space",".","Enter")
        val myKeysText = ArrayList<List<String>>()
        val layoutLines = ArrayList<LinearLayout>()
        var downView:View? = null
        var animationMode:Int = 0

        fun newInstance(context:Context, layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            Companion.context = context
            englishLayout = layoutInflater.inflate(R.layout.keyboard_action, null) as LinearLayout
            Companion.inputConnection = inputConnection
            Companion.keyboardInterationListener = keyboardInterationListener
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            val config = context.getResources().configuration
            sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
            val height = sharedPreferences.getInt("keyboardHeight", 100)
            animationMode = sharedPreferences.getInt("theme", 0)

            val numpadLine = englishLayout.findViewById<LinearLayout>(
                R.id.numpad_line
            )
            val firstLine = englishLayout.findViewById<LinearLayout>(
                R.id.first_line
            )
            val secondLine = englishLayout.findViewById<LinearLayout>(
                R.id.second_line
            )
            val thirdLine = englishLayout.findViewById<LinearLayout>(
                R.id.third_line
            )
            val fourthLine = englishLayout.findViewById<LinearLayout>(
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
            return englishLayout
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
                        if(vibrate > 0){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(70, vibrate))
                            }
                            else{
                                vibrator.vibrate(70)
                            }
                        }



                        when (actionButton.text.toString()) {
                            "CAPS" -> {
                                wholeAnimationStop()
                                modeChange()
                            }
                            "DEL" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    inputConnection.deleteSurroundingTextInCodePoints(1, 0)
                                }else{
                                    inputConnection.deleteSurroundingText(1,0)
                                }

                            }
                            "한/영" -> {
                                wholeAnimationStop()
                                keyboardInterationListener.modechange(1)
                            }
                            "!#1" -> {
                                wholeAnimationStop()
                                keyboardInterationListener.modechange(2)
                            }
                            "space" -> {
                                playClick('ㅂ'.toInt())
                                inputConnection.commitText(" ",1)
                            }
                            "Enter" -> {
                                val eventTime = SystemClock.uptimeMillis()
                                inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime,
                                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                                    KeyEvent.FLAG_SOFT_KEYBOARD))
                                inputConnection.sendKeyEvent(KeyEvent(
                                    SystemClock.uptimeMillis(), eventTime,
                                    KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                                    KeyEvent.FLAG_SOFT_KEYBOARD))
                            }
                            else -> {
                                playClick(
                                    actionButton.text.toString().toCharArray().get(
                                        0
                                    ).toInt()
                                )
                                inputConnection.commitText(actionButton.text,1)
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
}