package com.myhome.rpgkeyboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.myhome.rpgkeyboard.setting.*
import com.myhome.rpgkeyboard.setting.custom.SettingCustomActivity
import com.myhome.rpgkeyboard.setting.SettingItem

class MainActivity : AppCompatActivity() {
    lateinit var settingListView: ListView
    val REQUEST_INPUT_METHOD_SETTING = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingListView = findViewById(R.id.setting_listview)

        var arrayOfSettingItem = ArrayList<SettingItem>()
        arrayOfSettingItem.add(
            SettingItem(
                "키보드 설정",
                "노타키보드를 추가하고 설정 가능한 키보드를 확인합니다.",
                false,
                ""
            )
        )
        arrayOfSettingItem.add(
            SettingItem(
                "소리 및 진동 설정",
                "터치 시 발생하는 소리 또는 진동에 대한 설정을 변경합니다.",
                false,
                ""
            )
        )
        arrayOfSettingItem.add(
            SettingItem(
                "키보드 크기 및 종류 설정",
                "키 패드 또는 키보드의 높이와 입력방식을 쿼티 또는 천지인으로 변경할 수 있습니다.",
                false,
                ""
            )
        )
        arrayOfSettingItem.add(
            SettingItem(
                "편의 기능 설정",
                "한 손 조작모드 또는 자주쓰는 키 패드를 추가하거나 변경합니다.",
                false,
                ""
            )
        )

        settingListView.adapter =
            SettingListViewAdapter(arrayOfSettingItem, layoutInflater)
        settingListView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(position == 0){
                    val intent = Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                    startActivityForResult(intent, REQUEST_INPUT_METHOD_SETTING)
                }
                else if(position == 3){
                    val intent = Intent(applicationContext, SettingCustomActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(applicationContext, SettingDetailActivity::class.java)
                    intent.putExtra("setting", position)
                    startActivity(intent)
                }
            }
        })
    }




}
