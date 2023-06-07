package com.wanghao.game;


import com.wanghao.entity.Hanzi;
import com.wanghao.entity.Idiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wanghao
 * @description 成语接龙
 */
public class SolitaireGame extends GameFlow {
    private boolean playerFirst;
    private Difficulty difficulty;

    protected SolitaireGame() {
        System.out.println("欢迎来到成语接龙! exit退出");
    }

    //选择难度
    private void selectDifficulty() {
        System.out.println("请选择难度： 1-简单 2-中等 3-困难");
        System.out.print(">>>");
        String d = sc.nextLine();
        if ("1".equals(d)) {
            System.out.println("【简单】答对1个成语或AI接龙失败则玩家获胜，累计答错5个成语则玩家失败，允许同音不同调");
            difficulty = new Difficulty(1, 5, true);
        } else if ("2".equals(d)) {
            System.out.println("【中等】连续答对3个成语或AI接龙失败则玩家获胜，累计答错5个成语则玩家失败，允许同音不同调");
            difficulty = new Difficulty(3, 5, true);
        } else {
            System.out.println("【困难】连续答对5个成语或AI接龙失败则玩家获胜，累计答错3个成语则玩家失败，不允许同音不同调");
            difficulty = new Difficulty(5, 3, false);
        }
    }

    //选择先后手
    private void selectOrder() {
        System.out.println("请选择先后手: 1-先手 2-后手");
        System.out.print(">>>");
        String order = sc.nextLine();
        playerFirst = "1".equals(order);
    }

    private void doMainFlow() {
        int round = 0;
        int playerWonRounds = 0;//玩家获胜局数
        int playerLostRounds = 0;//玩家失败局数
        while (true) {
            round++;
            System.out.println("----------第"+round+"局----------");
            int playerLoseNum = 0;//玩家累计答错次数
            int playerStreakWinNum = 0;//玩家连续答对次数
            boolean allowFurtherSearch = difficulty.allowFurtherSearch;
            String lastComputerWord = null;
            while (true) {
                if (!playerFirst && lastComputerWord == null) {//电脑先手且是电脑第一次输出成语
                    lastComputerWord = pickRandomIdiom();
                    if (lastComputerWord == null) {
                        System.out.println("我想不出来了:( 你赢了!");
                        playerWonRounds++;
                        break;
                    } else {
                        System.out.println(lastComputerWord);
                    }
                }

                System.out.print("输入一个成语>>>");
                String input = sc.nextLine();
                if ("exit".equals(input)) {
                    printResult(playerWonRounds, playerLostRounds);
                    exit();
                }

                String thisPlayerWord = input;
                if (!isValidIdiom(thisPlayerWord)) { //输入的成语不合法
                    playerLoseNum++;//累计答错次数加1
                    playerStreakWinNum = 0;//连续答对次数清零
                    System.out.println("【" + thisPlayerWord + "】 不是成语!");
                    if (playerLost(playerLoseNum)) {
                        System.out.println("累计答错"+playerLoseNum+"次，你输了!");
                        playerLostRounds++;
                        break;
                    }
                    continue;
                }
                if (!isSolitaire(thisPlayerWord, lastComputerWord, allowFurtherSearch)) { //成语不符合接龙规则
                    playerLoseNum++;//累计答错次数加1
                    playerStreakWinNum = 0;//连续答对次数清零
                    System.out.println("【" + thisPlayerWord + "】 不符合接龙规则!");
                    if (playerLost(playerLoseNum)) {
                        System.out.println("累计答错"+playerLoseNum+"次，你输了!");
                        playerLostRounds++;
                        break;
                    }
                    continue;
                }

                if (lastComputerWord != null) {
                    // 玩家先手的情况下第一个成语不算答对
                    playerStreakWinNum++;
                }
                if (playerWon(playerStreakWinNum)) {
                    System.out.println("连续答对"+playerStreakWinNum+"次，你赢了!");
                    playerWonRounds++;
                    break;
                }

                String thisComputerWord;
                Idiom idiom = getNextIdiom(thisPlayerWord, allowFurtherSearch);
                if (idiom == null) {
                    System.out.println("我想不出来了:( 你赢了!");
                    playerWonRounds++;
                    break;
                } else {
                    thisComputerWord = idiom.getWord();
                }
                if (thisComputerWord == null) {
                    System.out.println("我想不出来了:( 你赢了!");
                    playerWonRounds++;
                    break;
                } else {
                    System.out.println(thisComputerWord);
                }
                lastComputerWord = thisComputerWord;
            }
        }

    }

    private void printResult(final int wonRounds, final int lostRounds) {
        System.out.println("-----结果：共"+(wonRounds+lostRounds)+"局，你赢了"+wonRounds+"局，输了"+lostRounds+"局-----");
    }

    @Override
    protected void mainFlow() {
        selectDifficulty();

        selectOrder();

        doMainFlow();
    }

