package com.nucleon.game;

import com.nucleon.entity.ChineseCharacter;
import com.nucleon.entity.Idiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SolitaireGame extends GameFlow {

    private Difficulty difficulty;

    protected SolitaireGame() {
        System.out.println("欢迎来到成语接龙! exit退出");
    }

    //选择难度
    private void selectDifficulty() {
        System.out.println("请选择难度： 1-娱乐模式 2-挑战模式");
        System.out.print(">>>");
        String d = sc.nextLine();
        if ("1".equals(d)) {
            System.out.println("【娱乐模式】：\n请选择接龙模式：1-普通模式 2-同音模式");
            if ("1".equals(sc.nextLine())) {
                difficulty = new Difficulty(false,false);
            }else {
                difficulty = new Difficulty(true,false);
            }
        }else {
            System.out.println("【挑战模式】：\n请选择接龙模式：1-普通模式 2-同音模式");
            if ("1".equals(sc.nextLine())) {
                difficulty = new Difficulty(false,true);
            } else {
                difficulty = new Difficulty(true,true);
            }
        }
    }

    private void doMainFlow() {
        /*
         * 1.用户输入一个成语作为开头
         * 2.判断成语是否存在
         * 3.判断成语是否已经被使用过
         * 4.判断成语是否符合接龙规则
         * 
         */
        //TODO:完成游戏主要流程
    }



    @Override
    protected void mainFlow() {
        selectDifficulty();

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

        char lastZi = thisWord.charAt(thisWord.length()-1);//成语末字
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
        List<ChineseCharacter> CharacterList = thisIdiom.getCharacterList();
        if (CharacterList == null || CharacterList.size() < 1) {
            return null;
        }
        String pinyinOfLast = null;
        for (ChineseCharacter Character : CharacterList) {
            if (Character.getZi() == lastZi) {
                pinyinOfLast = Character.getPinyin();
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

    /*
     * 电脑是否认输
     */
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
        if (thisIdiom == null || thisIdiom.getCharacterList() == null
                || lastIdiom == null || lastIdiom.getCharacterList() == null) {
            return false;
        }
        int thisIdiomCharacterNum = thisIdiom.getCharacterList().size();
        int lastIdiomCharacterNum = lastIdiom.getCharacterList().size();
        if (thisIdiomCharacterNum < 1 || lastIdiomCharacterNum < 1) {
            return false;
        }
        String lastPinyinOfLastIdiom = lastIdiom.getCharacterList().get(lastIdiomCharacterNum-1).getPinyin();
        String firstPinyinOfThisIdiom = thisIdiom.getCharacterList().get(0).getPinyin();
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
        //游戏模式
        private boolean challengeMode;
        //是否允许同音不同调
        private boolean allowFurtherSearch;

        Difficulty(boolean allowFurtherSearch, boolean challengeMode) {

            this.allowFurtherSearch = allowFurtherSearch;
            this.challengeMode = challengeMode;
        }
    }
    
}
