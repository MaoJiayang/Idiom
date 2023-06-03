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

    private void printIdiom(final Idiom idiom) {
        System.out.println("【"+idiom.getWord()+"】");
        System.out.println("拼音：" + idiom.getPinyin());
        System.out.println("释义：" + idiom.getExplanation());
        System.out.println("出处：" + idiom.getDerivation());
        System.out.println("举例：" + idiom.getExample());
    }
}    

