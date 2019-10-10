package com.myhome.rpgkeyboard.keyboardview

import android.content.Context
import android.content.res.Configuration
import android.inputmethodservice.Keyboard
import android.media.AudioManager
import android.os.*
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

class KeyboardEmoji{
    companion object{
        lateinit var emojiLayout: LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener
        lateinit var context:Context
        lateinit var vibrator: Vibrator

        lateinit var recyclerView: RecyclerView
        lateinit var emojiRecyclerViewAdapter: EmojiRecyclerViewAdapter
        var animationMode:Int = 0
        val fourthLineText = listOf<String>("!#1","한/영",",","space",".","DEL")

        fun newInstance(context:Context, layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            Companion.context = context
            emojiLayout = layoutInflater.inflate(R.layout.keyboard_emoji, null) as LinearLayout
            Companion.inputConnection = inputConnection
            Companion.keyboardInterationListener = keyboardInterationListener
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            val config = context.getResources().configuration
            val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
            val height = sharedPreferences.getInt("keyboardHeight", 100)
            animationMode = sharedPreferences.getInt("theme", 0)
            val fourthLine = emojiLayout.findViewById<LinearLayout>(
                R.id.fourth_line
            )
            val children = fourthLine.children.toList()
            for(item in children.indices){
                val actionImage = children[item].findViewById<ImageView>(R.id.action_image)
                val actionButton = children[item].findViewById<Button>(R.id.key_button)
                actionButton.text = fourthLineText[item]
                actionButton.setOnClickListener(View.OnClickListener {
                    when((it as Button).text){
                        "!#1" -> {
                            keyboardInterationListener.modechange(2)
                        }
                        "한/영" -> {
                            keyboardInterationListener.modechange(1)
                        }
                        "," -> {
                            inputConnection.commitText(",",1)
                        }
                        "space" -> {
                            inputConnection.commitText(" ", 1)
                        }
                        "." -> {
                            inputConnection.commitText(".", 1)
                        }
                        "DEL" -> {
                            inputConnection.deleteSurroundingText(1,0)
                        }
                    }
                })
            }

            setLayoutComponents()
            return emojiLayout
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


        private fun setLayoutComponents() {
            var recyclerView = emojiLayout.findViewById<RecyclerView>(R.id.emoji_recyclerview)
            val emojiList = ArrayList<String>()

            val unicode = 0x1F600

            for(i in 1..36){
                emojiList.add(getEmojiByUnicode(unicode + i))
//                emojiList.add(i.toString())
            }

            emojiRecyclerViewAdapter = EmojiRecyclerViewAdapter(context, emojiList, inputConnection)
            recyclerView.adapter = emojiRecyclerViewAdapter
            val gm = GridLayoutManager(context,8)
            gm.isItemPrefetchEnabled = true
            recyclerView.layoutManager = gm

        }

        fun getEmojiByUnicode(unicode: Int): String {
            return String(Character.toChars(unicode))
        }

    }
}