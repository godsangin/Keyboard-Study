package com.myhome.rpgkeyboard.setting.custom

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginRight
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhome.rpgkeyboard.R

class SettingCustomActivity : AppCompatActivity() {
    lateinit var oneHandItem:ConstraintLayout
    lateinit var customItem:ConstraintLayout
    lateinit var keyboardView:LinearLayout
    lateinit var customView:RecyclerView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var customSharedPreferences: SharedPreferences
    lateinit var viewWithKeyboardButton:Button
    lateinit var viewWithCustomViewButton:Button
    lateinit var checkGridView:RecyclerView
    lateinit var checkRecyclerAdapter:CheckRecyclerAdapter
    lateinit var checkedListener: CheckedListener
    lateinit var customViewAdapter: CustomViewAdapter
    lateinit var settingHeader:ConstraintLayout
    lateinit var items:ArrayList<CheckGridItem>
    lateinit var selectableItems:ArrayList<CheckGridItem>

    internal lateinit var db:CustomItemDBHelper
    internal lateinit var selectedDB:CustomSelectedItemDBHelper
    var customFunctionAllowed:Boolean = false
    var oneHandMode:Int = -1
    var customViewEnable:Int = -1
    var customItemCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_custom)

        oneHandItem = findViewById(R.id.setting_onehand_item)
        customItem = findViewById(R.id.setting_custom_item)
        keyboardView = findViewById(R.id.keyboard_view)
        customView = findViewById(R.id.keyboard_custom_view)
        viewWithKeyboardButton = findViewById(R.id.keyboard_view_bt)
        viewWithCustomViewButton = findViewById(R.id.custom_view_bt)
        checkGridView = findViewById(R.id.check_gridview)
        settingHeader = findViewById(R.id.setting_header)

        db = CustomItemDBHelper(this)
        selectedDB = CustomSelectedItemDBHelper(this)
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE)
        customSharedPreferences = getSharedPreferences("custom", Context.MODE_PRIVATE)

        items = ArrayList()
        selectableItems=ArrayList()
        oneHandMode = sharedPreferences.getInt("oneHandMode", -1)
        customViewEnable = sharedPreferences.getInt("customViewEnable", -1)
        //items꺼내오기
        checkedListener = object: CheckedListener{
            override fun isChecked(item: CheckGridItem) {
//                items.add(item)
                selectedDB.addItem(item)
                items = selectedDB.allItem
                val gl = GridLayoutManager(applicationContext,items.size)
                customView.layoutManager = gl
                customView.setHasFixedSize(true)
                customViewAdapter = CustomViewAdapter(applicationContext, items)
                customView.adapter = customViewAdapter
            }

            override fun nonChecked(item: CheckGridItem) {
//                items.remove(item)
                selectedDB.deleteItem(item)
                items = selectedDB.allItem
                if(items.size > 0){
                    val gl = GridLayoutManager(applicationContext,items.size)
                    customView.layoutManager = gl
                    customView.setHasFixedSize(true)
                }
                customViewAdapter = CustomViewAdapter(applicationContext, items)
                customView.adapter = customViewAdapter
            }

            override fun notifyDataSetChanged() {
                selectableItems.clear()
                selectableItems.add(CheckGridItem("커서이동1", "<", false))
                selectableItems.add(CheckGridItem("커서이동2", ">", false))
                selectableItems.add(CheckGridItem("붙혀넣기", "붙혀넣기", false))
                selectableItems.add(CheckGridItem("이모티콘", "\uD83D\uDE00", false))
                for(item in db.allItem){
                    selectableItems.add(item)
                }
                customItemCount = selectableItems.size - 3
                selectableItems.add(CheckGridItem("자동완성" + customItemCount, "", true))
                setCheckGroup()
            }
        }

        Log.d("selectableItems==", selectableItems.size.toString())
        selectableItems.add(CheckGridItem("커서이동1", "<", false))
        selectableItems.add(CheckGridItem("커서이동2", ">", false))
        selectableItems.add(CheckGridItem("붙혀넣기", "붙혀넣기", false))
        selectableItems.add(CheckGridItem("이모티콘", "\uD83D\uDE00", false))
        for(item in db.allItem){
            selectableItems.add(item)
        }
        customItemCount = selectableItems.size - 3
        selectableItems.add(CheckGridItem("자동완성" + customItemCount, "", true))

        setComponents()
        setViewMethodButtons()
        setHeader()
    }

    private fun setComponents(){
        val oneHandItemTitle = oneHandItem.findViewById<TextView>(R.id.item_title)
        val oneHandItemContent = oneHandItem.findViewById<TextView>(R.id.item_content)
        val oneHandItemCheckBox = oneHandItem.findViewById<CheckBox>(R.id.checkbox)
        val oneHandItemSeekBar = oneHandItem.findViewById<SeekBar>(R.id.seekbar)

        val customItemTitle = customItem.findViewById<TextView>(R.id.item_title)
        val customItemContent = customItem.findViewById<TextView>(R.id.item_content)
        val customItemCheckBox = customItem.findViewById<CheckBox>(R.id.checkbox)

        oneHandItemTitle.text = "한손 조작모드"
        oneHandItemContent.text = "키보드를 한손으로 조작할 수 있는 크기로 변경합니다."
        oneHandItemCheckBox.visibility = View.VISIBLE
        oneHandItemCheckBox.setOnCheckedChangeListener(oneHandItemCheckBoxListener(oneHandItemSeekBar))
        customItemTitle.text = "편의 기능 추가"
        customItemContent.text = "키보드에 편의기능을 추가합니다."
        customItemCheckBox.visibility = View.VISIBLE
        customItemCheckBox.setOnCheckedChangeListener(customItemCheckBoxListener())
        oneHandItemSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, position: Int, p2: Boolean) {
                if(position == 0){
                    keyboardView.setPadding(0,0,150,0)
                }
                else if(position == 1){
                    keyboardView.setPadding(75,0,75,0)
                }
                else{
                    keyboardView.setPadding(150,0,0,0)
                }
                oneHandMode = position
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        if(oneHandMode >= 0){
            oneHandItemCheckBox.isChecked = true
            oneHandItemSeekBar.visibility = View.VISIBLE
            oneHandItemSeekBar.max = 2
            oneHandItemSeekBar.progress = oneHandMode

            when(oneHandMode){//초기설정
                0 -> {
                    keyboardView.setPadding(0,0,150,0)
                }
                1 -> {
                    keyboardView.setPadding(75,0,75,0)
                }
                2 -> {
                    keyboardView.setPadding(150,0,0,0)
                }
            }
        }
        if(customViewEnable >= 0){
            customItemCheckBox.isChecked = true
            customFunctionAllowed = true
        }

        keyboardView.visibility = View.VISIBLE
    }

    private fun oneHandItemCheckBoxListener(oneHandItemSeekBar:SeekBar):CompoundButton.OnCheckedChangeListener{
        return object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    oneHandMode = 1
                    oneHandItemSeekBar.progress = oneHandMode
                    oneHandItemSeekBar.visibility = View.VISIBLE
                    oneHandItemSeekBar.max = 2
                }
                else{
                    oneHandItemSeekBar.visibility = View.GONE
                    oneHandMode = -1
                    keyboardView.setPadding(0,0,0,0)
                }
            }
        }
    }
    private fun customItemCheckBoxListener():CompoundButton.OnCheckedChangeListener{
        return object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    //나중에 확인 누르면 저장되게 변경
                    val editor = sharedPreferences.edit()
                    editor.putInt("customViewEnable", 0)
                    editor.commit()
                    customFunctionAllowed = true
                }
                else{
                    val editor = sharedPreferences.edit()
                    editor.remove("customViewEnable")
                    editor.commit()
                    customFunctionAllowed = false
                }
            }
        }
    }

    private fun setViewMethodButtons(){
        viewWithCustomViewButton.setOnClickListener(View.OnClickListener {
            keyboardView.visibility = View.GONE
            customView.visibility = View.VISIBLE
            if(!customFunctionAllowed){
                Toast.makeText(applicationContext, "먼저 편의 기능 추가를 허용해야합니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                items.clear()
                checkGridView.visibility = View.VISIBLE
                setCustomGroup()
                setCheckGroup()
            }
        })
        viewWithKeyboardButton.setOnClickListener(View.OnClickListener {
            customView.visibility = View.GONE
            checkGridView.visibility = View.GONE
            keyboardView.visibility = View.VISIBLE
            val params = keyboardView.layoutParams as LinearLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            keyboardView.layoutParams = params
        })
    }

    private fun setCheckGroup(){
        checkRecyclerAdapter = CheckRecyclerAdapter(applicationContext, this, selectableItems, checkedListener, items)
//        checkGridAdapter = CheckGridAdapter(applicationContext, this, selectableItems, checkedListener, items)
        val gl = GridLayoutManager(applicationContext,3)
        checkGridView.layoutManager = gl
        checkGridView.setHasFixedSize(true)
        checkGridView.adapter = checkRecyclerAdapter


    }

    private fun setCustomGroup(){
        val height = sharedPreferences.getInt("keyboardHeight", 100)
        val config = applicationContext.getResources().configuration

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            customView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (height*0.7).toInt())
        }else{
            customView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        }
//        val checkedGroup = customSharedPreferences.all
//        for(str in checkedGroup.keys){
//            if(customSharedPreferences.getBoolean(str, false)){
//                items.add(getItem(str)!!)
//            }
//        }
        items = selectedDB.allItem

        customViewAdapter = CustomViewAdapter(applicationContext, items)
        if(items.size > 0){
            val gl = GridLayoutManager(applicationContext,items.size)
            customView.layoutManager = gl
            customView.setHasFixedSize(true)
        }
        customView.adapter = customViewAdapter
    }

    private fun setHeader(){
        val headerTitle = settingHeader.findViewById<TextView>(R.id.header_title)
        val submitText = settingHeader.findViewById<TextView>(R.id.submit_text)

        headerTitle.text = "사용자 설정"

        submitText.setOnClickListener(View.OnClickListener {
            val editor = sharedPreferences.edit()
            editor.putInt("oneHandMode", oneHandMode)
            Log.d("one==", oneHandMode.toString())
            editor.commit()
            val customEditor = customSharedPreferences.edit()
//            customEditor.clear()
            for(item in items){
                customEditor.putBoolean(item.itemTitle, true)
            }
            customEditor.commit()
            finish()
        })
    }

}