    /** 查找以thisWord成语末字开头的成语。
     *  不能与thisWord重复，若找不到，则查找以与末字同音不同调的字为开头的成语，再找不到则返回null
     */
    private Idiom getNextIdiom(final String thisWord, boolean allowFurtherSearch) {
        if (thisWord == null || thisWord.length() < 1) {
            return null;
        }
        Idiom thisIdiom = wordIdiomMap.get(thisWord);
        if (thisIdiom == null) {
            return null;
        }

        char lastZi = thisWord.charAt(thisWord.length()-1);
        List<String> candidateWordList = initialWordListMap.get(lastZi);
        if (candidateWordList == null || candidateWordList.size() < 1) {
            //查找与last同音不同调的字
            return getNextIdiomFurther(lastZi, thisIdiom, allowFurtherSearch);
        }

        int candidateSize = candidateWordList.size();
        if (candidateSize == 1 && thisWord.equals(candidateWordList.get(0))) {
            //查找与last同音不同调的字
            return getNextIdiomFurther(lastZi, thisIdiom, allowFurtherSearch);
        }
        int idx = (int) (candidateSize * Math.random());
        String nextWord = candidateWordList.get(idx);
        int loopNum = 0;
        while (loopNum < 100) {
            //最多循环100次，防止死循环
            if (!thisWord.equals(nextWord)) {
                return wordIdiomMap.get(nextWord);
            }
            idx = (int) (candidateSize * Math.random());
            nextWord = candidateWordList.get(idx);
            loopNum++;
        }
        return null;
    }

    /** 查找与lastZi同音不同调的字为开头的成语
     */
    private Idiom getNextIdiomFurther(final char lastZi, final Idiom thisIdiom, boolean allowFurtherSearch) {
        if (!allowFurtherSearch) {
            return null;
        }
        if (thisIdiom == null) {
            return null;
        }
        List<Hanzi> hanziList = thisIdiom.getHanziList();
        if (hanziList == null || hanziList.size() < 1) {
            return null;
        }
        String pinyinOfLast = null;
        for (Hanzi hanzi : hanziList) {
            if (hanzi.getZi() == lastZi) {
                pinyinOfLast = hanzi.getPinyin();
                break;
            }
        }
        if (pinyinOfLast == null) {
            return null;
        }

        Set<Character> ziSet = pinyinZiListMap.get(pinyinOfLast);
        if (ziSet == null || ziSet.size() < 1) {
            return null;
        }
        int size = ziSet.size();
        if (size == 1 && ziSet.contains(lastZi)) {
            return null;
        }
        List<Character> ziList = new ArrayList<>(ziSet);
        int idx = (int) (size * Math.random());
        Character zi = ziList.get(idx);
        int loopNum = 0;
        while (loopNum < 100) {
            if (lastZi != zi) {
                List<String> wordList = initialWordListMap.get(zi);
                if (wordList != null && wordList.size() > 0) {
                    return wordIdiomMap.get(wordList.get(0));
                }
            }
            idx = (int) (size * Math.random());
            zi = ziList.get(idx);
            loopNum++;
        }

        return null;
    }

    private boolean isValidIdiom(final String word) {
        if (word == null) {
            return false;
        }
        return wordIdiomMap.get(word) != null;
    }

    private boolean isSolitaire(final String thisWord, final String lastWord, final boolean allowFurtherSearch) {
        if (thisWord == null || thisWord.length() < 1) {
            return false;
        }
        if (lastWord == null) {
            return true;
        }
        if (lastWord.length() < 1) {
            return false;
        }
        if (thisWord.equals(lastWord)) {
            //不允许与电脑的成语相同
            return false;
        }

        char lastCharOfLastWord = lastWord.charAt(lastWord.length()-1);
        char firstCharOfThisWord = thisWord.charAt(0);
        if (lastCharOfLastWord == firstCharOfThisWord) {
            return true;
        }

        if (!allowFurtherSearch) {//不允许同音不同调
            return false;
        }
        //允许同音不同调
        Idiom thisIdiom = wordIdiomMap.get(thisWord);
        Idiom lastIdiom = wordIdiomMap.get(lastWord);
        if (thisIdiom == null || thisIdiom.getHanziList() == null
                || lastIdiom == null || lastIdiom.getHanziList() == null) {
            return false;
        }
        int thisIdiomHanziNum = thisIdiom.getHanziList().size();
        int lastIdiomHanziNum = lastIdiom.getHanziList().size();
        if (thisIdiomHanziNum < 1 || lastIdiomHanziNum < 1) {
            return false;
        }
        String lastPinyinOfLastIdiom = lastIdiom.getHanziList().get(lastIdiomHanziNum-1).getPinyin();
        String firstPinyinOfThisIdiom = thisIdiom.getHanziList().get(0).getPinyin();
        if (lastPinyinOfLastIdiom != null && lastPinyinOfLastIdiom.equals(firstPinyinOfThisIdiom)) {
            return true;
        }

        return false;
    }

    private String pickRandomIdiom() {
        List<String> words = new ArrayList<>(wordIdiomMap.keySet());
        int idx = (int) (words.size() * Math.random());
        return words.get(idx);
    }

    // 游戏难度
    private class Difficulty {
        //玩家连续答对次数
        private int playerStreakWinNum;
        //玩家累计答错次数
        private int playerLoseNum;
        //是否允许同音不同调
        private boolean allowFurtherSearch;

        Difficulty(int playerStreakWinNum, int playerLoseNum, boolean allowFurtherSearch) {
            this.playerStreakWinNum = playerStreakWinNum;
            this.playerLoseNum = playerLoseNum;
            this.allowFurtherSearch = allowFurtherSearch;
        }
    }

    // 连续答对次数超过设定，则玩家赢了
    private boolean playerWon(final int playerStreakWinNum) {
        return playerStreakWinNum >= difficulty.playerStreakWinNum;
    }
    // 累计答错次数超过设定，则玩家输了
    private boolean playerLost(final int playerLoseNum) {
        return playerLoseNum >= difficulty.playerLoseNum;
    }
}
