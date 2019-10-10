package com.myhome.rpgkeyboard.setting.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.myhome.rpgkeyboard.R

class CustomInsertDialog(context:Context, val title:String, val checkedListener: CheckedListener):Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams
        setContentView(R.layout.dialog_customview_insert)

        val inputText = findViewById<EditText>(R.id.text_input)
        val positiveButton = findViewById<Button>(R.id.positive_bt)
        val negativeButton = findViewById<Button>(R.id.negative_bt)
        val db = CustomItemDBHelper(context)

        positiveButton.setOnClickListener(View.OnClickListener {
            val text = inputText.text.toString()
//            val sharedPreferences = context.getSharedPreferences("customMap", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            editor.putString(title, text)
//            editor.commit()
            db.addItem(CheckGridItem(title, text, false))
            checkedListener.notifyDataSetChanged()
            dismiss()
        })

        negativeButton.setOnClickListener(View.OnClickListener {
            dismiss()
        })

    }
}