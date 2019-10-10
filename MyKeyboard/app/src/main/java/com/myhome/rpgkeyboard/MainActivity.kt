package com.myhome.rpgkeyboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.myhome.rpgkeyboard.setting.*
import com.myhome.rpgkeyboard.setting.custom.SettingCustomActivity
import com.myhome.rpgkeyboard.setting.SettingItem

class MainActivity : AppCompatActivity() {
    lateinit var settingListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingListView = findViewById(R.id.setting_listview)

        var arrayOfSettingItem = ArrayList<SettingItem>()
        arrayOfSettingItem.add(
            SettingItem(
                "키보드 스킨 설정",
                "키보드를 누르는데 발생하는 이미지나 애니메이션, 키보드 테마 등을 변경합니다.",
                false,
                ""
            )
        )
        arrayOfSettingItem.add(
            SettingItem(
                "키보드 자판 설정",
                "쿼티, 천지인 등 키보드 자판의 설정을 변경합니다.",
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
                "키보드 크기 설정",
                "키 패드 또는 키보드의 높이를 설정합니다.",
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
                Log.d("position==", position.toString())
                if(position == 0){
//                    val intent = Intent(applicationContext, SettingThemeActivity::class.java)
//                    startActivity(intent)
                    Toast.makeText(applicationContext, "업데이트예정입니다.", Toast.LENGTH_SHORT).show()
                }
                else if(position == 1){
                    Toast.makeText(applicationContext, "준비중입니다.", Toast.LENGTH_SHORT).show()
                }
                else if(position == 4){
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
