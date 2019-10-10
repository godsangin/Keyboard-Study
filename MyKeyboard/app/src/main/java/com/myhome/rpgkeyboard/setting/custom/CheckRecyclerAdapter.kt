package com.myhome.rpgkeyboard.setting.custom

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.R

class CheckRecyclerAdapter(val context:Context, val activity: Activity, val items:ArrayList<CheckGridItem>, val checkedListener: CheckedListener, val selectedItems:ArrayList<CheckGridItem>) :RecyclerView.Adapter<CheckRecyclerAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_check_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(items[position], context)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemCheckBox = itemView?.findViewById<CheckBox>(R.id.checkbox)
        val keyboardItem = itemView?.findViewById<ConstraintLayout>(R.id.keyboard_item)
        val keyButton = keyboardItem?.findViewById<Button>(R.id.key_button)
        val insertButton = itemView?.findViewById<Button>(R.id.insert_bt)
        fun bind(item: CheckGridItem, context: Context) {
            if(item.isCustomItem){
                itemCheckBox?.visibility = View.GONE
                keyboardItem?.visibility = View.GONE
                insertButton?.visibility = View.VISIBLE
                insertButton?.setOnClickListener {
                    openDialog(item.itemTitle)
                }
                return
            }
            itemCheckBox?.text = item.itemTitle
            keyButton?.text = item.itemContent

            //if checkedItem is contains this item(checked == true)
            for(selectedItem in selectedItems){
                if(selectedItem.itemTitle.equals(item.itemTitle)){
                    itemCheckBox?.isChecked = true
                }
            }

            itemCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                    if(isChecked){
                        checkedListener.isChecked(item)
                    }
                    else{
                        checkedListener.nonChecked(item)
                    }
                }
            })
        }
    }

    fun openDialog(title:String){

        val dialog = CustomInsertDialog(activity, title, checkedListener)
        dialog.show()
        val display: Display = activity.windowManager.defaultDisplay
        var size: Point = Point()
        display.getSize(size)

        val window = dialog.window as Window
        val x = (size.x * 0.8f).toInt()
        val y = (size.y * 0.3f).toInt()
        window.setLayout(x, y)
    }
}