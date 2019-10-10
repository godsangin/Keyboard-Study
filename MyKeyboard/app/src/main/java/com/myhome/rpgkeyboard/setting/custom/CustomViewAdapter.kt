package com.myhome.rpgkeyboard.setting.custom

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.R

class CustomViewAdapter(val context:Context, val items:ArrayList<CheckGridItem>) :RecyclerView.Adapter<CustomViewAdapter.Holder>(){
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
        }
    }
}