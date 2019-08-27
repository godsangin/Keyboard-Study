package com.example.keyboardsecondmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_detail)

        val intent = getIntent()
        val detailSettingArray = ArrayList<SettingItem>()
        val settingListView = findViewById<ListView>(R.id.setting_listview)
        val sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE)
        var editor:SharedPreferences.Editor? = null
        val settingCase = intent.getIntExtra("setting", -1)


        val settingRecyclerView = findViewById<RecyclerView>(R.id.setting_recyclerview)
        val settingRecyclerViewAdapter = SettingRecyclerViewAdapter(applicationContext, detailSettingArray)
        val keyboardView = findViewById<LinearLayout>(R.id.keyboard_view)

        when(settingCase){
            0 -> {

            }
            1 -> {
                detailSettingArray.add(SettingItem("키보드 소리 설정", "키 패드를 누를 경우 키보드 소리를 출력합니다. 휴대폰이 무음 또는 진동모드일 경우 적용되지 않습니다.",  true, "keyboardSound"))
                detailSettingArray.add(SettingItem("키보드 진동 설정", "키 패드를 누를 경우 키보드 진동을 출력합니다.", true, "keyboardVibrate"))
                editor = sharedPreferences.edit()
                editor.putInt("vibrator", 100)

            }
            2 -> {

            }
            3 -> {
                detailSettingArray.add(SettingItem("키보드 크기 설정","크기를 기본 값에서 변경합니다.", true, "keyboardHeight"))
                detailSettingArray.add(SettingItem("키보드 테마 설정", "키보드의 기본 테마를 변경합니다.", true, "keyboardTheme"))
                settingRecyclerViewAdapter.keyboardSettingListener = object:KeyboardSettingListener{
                    override fun setKeyboardHeight(height: Int) {
                        Log.d("height==", height.toString())
                        val firstLine = keyboardView.findViewById<LinearLayout>(R.id.first_line)
                        val secondLine = keyboardView.findViewById<LinearLayout>(R.id.second_line)
                        val thirdLine = keyboardView.findViewById<LinearLayout>(R.id.third_line)

                        firstLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                        secondLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                        thirdLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                    }

                    override fun setKeyboardTheme(themeNum: Int) {
                        //something
                    }
                }
                keyboardView.visibility = View.VISIBLE
            }
        }

        settingRecyclerView.adapter = settingRecyclerViewAdapter

        val lm = LinearLayoutManager(applicationContext)
        lm.isItemPrefetchEnabled = true
        settingRecyclerView.layoutManager = lm
        settingRecyclerView.setHasFixedSize(true)
    }


}
