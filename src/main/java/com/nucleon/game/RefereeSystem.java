package com.nucleon.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.lang.Math;

import com.nucleon.entity.ChineseCharacter;
import com.nucleon.entity.Idiom;

/*
 * 游戏裁判系统,用于控制游戏难度
 * 负责接收游戏流程中的各种信息,并根据游戏难度进行判断,返回对应的结果
 * 裁判系统负责维护游戏流中用到的成员变量,接收信息,判断,返回结果
*/
public class RefereeSystem extends GameFlow{
        
        private boolean challengeMode;//游戏模式
        private boolean allowFurtherSearch;//是否允许同音不同调
        private int currentDifficulty = 0;//当前难度,代表的是最大可接龙数
        private int availableHintCount = 3;//可用提示次数
        private int killNum = 0;//当前杀龙数
        private Idiom previousHintIdiom = null;//上一次提示的成语
        //存储游戏中已经使用过的成语
        //如{"我行我素","我见犹怜",...}
        private static Set<Idiom> usedIdioms = new HashSet<>();

    RefereeSystem(boolean allowFurtherSearch, boolean challengeMode) {//构造函数
        this.allowFurtherSearch = allowFurtherSearch;
        this.challengeMode = challengeMode;
        this.availableHintCount = 3;
        this.killNum = 0;
        this.previousHintIdiom = null;
        if (challengeMode) {
            currentDifficulty = 700;//挑战模式下,难度为700,即初始最大可接龙数为700
        }else {
            currentDifficulty = Integer.MAX_VALUE;//最大值,不限制
        }
        if(usedIdioms != null) {
            usedIdioms.clear();
        }
    }
    @Override
    public Idiom doOneRound(final String idiomString){
    /* 
    * 裁判系统的主要方法之一
    * 一轮游戏.传入的是用户提供的成语字符串
    * 根据规则向游戏流提供一个成语,然后更新已经使用的成语列表
    *提供成语的流程:
    * 1.根据用户输入成语的最后一个字,在成语表中查找所有可接龙的成语
    * 2.从中找出符合当前难度要求(是否允许同音,当前难度数是多少)的接龙成语,并返回
    */  
    System.out.println("裁判系统中被调用");
        if (!idiomNotUsed(idiomString)){//如果成语不合法或已经被使用过
            Idiom errorIdiom = new Idiom(404);
            return errorIdiom;
        }
        if (currentDifficulty < 0){//每轮难度递减100,如果难度小于0,设为0
            currentDifficulty = 0;
        }
        else currentDifficulty -= 100;
        Idiom candidate = wordIdiomMap.get(idiomString);
        usedIdioms.add(candidate);
        ChineseCharacter cword = candidate.getCharacterList().get(candidate.getCharacterList().size()-1);//获取成语的最后一个字
        Idiom validIdiom = (findValidIdiom(cword));
        if( validIdiom != null){//如果有可接龙的成语
            usedIdioms.add(validIdiom);
            return validIdiom;
        }else{
            //System.out.println("提示:该成语无法接龙,奖励1000分\n您成功击杀一条龙.接下来随机给出一个龙头,然后游戏继续\n如果您是输入hint后收到了这条消息,说明给您提供的提示将会逼龙自杀.您可以输入提示成语作为龙头继续接龙");
            killNum++;
            if(pickRandomIdiom() != null){
                Idiom randomIdiom = pickRandomIdiom();
                randomIdiom.setState(1);
                return randomIdiom;
           }else{
                //输出语句"成语表中没有成语了,游戏结束"
                //System.out.println("成语都被你接完了,你是开挂了吗?");
                return null;
           }
        }
    }
    @Override
    public double getCurrentScore() {
        //系统的主要方法之一,通过已经使用的成语的可接龙数量计算当前分数,
        float score = 0;
        for (Idiom idiom : usedIdioms) {
            if (allowFurtherSearch) {
                score += 60/(idiom.getAllowHomophoneNum()+1);//对于每个成语,成语的可接龙数量越多,分数越低,+1是为了防止分母为0
            } else {
                score += 60/(idiom.getNotAllowHomophoneNum()+1);
            }
        }
        score += Math.pow(1.5, usedIdioms.size()+1) + 1000 * killNum;//再加上接龙长度得分和击杀得分
        return score;//分数越高,2的幂次方越大,且分数的增加速度越快
    }
    @Override 
    public Idiom getHint(String computerIdiom){//调用这个方法,一定要保证调用的成语是存在的.
        Idiom helpIdiom = wordIdiomMap.get(computerIdiom);
        if(previousHintIdiom != null ){
            //如果存在上一次提示的成语,将其加到已经使用的成语列表中,保证不重复提示
            usedIdioms.add(previousHintIdiom);
        }
        if (availableHintCount <= 0){
            //如果可用提示次数为0,返回一个404成语
            Idiom errorIdiom = new Idiom(404);
            return errorIdiom;
        }
        //系统的主要方法之一,根据用户输入的成语,返回一个提示成语
        //从成语表中找到该字符对应的成语
        //从成语对象中得到该成语的最后一个字
        ChineseCharacter cword = helpIdiom.getCharacterList().get(helpIdiom.getCharacterList().size()-1);
        //从成语表中找到可接龙的成语
        Idiom hintIdiom = findValidIdiom(cword);
        if (hintIdiom == null){
            //如果没有提示的成语,在不考虑三万个成语接龙完毕的情况下,
            //说明该成语电脑无法接龙.返回一个403成语表示Forbidden
            Idiom errorIdiom = new Idiom(403);
            return errorIdiom;
        }
        availableHintCount--;//可用提示次数减一
        hintIdiom.setState(availableHintCount);//设置提示成语的状态,向系统传递提示次数
        previousHintIdiom = hintIdiom;//将提示成语设置为上一次提示的成语
        return hintIdiom;
    }
    
