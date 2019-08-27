package com.example.keyboardsecondmodel

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout

class KeyboardNumpad{

    companion object {
        lateinit var numpadLayout:LinearLayout
        lateinit var inputConnection: InputConnection
        lateinit var keyboardInterationListener: KeyboardInterationListener

        fun newInstance(layoutInflater: LayoutInflater, inputConnection: InputConnection, keyboardInterationListener: KeyboardInterationListener): LinearLayout {
            this.numpadLayout = layoutInflater.inflate(R.layout.keyboard_numpad, null) as LinearLayout
            this.inputConnection = inputConnection
            this.keyboardInterationListener = keyboardInterationListener
            numpadLayout.findViewById<Button>(R.id.num1).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num2).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num3).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num4).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num5).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num6).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num7).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num8).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num9).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.num0).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.del).setOnClickListener(numpadOnClickListener)
            numpadLayout.findViewById<Button>(R.id.key_modechange).setOnClickListener(numpadOnClickListener)

            return numpadLayout
        }

        val numpadOnClickListener = object:View.OnClickListener {
            override fun onClick(view: View?) {
                Log.d("view==", view?.id.toString())
                when (view?.id) {
                    R.id.num1 -> {
                        inputConnection.commitText("1", 1)
                    }
                    R.id.num2 -> {
                        inputConnection.commitText("2", 1)
                    }
                    R.id.num3 -> {
                        inputConnection.commitText("3", 1)
                    }
                    R.id.num4 -> {
                        inputConnection.commitText("4", 1)
                    }
                    R.id.num5 -> {
                        inputConnection.commitText("5", 1)
                    }
                    R.id.num6 -> {
                        inputConnection.commitText("6", 1)
                    }
                    R.id.num7 -> {
                        inputConnection.commitText("7", 1)
                    }
                    R.id.num8 -> {
                        inputConnection.commitText("8", 1)
                    }
                    R.id.num9 -> {
                        inputConnection.commitText("9", 1)
                    }
                    R.id.num0 -> {
                        inputConnection.commitText("0", 1)
                    }
                    R.id.dot -> {
                        inputConnection.commitText(".", 1)
                    }
                    R.id.del -> {
                        inputConnection.deleteSurroundingText(1, 0)
                    }
                    R.id.next -> {

                    }
                    R.id.minus -> {
                        inputConnection.commitText("-", 1)
                    }
                    R.id.key_modechange -> {
                        keyboardInterationListener.modechange(0)
                    }
                }
            }
        }
    }
}

