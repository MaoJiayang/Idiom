package com.nucleon.game;

import com.alibaba.fastjson.JSON;
import com.nucleon.entity.ChineseCharacter;
import com.nucleon.entity.Idiom;
import com.nucleon.util.DataUtil;
import com.nucleon.util.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GameFlow {
    protected Scanner sc = new Scanner(System.in);
    GameFlow gameFlow;

    // 存储某个拼音(无声调)对应的所有汉字
    // 如{"wang":['汪','王','网','忘',...]}
    protected static Map<String, Set<Character>> pinyinZiListMap = new HashMap<>();

    // 存储以某个汉字开头的所有成语
    // 如以'我'开头的成语：{‘我’：["我黼子佩","我负子戴","我见犹怜","我武惟扬","我心如秤","我行我素","我醉欲眠"]}
    protected static Map<Character, List<String>> initialWordListMap = new HashMap<>();

    // 存储每个成语对应的详细信息
    // 如{"我行我素":{"abbreviation":"wxws","derivation":...,"example":...,"explanation":...,"CharacterList":[...]}}
    protected static Map<String, Idiom> wordIdiomMap = new HashMap<>();

    //存储某个成语在全量成语中的可接龙成语个数,分为不允许同音和允许同音两种情况



    //存储游戏中已经使用过的成语
    //如{"我行我素":true,"我见犹怜":true,...}
    protected static Map<String, Boolean> usedWordMap = new HashMap<>();

    public GameFlow() {
    }

    public GameFlow(int type) {
        if (type == 1) {
            gameFlow = new Dict();
        } else {
            gameFlow = new SolitaireGame();
        }
    }

    public void startGame() {
        // 读取数据文件
        loadData();
        // 选择游戏，如果gameFlow不为null则跳过选择
        if (gameFlow == null) {
            selectGame();
        }
        // 进入游戏主要流程
        mainFlow();
    }


    private void loadData() {
        /*
         * 读取数据文件,并将数据文件中的数据转换成成语对象列表
         * 完成的任务分别有:
         * 1.将成语对象列表存入idioms中,并完成对每个成语的每个字和拼音的处理,并标记成语是否是常用成语
         * 2.生成pinyinZiListMap,存储某个拼音(无声调)对应的所有汉字
         * 3.生成initialWordListMap,存储以某个汉字开头的所有成语
         * 4.生成wordIdiomMap,存储每个成语对应的详细信息
         * 5.初始化idioms对象的某个成语在全量成语中的可接龙成语个数属性,分为不允许同音和允许同音两种情况
         */
        String data = DataUtil.readData("idiom.json");
        String commonData = DataUtil.readData("common_idiom.json");
        if (data == null || commonData == null) {
            System.err.println("无法读取数据文件!");
            exit();
        }

        List<Idiom> idioms = null;
        List<Idiom> commonIdioms = null;
        try {
            idioms = JSON.parseArray(data, Idiom.class);
            commonIdioms = JSON.parseArray(commonData, Idiom.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (idioms == null || idioms.size() < 1 || commonIdioms == null || commonIdioms.size() < 1) {
            System.err.println("数据文件非法!");
            return;
        }

        for (Idiom idiom : idioms) {

            String word = idiom.getWord();
            String pinyin = idiom.getPinyin();
            //对于每个成语,判断其是否是常用成语
            if (word == null || word.length() < 1) {
                continue;
            }
            boolean isCommon = false;
            for (Idiom commonIdiom : commonIdioms) {
                if (word.equals(commonIdiom.getWord())) {
                    isCommon = true;
                    break;
                }
            }
            idiom.setCommonlyUsed(isCommon);
            //拼音和汉字处理
            if (word == null || word.length() < 1 || pinyin == null || pinyin.length() < 1) {
                continue;
            }
            String[] pinyinArr = pinyin.split(" ");//拆分拼音,存到数组中
            if (word.length() != pinyinArr.length) {
                continue;
            }
            Character initial = null;
            List<ChineseCharacter> CharacterList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {   //对于每个成语,遍历每个字创建ChineseCharacter对象
                ChineseCharacter Character = new ChineseCharacter();
                char zi = word.charAt(i);//获取成语中的第i个字
                String pinyinWithoutTone = PinyinHelper.getPinyinWithoutTone(pinyinArr[i]);//获取每个字的拼音(不带音调)
                Character.setZi(zi);
                Character.setPinyin(pinyinWithoutTone);
                CharacterList.add(Character);//将每个字存入成语对象中
                if (i == 0) {
                    initial = zi;
                }
//以上部分是对成语,成语的每个字和拼音,以及是否常用的处理,下面是对成语的其他信息的处理
                if (pinyinZiListMap.containsKey(pinyinWithoutTone)) {//同音字列表预处理.如果已经存在该拼音,则将该字加入到对应的集合中
                    pinyinZiListMap.get(pinyinWithoutTone).add(zi);
                } else {//如果不存在该拼音,则创建一个新的集合,并将该字加入到集合中
                    Set<Character> ziSet = new HashSet<>();
                    ziSet.add(zi);
                    pinyinZiListMap.put(pinyinWithoutTone, ziSet);
                }
            }
            idiom.setCharacterList(CharacterList);//将成语的每个字存入成语对象中
            wordIdiomMap.put(word, idiom);//成语字符-成语对象列表预处理.将成语和对应的详细信息存入map中

            if (initialWordListMap.containsKey(initial)) {//首字成语列表预处理.如果首字列表中已经存在该首字,则将该成语加入到对应的集合中
                initialWordListMap.get(initial).add(word);
            } else {//如果首字列表中不存在该首字,则创建一个新的集合,并将该成语加入到集合中
                List<String> list = new ArrayList<>();
                list.add(word);
                initialWordListMap.put(initial, list);
            }
        }
        //计算每个成语的可接龙成语个数
        for (Idiom idiom : idioms) {
        // 计算不允许同音的可接龙成语个数
            char lastChar = idiom.getCharacterList().get(idiom.getCharacterList().size() - 1).getZi();
            int notAllowHomophoneNum = initialWordListMap.get(lastChar).size() - 1;
            idiom.setNotAllowHomophoneNum(notAllowHomophoneNum);

            // 计算允许同音的可接龙成语个数
            ChineseCharacter lastChineseCharacter = idiom.getCharacterList().get(idiom.getCharacterList().size() - 1);
            String pinyinWithoutTone = lastChineseCharacter.getPinyin();
            int allowHomophoneNum = pinyinZiListMap.get(pinyinWithoutTone).size() - 1;
            idiom.setAllowHomophoneNum(allowHomophoneNum);
        }
    }

    private void selectGame() {//选择游戏
        System.out.println("请选择游戏: 1-成语词典 2-成语接龙");
        System.out.print(">>>");
        String mode = sc.nextLine();
        if ("1".equals(mode)) {
            gameFlow = new Dict();
        } else {
            gameFlow = new SolitaireGame();
        }
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
