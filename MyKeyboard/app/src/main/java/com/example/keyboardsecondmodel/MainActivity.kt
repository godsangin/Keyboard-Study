package com.example.keyboardsecondmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : AppCompatActivity() {
    lateinit var settingListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingListView = findViewById(R.id.setting_listview)

        var arrayOfSettingItem = ArrayList<SettingItem>()
        arrayOfSettingItem.add(SettingItem("키보드 자판 설정","쿼티, 천지인 등 키보드 자판의 설정을 변경합니다.", false, ""))
        arrayOfSettingItem.add(SettingItem("소리 및 진동 설정","터치 시 발생하는 소리 또는 진동에 대한 설정을 변경합니다.", false, ""))
        arrayOfSettingItem.add(SettingItem("편의 기능 추가","키보드 윗부분에 자주 사용하는 키 또는 기능을 추가합니다.", false, ""))
        arrayOfSettingItem.add(SettingItem("키보드 크기 설정","키 패드 또는 키보드의 높이를 설정합니다.",  false, ""))

        settingListView.adapter = SettingListViewAdapter(arrayOfSettingItem, layoutInflater)
        settingListView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d("position==", position.toString())
                val intent = Intent(applicationContext, SettingDetailActivity::class.java)
                intent.putExtra("setting", position)
                startActivity(intent)

            }
        })
    }




}
