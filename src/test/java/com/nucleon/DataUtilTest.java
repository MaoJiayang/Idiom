package com.nucleon;
import org.junit.Test;
import static org.junit.Assert.*;
import com.nucleon.util.DataUtil;
import com.nucleon.util.PinyinHelper;
import com.alibaba.fastjson.JSON;
import com.nucleon.entity.ChineseCharacter;
import com.nucleon.entity.Idiom;
import com.nucleon.game.GameFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nucleon.entity.Idiom;
public class DataUtilTest extends GameFlow{
    @Test
    public void testReadData() {
  
        String jsonString = DataUtil.readData("/idiom.json");
        
        assertNotNull(jsonString);
        List<Idiom> idioms = null;
        try {
            idioms = JSON.parseArray(jsonString, Idiom.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(idioms);
        //取出第一条成语,查看其word项目是不是"阿鼻地狱"
        Idiom idiom = idioms.get(0);
        assertEquals("阿鼻地狱", idiom.getWord());
    }
    @Test
    public void loadData() {
        String data = DataUtil.readData("/idiom.json");
        String commonData = DataUtil.readData("/common_idiom.json");
        if (data == null || commonData == null) {
            System.err.println("无法读取数据文件!");
            exit();
        }
        List<Idiom> idioms = parseIdioms(data);//将json字符串转换为List<Idiom>对象
        List<Idiom> commonIdioms = parseIdioms(commonData);//将json字符串转换为List<Idiom>对象
        if (idioms == null || idioms.size() < 1 || commonIdioms == null || commonIdioms.size() < 1) {
            System.err.println("数据文件非法!");
            return;
        }
        System.out.println("数据文件转换成功!"+idioms.size());
        for (Idiom idiom : idioms) {
            processIdiom(idiom, commonIdioms);//处理每个成语对象,将其常用性设置为true或false
            //如果该成语CharacterList为空,则从idioms里删去该成语
            if (idiom.getCharacterList() == null || idiom.getCharacterList().size() < 1) {
                idioms.remove(idiom);
                break;
            }
            processPinyinZiListMap(idiom);//将每个成语对象的拼音和字列表存入pinyinZiListMap
            processInitialWordListMap(idiom);//将每个成语对象的首字(字符)和成语(字符)存入initialWordListMap
        }
        System.out.println("数据预处理成功!");
        calculateHomophoneNum(idioms);//计算每个成语的可接龙数,分为普通接龙数和同音接龙数.该函数中调用了两个私有方法
        System.out.println("可接龙数计算成功!");
    }

    private List<Idiom> parseIdioms(String data) {
        /*
         * 将json字符串转换为List<Idiom>对象
         */
        try {
            return JSON.parseArray(data, Idiom.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processIdiom(Idiom idiom, List<Idiom> commonIdioms) {
        /*
         * 处理每个成语对象,将其常用性设置为true或false
         * 为成语对象添加一个成语字符列表,列表中的每个元素是一个ChineseCharacter对象
         * 其中每个字都有自己的拼音,拼音不带声调
         * 将每个成语对象的word和成语对象本身存入wordIdiomMap
         */
        String word = idiom.getWord();
        String pinyin = idiom.getPinyin();
        if (word == null || word.length() < 1) {
            return;
        }
        boolean isCommon = false;
        for (Idiom commonIdiom : commonIdioms) {
            if (word.equals(commonIdiom.getWord())) {
                isCommon = true;
                break;
            }
        }
        idiom.setCommonlyUsed(isCommon);
        if (word == null || word.length() < 1 || pinyin == null || pinyin.length() < 1) {
            return;
        }
        String[] pinyinArr = pinyin.split("[，,\u3000\\s]+");//将拼音字符串按照"，"或空格分割为字符串数组
        StringBuilder sb = new StringBuilder(word);
        int index = word.indexOf("，");
        while (index >= 0) {
            sb.deleteCharAt(index);
            word = word.substring(0, index) + word.substring(index + 1);
            index = word.indexOf("，");
        }
        List<ChineseCharacter> CharacterList = new ArrayList<>();
        if (word.length() != pinyinArr.length) {
            //如果长度不匹配,return
            return; 
        }

            for (int i = 0; i < word.length(); i++) {
                    ChineseCharacter Character = new ChineseCharacter();
                    char zi = word.charAt(i);
                    String pinyinWithoutTone = PinyinHelper.getPinyinWithoutTone(pinyinArr[i]);
                    Character.setZi(zi);
                    Character.setPinyin(pinyinWithoutTone);
                    CharacterList.add(Character);
            }

        idiom.setCharacterList(CharacterList);
        wordIdiomMap.put(word, idiom);
    }

    private void processPinyinZiListMap(Idiom idiom) {
        /*
         * 将每个成语对象的首字拼音和首字列表存入pinyinZiListMap
         */
        ChineseCharacter Character = idiom.getCharacterList().get(0);
        String pinyinWithoutTone = Character.getPinyin();
        if (pinyinWithoutTone == null || pinyinWithoutTone.isEmpty()) {
            return;
        }
        if (pinyinZiListMap.containsKey(pinyinWithoutTone)) {
            pinyinZiListMap.get(pinyinWithoutTone).add(Character.getZi());
        } else {
            Set<Character> ziSet = new HashSet<>();
            ziSet.add(Character.getZi());
            pinyinZiListMap.put(pinyinWithoutTone, ziSet);
        }
        
    }

    private void processInitialWordListMap(Idiom idiom) {
        /*
         * 将每个成语对象的首字(字符)和成语(字符)存入initialWordListMap
         */
        char initial = idiom.getCharacterList().get(0).getZi();
        if (initialWordListMap.containsKey(initial)) {
            initialWordListMap.get(initial).add(idiom.getWord());
        } else {
            List<String> list = new ArrayList<>();
            list.add(idiom.getWord());
            initialWordListMap.put(initial, list);
        }
    }

    private void calculateHomophoneNum(List<Idiom> idioms) {
        for (Idiom idiom : idioms) {
            calculateNotAllowHomophoneNum(idiom);
            calculateAllowHomophoneNum(idiom);
        }
    }

    private void calculateNotAllowHomophoneNum(Idiom idiom) {
        if (idiom.getCharacterList() == null) {
            idiom.setNotAllowHomophoneNum(0);
            return;
        }
        char lastChar = idiom.getCharacterList().get(idiom.getCharacterList().size() - 1).getZi();
        if (initialWordListMap.get(lastChar) == null || initialWordListMap.get(lastChar).size() < 1) {
            idiom.setNotAllowHomophoneNum(0);
            return;
        }
        int notAllowHomophoneNum = initialWordListMap.get(lastChar).size() - 1;
        idiom.setNotAllowHomophoneNum(notAllowHomophoneNum);
        //System.out.println(idiom.getWord()+"不可同音个数处理完毕!"+idiom.getNotAllowHomophoneNum());
    }

    private void calculateAllowHomophoneNum(Idiom idiom) {
        if (idiom.getCharacterList() == null) {
            idiom.setAllowHomophoneNum(0);
            return;
        }
        ChineseCharacter lastChineseCharacter = idiom.getCharacterList().get(idiom.getCharacterList().size() - 1);
        String pinyinWithoutTone = lastChineseCharacter.getPinyin();
        if (pinyinZiListMap.get(pinyinWithoutTone) == null || pinyinZiListMap.get(pinyinWithoutTone).size() < 1) {
            idiom.setAllowHomophoneNum(0);
            return;
        }
        int allowHomophoneNum = pinyinZiListMap.get(pinyinWithoutTone).size() - 1;
        idiom.setAllowHomophoneNum(allowHomophoneNum);
        //System.out.println(idiom.getWord()+"可同音个数处理完毕!"+idiom.getAllowHomophoneNum());
    }
}

