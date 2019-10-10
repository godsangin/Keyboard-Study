package com.myhome.rpgkeyboard

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.keyboardview.*
import com.myhome.rpgkeyboard.setting.custom.CheckGridItem
import com.myhome.rpgkeyboard.setting.custom.CustomSelectedItemDBHelper
import com.myhome.rpgkeyboard.setting.custom.CustomViewAdapter


class KeyBoardService : InputMethodService(){
    lateinit var keyboardView:LinearLayout
    lateinit var keyboardFrame:FrameLayout

    lateinit var keyboardKorean:KeyboardKorean
    lateinit var sharedPreferences:SharedPreferences
    lateinit var customSharedPreferences: SharedPreferences

    var customItemCount = 1
    lateinit var items:ArrayList<CheckGridItem>
    lateinit var selectableItems:ArrayList<CheckGridItem>


    lateinit var customView: RecyclerView
    lateinit var customViewAdapter: FunctionalCustomViewAdpater


    val keyboardInterationListener = object:KeyboardInterationListener{
        //inputconnection이 null일경우 재요청하는 부분 필요함
        override fun modechange(mode: Int) {
            when(mode){
                0 ->{
                    Log.d("modechange==", "clicked0")
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardEnglish.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
                1 -> {
                    keyboardFrame.removeAllViews()
                    keyboardKorean = KeyboardKorean(applicationContext, layoutInflater, currentInputConnection, this)
                    keyboardFrame.addView(keyboardKorean.init())
                }
                2 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardSimbols.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
                3 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardEmoji.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
                4 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardNumpad.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
            }
        }
    }

    override fun onCreateInputView(): View {
        //onclick에서 바로 변경하자
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        keyboardFrame = keyboardView.findViewById(R.id.keyboard_frame)

        keyboardKorean = KeyboardKorean(applicationContext, layoutInflater, currentInputConnection, keyboardInterationListener)
        //english simbols number emoji

        items = ArrayList()
        selectableItems = ArrayList<CheckGridItem>()
        customView = keyboardView.findViewById(R.id.keyboard_custom_view)




        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE)
        customSharedPreferences = getSharedPreferences("custom", Context.MODE_PRIVATE)
        setCustomComponents()

        return keyboardView
    }

    override fun updateInputViewShown() {
        super.updateInputViewShown()
        if(currentInputEditorInfo.inputType == EditorInfo.TYPE_CLASS_NUMBER){
            keyboardInterationListener.modechange(4)
        }
        else{
            keyboardInterationListener.modechange(1)
        }
        items.clear()
        selectableItems.clear()
        setCustomComponents()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
    }

    fun setCustomComponents(){
        val customViewEnable = sharedPreferences.getInt("customViewEnable", -1)
        Log.d("cutomViewEnable==", customViewEnable.toString())
        if(customViewEnable >= 0){
            customView.visibility = View.VISIBLE
            setCustomGroup()
        }
        val oneHandMode = sharedPreferences.getInt("oneHandMode", -1)
        when(oneHandMode){
            0 -> {
                keyboardFrame.setPadding(0,0,150,0)
                customView.setPadding(0,0,150,0)
            }
            1 -> {
                keyboardFrame.setPadding(75,0,75,0)
                customView.setPadding(75,0,75,0)
            }
            2 -> {
                keyboardFrame.setPadding(150,0,0,0)
                customView.setPadding(150,0,0,0)
            }
            else -> {
                keyboardFrame.setPadding(0,0,0,0)
                customView.setPadding(0,0,0,0)
            }
        }
    }
    private fun setCustomGroup(){
        val height = sharedPreferences.getInt("keyboardHeight", 100)
        val config = applicationContext.getResources().configuration
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            customView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (height*0.7).toInt())
        }else{
            customView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        }
        val customSelectedItemDBHelper = CustomSelectedItemDBHelper(applicationContext)
        items = customSelectedItemDBHelper.allItem

        customViewAdapter = FunctionalCustomViewAdpater(applicationContext, items, keyboardInterationListener, currentInputConnection)
        if(items.size > 0){
            val gl = GridLayoutManager(applicationContext,items.size)
            customView.layoutManager = gl
            customView.setHasFixedSize(true)
        }
        customView.adapter = customViewAdapter
        customViewAdapter.notifyDataSetChanged()
    }

}
