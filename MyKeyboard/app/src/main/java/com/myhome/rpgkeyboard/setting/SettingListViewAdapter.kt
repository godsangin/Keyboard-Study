package com.myhome.rpgkeyboard.setting

import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.myhome.rpgkeyboard.R

class SettingListViewAdapter(val myList: ArrayList<SettingItem>, val layoutInflater: LayoutInflater): BaseAdapter(){
    var vibrator: Vibrator? = null
    fun setMyVibrator(vibrator: Vibrator){
        this.vibrator = vibrator
    }
    override fun getView(position: Int, v: View?, parent: ViewGroup?): View {
        var view:View? = null
        var checkBox:CheckBox? = null
        view = layoutInflater?.inflate(R.layout.setting_item, parent, false)
        val titleTextView = view.findViewById<TextView>(R.id.item_title)
        checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        val contentTextView = view.findViewById<TextView>(R.id.item_content)
        titleTextView.text = myList.get(position).title
        contentTextView.text = myList.get(position).content

        if(myList.get(position).isCheckBoxVisible){
            checkBox.visibility = View.VISIBLE
        }


        return view
    }

    override fun getItem(p0: Int): Any {
        return myList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return myList.size
    }
}