    public boolean isValidIdiom(final String idiom) {
        //系统的主要方法之一,判断用户输入的成语是否合法.(是否在成语表中)
        if (idiom == null || idiom.isEmpty()) {
            return false;
        }
        //判断
        Idiom candidate = wordIdiomMap.get(idiom);
        if (candidate == null) {
            return false;
        }
        return true;
    }
    public boolean idiomNotUsed(final String idiom){
        //系统的主要方法之一,判断用户输入的成语是否已经使用过.不在成语表中的成语也算使用过,返回false,是包括了isValidIdiom的.
        if(!isValidIdiom(idiom)){
            return false;
        }
        Idiom candidate = wordIdiomMap.get(idiom);
        if(usedIdioms.contains(candidate)){
            return false;
        }
        return true;
    }
    public void updateUsedIdioms(final Idiom idiom) {
        //系统的主要方法之一,更新已经使用的成语列表.这个重载未被使用过.
        usedIdioms.add(idiom);
    }
    public void updateUsedIdioms(final String idiom) {
        //系统的主要方法之一,更新已经使用的成语列表
        //从成语表中找到这个成语,然后更新已经使用的成语列表
        Idiom candidate = wordIdiomMap.get(idiom);
        usedIdioms.add(candidate);
    }

    public void setCurrentDifficulty(int currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }
    public int getCurrentDifficulty() {
        return currentDifficulty;
    }

