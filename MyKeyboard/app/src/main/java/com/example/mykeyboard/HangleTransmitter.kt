package com.example.mykeyboard

class HangleTransmitter{
    private var c:Char
    private var englishList:ArrayList<Char>
    private var hangulList:List<Char>
    constructor(c:Char){
        this.c = c
        englishList = ArrayList<Char>()
        hangulList = ArrayList<Char>()
        initEng()
        initHan()
    }
    private fun initEng(){
        for(ec in 'a' .. 'z'){
            englishList.add(ec)
        }
    }

    private fun initHan(){
        for(ec in 'ㄱ' .. 'ㅎ'){

        }
    }
}