package com.wanghao.entity;

/**
 * @author wanghao
 * @description 汉字
 */
public class Hanzi {
    //字
    private char zi;
    //不带音调的拼音
    private String pinyin;

    public char getZi() {
        return zi;
    }

    public void setZi(char zi) {
        this.zi = zi;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
