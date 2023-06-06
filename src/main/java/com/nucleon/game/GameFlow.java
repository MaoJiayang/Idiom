package com.nucleon.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.nucleon.entity.ChineseCharacter;
import com.nucleon.entity.Idiom;
import com.nucleon.util.DataUtil;
import com.nucleon.util.PinyinHelper;

public class GameFlow implements GamingLogic{
    protected Scanner sc = new Scanner(System.in);
    GameFlow gameFlow;

    // 存储某个拼音(无声调)对应的所有汉字,该汉字是成语表中所有成语的首字,用于同音搜索
    // 如{"wang":['汪','王','网','忘',...]}
    protected static Map<String, Set<Character>> pinyinZiListMap = new HashMap<>();

    // 存储以某个汉字开头的所有成语,用于首字搜索
    // 如以'我'开头的成语：{‘我’：["我黼子佩","我负子戴","我见犹怜","我武惟扬","我心如秤","我行我素","我醉欲眠"]}
    protected static Map<Character, List<String>> initialWordListMap = new HashMap<>();

    // 存储每个成语对应的详细信息,用于通过成语查找详细信息
    // 如{"我行我素":{"abbreviation":"wxws","derivation":...,"example":...,"explanation":...,"CharacterList":[...]}}
    protected static Map<String, Idiom> wordIdiomMap = new HashMap<>();

    //存储游戏中已经使用过的成语
    //如{"我行我素","我见犹怜",...}
    protected static Set<Idiom> usedIdioms = new HashSet<>();

    public GameFlow() {
        this.gameFlow = this; 
    }

    public GameFlow(int type) {
        if (type == 1) {
            gameFlow = new Dict();
        } else {
            gameFlow = new SolitaireGame();
        }
    }

    @Override
    public void loadGame() {
        String data = DataUtil.readData("/idiom.json");
        String commonData = DataUtil.readData("/common_idiom.json");
        if (data == null || commonData == null) {
            System.err.println("无法读取数据文件!");
            exit();
        }
        List<Idiom> idioms = JSON.parseArray(data,Idiom.class);//将json字符串转换为List<Idiom>对象
        List<Idiom> commonIdioms = JSON.parseArray(commonData,Idiom.class);//将json字符串转换为List<Idiom>对象
        if (idioms == null || idioms.size() < 1 || commonIdioms == null || commonIdioms.size() < 1) {
            System.err.println("数据文件非法!");
            return;
        }
        System.out.println("数据文件转换成功!"+idioms.size());
        int loopNum = 0;
        Iterator<Idiom> iterator = idioms.iterator();
        while (iterator.hasNext()) {
            Idiom idiom = iterator.next();
            processIdiom(idiom, commonIdioms);
            if (idiom.getCharacterList() == null || idiom.getCharacterList().size() < 1) {
                iterator.remove();
                continue;
            }
            processPinyinZiListMap(idiom);
            processInitialWordListMap(idiom);
            loopNum++;
        }
        System.out.println("数据预处理成功!"+loopNum);
        calculateHomophoneNum(idioms);//计算每个成语的可接龙数,分为普通接龙数和同音接龙数.该函数中调用了两个私有方法
        System.out.println("可接龙数计算成功!");
        //提取idioms中的成语,将其word,notAllowHomophoneNum,allowHomophoneNum用逗号分割打印为txt文件
        DataUtil.writeIdiomsToFile(idioms, "idioms.txt");
        DataUtil.writePinyinZiListMapToFile(pinyinZiListMap, "pinyin_zi_list_map.txt");
        DataUtil.writeInitialWordListMapToFile(initialWordListMap, "initial_word_list_map.txt");
        DataUtil.writeWordIdiomMapToFile(wordIdiomMap, "word_idiom_map.txt");
    }

    @Override
    public void gameSetting(int gameType,Boolean challengeMode,Boolean allowFurtherSearch) {
        if (gameType == 1) {
            gameFlow = new Dict();
        } else {
            gameFlow = new SolitaireGame(challengeMode,allowFurtherSearch);
        }

    }
    @Override
    public Idiom doOneRound(String userIdiom){
        System.out.println("GameFlow中被调用");
        return gameFlow.doOneRound(userIdiom);
    }
    @Override
    public double getCurrentScore(){
        return gameFlow.getCurrentScore();
    }
    @Override
    public Idiom getHint(String computerIdiom){
        return gameFlow.getHint(computerIdiom);
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
        Character lastChar = idiom.getCharacterList().get(idiom.getCharacterList().size() - 1).getZi();
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
        idiom.setAllowHomophoneNum(allowHomophoneNum + idiom.getNotAllowHomophoneNum());
        //System.out.println(idiom.getWord()+"可同音个数处理完毕!"+idiom.getAllowHomophoneNum());
    }

    protected void exit() {//退出游戏
        sc.close();
        System.exit(0);
    }

    protected void mainFlow() {//进入游戏主要流程
        if (gameFlow != null) {
            gameFlow.mainFlow();
        }
    }
}
