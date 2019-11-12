package com.myhome.rpgkeyboard.setting

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.KeyboardSettingListener
import com.myhome.rpgkeyboard.R

class SettingRecyclerViewAdapter(val context:Context, val settingList:ArrayList<SettingItem>) :RecyclerView.Adapter<SettingRecyclerViewAdapter.Holder>(){

    var keyboardSettingListener: KeyboardSettingListener? = null
        set(keyboardSettingListener) {field = keyboardSettingListener}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.setting_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return settingList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(settingList[position], context)
        if(settingList[position].flag.equals("keyboardHeight")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder?.seekBar?.min = 100
            }else{
                //?
            }
            holder?.seekBar?.max = 200
            if(holder?.seekBar != null){
                keyboardSettingListener?.setKeyboardHeight(holder?.seekBar?.progress)
            }
        }
        holder.seekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                sendSeekBarEvent(settingList[position].flag, progress)
                val editor = holder.sharedPreferences.edit()
                editor.putInt(settingList[position].flag, progress)
                editor.commit()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    fun sendSeekBarEvent(flag:String, progress:Int){
        when(flag){
            "keyboardVibrate" -> {
                Log.d("progress==", progress.toString())
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, progress))
                }
                else{
                    vibrator.vibrate(100)
                }
            }
            "keyboardSound" -> {
                val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
                am!!.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, (-progress).toFloat())
            }
            "keyboardHeight" -> {
                Log.d("progress==", progress.toString())
                keyboardSettingListener?.setKeyboardHeight(progress)

            }
            else -> {
                Log.d("else==",  "true")
            }
        }
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val titleTextView = itemView?.findViewById<TextView>(R.id.item_title)
        val contentTextView = itemView?.findViewById<TextView>(R.id.item_content)
        val checkBox = itemView?.findViewById<CheckBox>(R.id.checkbox)
        val seekBar = itemView?.findViewById<SeekBar>(R.id.seekbar)
        val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)

        fun bind(settingItem: SettingItem, context: Context) {
            titleTextView?.text = settingItem.title
            contentTextView?.text = settingItem.content

            val size = sharedPreferences.getInt(settingItem.flag, -1)
            if(settingItem.isCheckBoxVisible){
                checkBox?.visibility = View.VISIBLE
            }
            else{
                checkBox?.visibility = View.GONE
            }
            Log.d("size==", size.toString())
            if(size > 0){
                checkBox?.isChecked = true
                seekBar?.visibility = View.VISIBLE
                seekBar?.max = 255
                seekBar?.progress = size
            }
            else{
                checkBox?.isChecked = false
                seekBar?.progress = 150
                seekBar?.visibility = View.GONE
            }
            checkBox?.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    if(checkBox.isChecked){
                        checkBox?.isChecked = true
                        seekBar?.visibility = View.VISIBLE
                        seekBar?.max = 255
                        seekBar?.progress = 150
                    }
                    else{
                        val editor = sharedPreferences.edit()
                        editor.putInt(settingList[position].flag, -1)
                        editor.commit()
                        seekBar?.progress = 150
                        seekBar?.visibility = View.GONE
                    }
                }
            })
        }
    }
}