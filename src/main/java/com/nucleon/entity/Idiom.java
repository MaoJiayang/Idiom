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
    //不允许同音的可接龙成语个数,用于计算分数
    private int notAllowHomophoneNum;
    //允许同音的可接龙成语个数,用于计算分数
    private int allowHomophoneNum;
    private boolean isCommonlyUsed;
    private int state = 0;//正常状态为0,生成随机龙头时为1

    public Idiom(int state) {
        this.state = state;//一般情况无需调用这个构造函数.只有在需要返回状态(成语提示,错误信息)成语时才需要调用
        if (state == 404) {
            this.word = "404 not found.\n如果看到这条消息,可能是游戏流程中没有判断接口返回的成语是否存在.";
        }
        if (state == 403){
            this.word = "403 forbidden.\n如果看到这条消息,意味着该提示词语会导致电脑接不了龙.这种情况下,在游戏流程里不提供这个提示成语最好";
        }
    }
    public Idiom() {
    }
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
    public void setNotAllowHomophoneNum(int notAllowHomophoneNum) {
        this.notAllowHomophoneNum = notAllowHomophoneNum;
    }
    public void setAllowHomophoneNum(int allowHomophoneNum) {
        this.allowHomophoneNum = allowHomophoneNum;
    }
    public int getNotAllowHomophoneNum() {
        return notAllowHomophoneNum;
    }
    public int getAllowHomophoneNum() {
        return allowHomophoneNum;
    }
    public void setCommonlyUsed(boolean isCommonlyUsed) {
        this.isCommonlyUsed = isCommonlyUsed;
    }
    public boolean getCommonlyUsed() {
        return isCommonlyUsed;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }
}
