package com.wanghao.game;

import com.alibaba.fastjson.JSON;
import com.wanghao.entity.Hanzi;
import com.wanghao.entity.Idiom;
import com.wanghao.util.DataUtil;
import com.wanghao.util.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author wanghao
 * @description
 */
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
    // 如{"我行我素":{"abbreviation":"wxws","derivation":...,"example":...,"explanation":...,"hanziList":[...]}}
    protected static Map<String, Idiom> wordIdiomMap = new HashMap<>();

    public GameFlow() {
    }

    public GameFlow(int type) {
        if (type == 1) {
            gameFlow = new DictGame();
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
        String data = DataUtil.readData();
        if (data == null) {
            System.err.println("无法读取数据文件!");
            exit();
        }

        List<Idiom> idioms = null;
        try {
            idioms = JSON.parseArray(data, Idiom.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (idioms == null || idioms.size() < 1) {
            System.err.println("数据文件非法!");
            return;
        }

        for (Idiom idiom : idioms) {
            String word = idiom.getWord();
            String pinyin = idiom.getPinyin();
            if (word == null || word.length() < 1 || pinyin == null || pinyin.length() < 1) {
                continue;
            }
            String[] pinyinArr = pinyin.split(" ");
            if (word.length() != pinyinArr.length) {
                continue;
            }

            Character initial = null;
            List<Hanzi> hanziList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                Hanzi hanzi = new Hanzi();
                char zi = word.charAt(i);
                String pinyinWithoutTone = PinyinHelper.getPinyinWithoutTone(pinyinArr[i]);
                hanzi.setZi(zi);
                hanzi.setPinyin(pinyinWithoutTone);
                hanziList.add(hanzi);
                if (i == 0) {
                    initial = zi;
                }

                if (pinyinZiListMap.containsKey(pinyinWithoutTone)) {
                    pinyinZiListMap.get(pinyinWithoutTone).add(zi);
                } else {
                    Set<Character> ziSet = new HashSet<>();
                    ziSet.add(zi);
                    pinyinZiListMap.put(pinyinWithoutTone, ziSet);
                }
            }
            idiom.setHanziList(hanziList);
            wordIdiomMap.put(word, idiom);

            if (initialWordListMap.containsKey(initial)) {
                initialWordListMap.get(initial).add(word);
            } else {
                List<String> list = new ArrayList<>();
                list.add(word);
                initialWordListMap.put(initial, list);
            }
        }
    }

    private void selectGame() {
        System.out.println("请选择游戏: 1-成语词典 2-成语接龙");
        System.out.print(">>>");
        String mode = sc.nextLine();
        if ("1".equals(mode)) {
            gameFlow = new DictGame();
        } else {
            gameFlow = new SolitaireGame();
        }
    }

    protected void exit() {
        sc.close();
        System.exit(0);
    }

    protected void mainFlow() {
        if (gameFlow != null) {
            gameFlow.mainFlow();
        }
    }
}
