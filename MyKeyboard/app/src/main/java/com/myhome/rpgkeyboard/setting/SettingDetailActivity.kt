package com.myhome.rpgkeyboard.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.KeyboardSettingListener
import com.myhome.rpgkeyboard.R

class SettingDetailActivity : AppCompatActivity() {
    lateinit var radioGrup:RadioGroup
    lateinit var keyboardView:LinearLayout
    lateinit var sharedPreferences:SharedPreferences
    var isQwerty = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_detail)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.setting_header_background)
            }
        }

        val intent = getIntent()
        val detailSettingArray = ArrayList<SettingItem>()
        val settingCase = intent.getIntExtra("setting", -1)
        val settingRecyclerView = findViewById<RecyclerView>(R.id.setting_recyclerview)
        val settingRecyclerViewAdapter =
            SettingRecyclerViewAdapter(applicationContext, detailSettingArray)
        keyboardView = findViewById<LinearLayout>(R.id.keyboard_view)
        val settingHeader = findViewById<ConstraintLayout>(R.id.setting_header)
        val submitButton = settingHeader.findViewById<TextView>(R.id.submit_text)
        radioGrup = findViewById<RadioGroup>(R.id.radiogroup)
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE)
        submitButton.visibility = View.GONE

        when(settingCase){
            0 -> {

            }
            1 -> {
                detailSettingArray.add(
                    SettingItem(
                        "키보드 소리 설정",
                        "키 패드를 누를 경우 키보드 소리를 출력합니다. 휴대폰이 무음 또는 진동모드일 경우 적용되지 않습니다.",
                        true,
                        "keyboardSound"
                    )
                )
                detailSettingArray.add(
                    SettingItem(
                        "키보드 진동 설정",
                        "키 패드를 누를 경우 키보드 진동을 출력합니다.",
                        true,
                        "keyboardVibrate"
                    )
                )

            }
            2 -> {
                detailSettingArray.add(
                    SettingItem(
                        "키보드 크기 설정",
                        "크기를 기본 값에서 변경합니다.",
                        true,
                        "keyboardHeight"
                    )
                )
                setKeyboardSettingComponent()
                keyboardView.visibility = View.VISIBLE
            }
        }
        settingRecyclerViewAdapter.keyboardSettingListener = object: KeyboardSettingListener {
            override fun setKeyboardHeight(height: Int) {
                Log.d("height==", height.toString())
                setMyKeyboardHeight(height)
            }

            override fun setKeyboardTheme(themeNum: Int) {
                //something
            }

            override fun setKeyboardConvenience() {
                keyboardView.visibility = View.GONE
                //편의기능 view 추가
            }
        }

        settingRecyclerView.adapter = settingRecyclerViewAdapter

        val lm = LinearLayoutManager(applicationContext)
        lm.isItemPrefetchEnabled = true
        settingRecyclerView.layoutManager = lm
        settingRecyclerView.setHasFixedSize(true)
    }

    fun setMyKeyboardHeight(height:Int){
        val firstLine = keyboardView.findViewById<LinearLayout>(R.id.first_line)
        val secondLine = keyboardView.findViewById<LinearLayout>(R.id.second_line)
        val thirdLine = keyboardView.findViewById<LinearLayout>(R.id.third_line)

        firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        if(!isQwerty){
            val fourthLine = keyboardView.findViewById<LinearLayout>(R.id.fourth_line)
            fourthLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        }
    }

    fun setKeyboardSettingComponent(){
        radioGrup.visibility = View.VISIBLE
        val qwertyButton = findViewById<RadioButton>(R.id.qwerty_bt)
        val chunjiinButton = findViewById<RadioButton>(R.id.chunjiin_bt)
        val editor = sharedPreferences.edit()
        if(sharedPreferences.getInt("keyboardMode", 1) == 1){
            qwertyButton.isChecked = true
        }
        else{
            chunjiinButton.isChecked = true
            keyboardView.visibility = View.GONE
            keyboardView = findViewById(R.id.keyboard_chunjiin)
            keyboardView.visibility = View.VISIBLE
            setMyKeyboardHeight(sharedPreferences.getInt("height", 150))
        }
        qwertyButton.setOnClickListener {
            isQwerty = true
            keyboardView.visibility = View.GONE
            keyboardView = findViewById(R.id.keyboard_view)
            keyboardView.visibility = View.VISIBLE
            setMyKeyboardHeight(sharedPreferences.getInt("height", 150))
            editor.putInt("keyboardMode", 1)
            editor.commit()
        }
        chunjiinButton.setOnClickListener {
            isQwerty = false
            keyboardView.visibility = View.GONE
            keyboardView = findViewById(R.id.keyboard_chunjiin)
            keyboardView.visibility = View.VISIBLE
            setMyKeyboardHeight(sharedPreferences.getInt("height", 150))
            editor.putInt("keyboardMode", 0)
            editor.commit()
        }
    }


}
