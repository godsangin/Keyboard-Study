package com.myhome.rpgkeyboard

import android.content.Context
import android.os.SystemClock
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.setting.custom.CheckGridItem
import com.myhome.rpgkeyboard.setting.custom.CustomViewAdapter

class FunctionalCustomViewAdpater(val context: Context, val items:ArrayList<CheckGridItem>, val keyboardInterationListener: KeyboardInterationListener, val inputConnection: InputConnection):RecyclerView.Adapter<FunctionalCustomViewAdpater.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.keyboard_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(items[position], context)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val button = itemView?.findViewById<Button>(R.id.key_button)
        fun bind(item: CheckGridItem, context: Context) {
            button?.text = item.itemContent
            button?.setOnClickListener(View.OnClickListener {
                when((it as Button).text.toString()){
                    "<" ->{
                        val eventTime = SystemClock.uptimeMillis()
                        inputConnection.sendKeyEvent(
                            KeyEvent(eventTime, eventTime,
                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                        inputConnection.sendKeyEvent(
                            KeyEvent(
                                SystemClock.uptimeMillis(), eventTime,
                                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                    }
                    ">" -> {
                        val eventTime = SystemClock.uptimeMillis()
                        inputConnection.sendKeyEvent(
                            KeyEvent(eventTime, eventTime,
                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                        inputConnection.sendKeyEvent(
                            KeyEvent(
                                SystemClock.uptimeMillis(), eventTime,
                                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                    }
                    "붙혀넣기" -> {
                        val eventTime = SystemClock.uptimeMillis()
                        inputConnection.sendKeyEvent(
                            KeyEvent(eventTime, eventTime,
                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_PASTE, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                        inputConnection.sendKeyEvent(
                            KeyEvent(
                                SystemClock.uptimeMillis(), eventTime,
                                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_PASTE, 0, 0, 0, 0,
                                KeyEvent.FLAG_SOFT_KEYBOARD)
                        )
                    }
                    "\uD83D\uDE00" -> {
                        keyboardInterationListener.modechange(3)
                    }
                    else -> {
                        inputConnection.commitText((it as Button).text.toString(), (it as Button).text.toString().length)
                    }
                }
            })
        }

    }
}