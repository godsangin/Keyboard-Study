package com.myhome.rpgkeyboard.setting.custom

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.myhome.rpgkeyboard.R

class CheckGridAdapter(val context:Context, val activity:Activity, val items:ArrayList<CheckGridItem>, val checkedListener: CheckedListener, val selectedItems:ArrayList<CheckGridItem>): BaseAdapter() {
    
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.custom_check_item, parent, false)
        var item = items.get(position)
        var itemCheckBox = view.findViewById<CheckBox>(R.id.checkbox)
        val keyboardItem = view.findViewById<ConstraintLayout>(R.id.keyboard_item)
        val keyButton = keyboardItem.findViewById<Button>(R.id.key_button)
        val insertButton = view.findViewById<Button>(R.id.insert_bt)
        if(item.isCustomItem){
            itemCheckBox.visibility = View.GONE
            keyboardItem.visibility = View.GONE
            insertButton.visibility = View.VISIBLE
            insertButton.setOnClickListener {
                openDialog(item.itemTitle)
            }
            return view
        }
        itemCheckBox.text = item.itemTitle
        keyButton.text = item.itemContent

        //if checkedItem is contains this item(checked == true)
        for(selectedItem in selectedItems){
            if(selectedItem.itemTitle.equals(item.itemTitle)){
                itemCheckBox.isChecked = true
            }
        }

        itemCheckBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    checkedListener.isChecked(item)
                }
                else{
                    checkedListener.nonChecked(item)
                }
            }
        })

        return view
    }

    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun openDialog(title:String){

        val dialog = CustomInsertDialog(activity, title, checkedListener)
        dialog.show()
        val display:Display = activity.windowManager.defaultDisplay
        var size:Point = Point()
        display.getSize(size)

        val window = dialog.window as Window
        val x = (size.x * 0.8f).toInt()
        val y = (size.y * 0.3f).toInt()
        window.setLayout(x, y)
    }
}