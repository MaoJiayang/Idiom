package com.nucleon.entity;
import java.util.List;

public class Idiom {
    //成语
    private String word;
    //拼音（带音调）
    private String pinyin;
    //英文缩写
    private String abbreviation;
    //出处
    private String derivation;
    //释义
    private String explanation;
    //举例
    private String example;
    //汉字列表
    private List<ChineseCharacter> CharacterList;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public List<ChineseCharacter> getCharacterList() {
        return CharacterList;
    }

    public void setCharacterList(List<ChineseCharacter> CharacterList) {
        this.CharacterList = CharacterList;
    }
}