    private Idiom findValidIdiom(final ChineseCharacter cword) {//传入的word是成语的最后一个字
        /*
         * 裁判系统内部使用的最主要方法.
         * 根据成语的最后一个字,在成语表中查找所有可接龙的成语.如果允许使用同音字,则在上一个函数的基础上再加上同音字可接龙的成语
         * 该成语是与当前难度差距最小的可接龙成语,且不能被使用过,不能接不了龙.如果没有这样的成语,则返回null
         * return:返回一个Idiom对象,该成语是与当前难度差距最小的可接龙成语
         */
        if (cword == null) {
            Idiom errorIdiom = new Idiom(404);
            return errorIdiom;
        }
        Character word = cword.getZi();
        Set<Idiom> validIdioms = new HashSet<>();
        validIdioms = findValidIdioms(word);
        if(allowFurtherSearch){
            String pinyin = cword.getPinyin();
            Set<Character> ziSet = pinyinZiListMap.get(pinyin);
            if (ziSet != null) {
                for (Character zi : ziSet) {
                    if (zi != word) {//相当于忽略掉了成语表中以这个字开头的成语,因为这些成语已经在上一个函数中被找到了(注意:这里有优化空间.同音寻找一定能找到同字的成语,完全不需要再找一遍)
                        if (validIdioms == null) {//如果validIdioms为空,则创建一个新的Set<Idiom>对象,避免空指针异常
                            validIdioms = new HashSet<>();
                        }
                        validIdioms.addAll(findValidIdioms(zi));//将同音字可接龙的成语加入到validIdioms中
                    }
                }
            }
            //去掉已经使用过的成语
            validIdioms.removeAll(usedIdioms);
            if (!challengeMode) {
                //去掉validIdioms中不常用的成语isCommonlyUsed为false的成语
                Iterator<Idiom> iterator = validIdioms.iterator();
                while (iterator.hasNext()) {
                    Idiom idiom = iterator.next();
                    if (!idiom.getCommonlyUsed()) {
                        iterator.remove();
                    }
                }
            }
            //去掉Idiom.allowHomophoneNum为0的成语,保证提供给用户的成语都是可以接龙的
            Iterator<Idiom> iterator = validIdioms.iterator();
            while (iterator.hasNext()) {
                Idiom idiom = iterator.next();
                if (idiom.getAllowHomophoneNum() == 0) {
                    iterator.remove();
                }
            }
            //找到Idiom.allowHomophoneNum和currentDifficulty差距最小的成语
            Idiom bestIdiom = null;
            int minDiff = Integer.MAX_VALUE;
            for (Idiom idiom : validIdioms) {
                int diff = Math.abs(idiom.getAllowHomophoneNum() - currentDifficulty);
                if (diff < minDiff) {
                    minDiff = diff;
                    bestIdiom = idiom;
                }
            }
            return bestIdiom;
        }else{
            if (validIdioms != null) {
                validIdioms.removeAll(usedIdioms);
            }
            else{
                return null;
            }
            //去掉Idiom.notAllowHomophoneNum为0的成语,保证提供给用户的成语都是可以接龙的
            Iterator<Idiom> iterator = validIdioms.iterator();
            while (iterator.hasNext()) {
                Idiom idiom = iterator.next();
                if (idiom.getNotAllowHomophoneNum() == 0) {
                    iterator.remove();
                }
            }
            //找到Idiom.notAllowHomophoneNum和currentDifficulty差距最小的成语
            Idiom bestIdiom = null;
            int minDiff = Integer.MAX_VALUE;
            for (Idiom idiom : validIdioms) {
                int diff = Math.abs(idiom.getNotAllowHomophoneNum() - currentDifficulty);
                if (diff < minDiff) {
                    minDiff = diff;
                    bestIdiom = idiom;
                }
            }
            return bestIdiom;
        }
    }
    private Set<Idiom> findValidIdioms(final Character word) {//传入的word是成语的最后一个字
        /*
         * 裁判系统内部使用的主要方法.
         * 根据成语的最后一个字,在成语表中查找所有同字可接龙的成语
         * return:返回一个Set<Idiom>类型的集合,集合中的成语都是可以接龙的成语
         */
        if (word == null) {
            return null;
        }
        List<String> idioms = initialWordListMap.get(word);//根据最后一个字找到成语表中以这个字开头的成语(string)
        if (idioms == null || idioms.isEmpty()) {
            return null;
        }
        Set<Idiom> validIdioms = new HashSet<>();
        for (String idiom : idioms) {
            Idiom candidate = wordIdiomMap.get(idiom);//根据成语找到成语表中的成语(Idiom类型)
            if (  candidate != null && !usedIdioms.contains(candidate) ) {//如果成语表中有这个成语且Set<Idiom> usedIdioms中不存在这个成语
                validIdioms.add(candidate);
            }
        }
        return validIdioms;
    }    

    private Idiom pickRandomIdiom() {//随机返回一个成语对象
        List<Idiom> idioms = new ArrayList<>(wordIdiomMap.values());
        idioms.removeAll(usedIdioms);
        if (idioms.isEmpty()) {
            return null;
        }
        int idx = (int) (idioms.size() * Math.random());
        return idioms.get(idx);
    }
}
