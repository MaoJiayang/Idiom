package com.nucleon.game;

import com.nucleon.entity.Idiom;

public class Dict extends GameFlow{

    protected Dict() {
        System.out.println("欢迎来到成语词典! exit退出");
    }

    @Override
    protected void mainFlow() {
        while (true) {
            System.out.print("输入一个成语>>>");
            String input = sc.nextLine();
            if ("exit".equals(input)) {
                exit();
            }

            Idiom idiom = wordIdiomMap.get(input);
            if (idiom == null) {
                System.out.println("【" + input + "】 找不到该成语!");
                continue;
            }
            printIdiom(idiom);
        }
    }
    @Override
    public Idiom doOneRound(String userIdiom) {
            Idiom idiom = wordIdiomMap.get(userIdiom);
            if (idiom == null) {
                return null;
            }
            return idiom;

    }
    public static String parseIdiomToString(Idiom idiom){
        String detailedInfo = "【"+idiom.getWord()+"】\n";
        detailedInfo += "拼音：" + idiom.getPinyin()+"\n";
        detailedInfo += "释义：" + idiom.getExplanation()+"\n";
        detailedInfo += "出处：" + idiom.getDerivation()+"\n";
        detailedInfo += "举例：" + idiom.getExample()+"\n";
        return detailedInfo;
    }

    private void printIdiom(final Idiom idiom) {
        System.out.println("【"+idiom.getWord()+"】");
        System.out.println("拼音：" + idiom.getPinyin());
        System.out.println("释义：" + idiom.getExplanation());
        System.out.println("出处：" + idiom.getDerivation());
        System.out.println("举例：" + idiom.getExample());
    }
    @Override//对于这一类,这是无用方法
    public double getCurrentScore() {
        return 0;
    }
    @Override//对于这一类,这是无用方法
    public Idiom getHint(String computerIdiom) {
        //返回一个404成语
        return new Idiom(404);
    }
}    

