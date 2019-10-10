package com.myhome.rpgkeyboard.setting.custom

interface CheckedListener {
    fun isChecked(item:CheckGridItem)
    fun nonChecked(item:CheckGridItem)
    fun notifyDataSetChanged()
}