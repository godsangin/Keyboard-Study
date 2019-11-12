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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.keyboardview.*
import com.myhome.rpgkeyboard.setting.custom.CheckGridItem
import com.myhome.rpgkeyboard.setting.custom.CustomSelectedItemDBHelper
import java.util.*


class KeyBoardService : InputMethodService(){
    lateinit var keyboardView:LinearLayout
    lateinit var keyboardFrame:FrameLayout
    lateinit var keyboardKorean:KeyboardKorean
    lateinit var keyboardEnglish:KeyboardEnglish
    lateinit var keyboardSimbols:KeyboardSimbols
    lateinit var sharedPreferences:SharedPreferences
    lateinit var customSharedPreferences: SharedPreferences
    lateinit var items:ArrayList<CheckGridItem>
    lateinit var selectableItems:ArrayList<CheckGridItem>
    lateinit var customView: RecyclerView
    lateinit var customViewAdapter: FunctionalCustomViewAdpater
    var isQwerty = 0


    val keyboardInterationListener = object:KeyboardInterationListener{
        //inputconnection이 null일경우 재요청하는 부분 필요함
        override fun modechange(mode: Int) {
            currentInputConnection.finishComposingText()
            when(mode){
                0 ->{
                    keyboardFrame.removeAllViews()
                    keyboardEnglish.inputConnection = currentInputConnection
                    keyboardFrame.addView(keyboardEnglish.getLayout())
                }
                1 -> {
                    if(isQwerty == 1){
                        keyboardFrame.removeAllViews()
                        keyboardKorean.inputConnection = currentInputConnection
                        keyboardFrame.addView(keyboardKorean.getLayout())
                    }
                    else{
                        keyboardFrame.removeAllViews()
                        keyboardFrame.addView(KeyboardChunjiin.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                    }
                }
                2 -> {
                    keyboardFrame.removeAllViews()
                    keyboardSimbols.inputConnection = currentInputConnection
                    keyboardFrame.addView(keyboardSimbols.getLayout())
                }
                3 -> {
                    keyboardFrame.removeAllViews()
                    keyboardFrame.addView(KeyboardEmoji.newInstance(applicationContext, layoutInflater, currentInputConnection, this))
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        keyboardFrame = keyboardView.findViewById(R.id.keyboard_frame)
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE)
        customSharedPreferences = getSharedPreferences("custom", Context.MODE_PRIVATE)
    }

    override fun onCreateInputView(): View {
        keyboardKorean = KeyboardKorean(applicationContext, layoutInflater, keyboardInterationListener)
        keyboardEnglish = KeyboardEnglish(applicationContext, layoutInflater, keyboardInterationListener)
        keyboardSimbols = KeyboardSimbols(applicationContext, layoutInflater, keyboardInterationListener)
        keyboardKorean.inputConnection = currentInputConnection
        keyboardKorean.init()
        keyboardEnglish.inputConnection = currentInputConnection
        keyboardEnglish.init()
        keyboardSimbols.inputConnection = currentInputConnection
        keyboardSimbols.init()

        items = ArrayList()
        selectableItems = ArrayList<CheckGridItem>()
        customView = keyboardView.findViewById(R.id.keyboard_custom_view)

        setCustomComponents()

        return keyboardView
    }

    override fun updateInputViewShown() {
        super.updateInputViewShown()
        currentInputConnection.finishComposingText()
        isQwerty = sharedPreferences.getInt("keyboardMode", 1)
        if(currentInputEditorInfo.inputType == EditorInfo.TYPE_CLASS_NUMBER){
            keyboardFrame.removeAllViews()
            keyboardFrame.addView(KeyboardNumpad.newInstance(applicationContext, layoutInflater, currentInputConnection, keyboardInterationListener))
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
        val height = sharedPreferences.getInt("keyboardHeight", 150)
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
