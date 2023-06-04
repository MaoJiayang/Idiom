package com.nucleon.game;

import com.nucleon.entity.Idiom;

public class SolitaireGame extends GameFlow {

    private RefereeSystem RefereeSystem;

    protected SolitaireGame() {
        System.out.println("欢迎来到成语接龙! exit退出");
    }

    //选择难度
    private void selectRefereeSystem() {
        System.out.println("请选择难度： 1-娱乐模式 2-挑战模式");
        System.out.print(">>>");
        String d = sc.nextLine();
        if ("1".equals(d)) {
            System.out.println("【娱乐模式】：\n请选择接龙模式：1-普通模式 2-同音模式");
            if ("1".equals(sc.nextLine())) {
                RefereeSystem = new RefereeSystem(false,false);
            }else {
                RefereeSystem = new RefereeSystem(true,false);
            }
        }else {
            System.out.println("【挑战模式】：\n请选择接龙模式：1-普通模式 2-同音模式");
            if ("1".equals(sc.nextLine())) {
                RefereeSystem = new RefereeSystem(false,true);
            } else {
                RefereeSystem = new RefereeSystem(true,true);
            }
        }
    }

    private void doMainFlow() {
        /*
         * 1.用户输入一个成语作为开头
         * 2.判断该成语是否存在
         * 3.判断该成语是否已经被使用过
         * 4.判断该成语是否符合该难度下的接龙规则
         * 5.将用户输入成语加入已使用成语列表
         * 6.查找符合难度要求的,未被使用过的成语作为电脑回合的成语,并加入已使用成语列表.
         *       如果该成语可以接龙的个数为0,则杀龙,输出提示信息,并随机挑一个没被使用过的新词重新开始接龙.
         * 7.接受用户的接龙成语,如果用户选择需要提示,则给用户提供一个可接龙数多的成语作为提示.重复2-7步骤,直到用户输入exit
         */
        //TODO:完成游戏主要流程
        //1.用户输入一个成语作为开头
        System.out.println("请输入一个成语作为开头：");
        String idiom = sc.nextLine();
        //2.判断该成语是否合法
        if (!RefereeSystem.isValidIdiom(idiom)) {
            System.out.println("您输入的成语被使用过或不存在，请重新输入：");
            idiom = sc.nextLine();
        }
        //3.将用户输入成语加入已使用成语列表
        RefereeSystem.updateUsedIdioms(idiom);
        //4.查找符合难度要求的,未被使用过的成语作为电脑回合的成语,展示该成语,并加入已使用成语列表.
        Idiom computerIdiom = RefereeSystem.doOneRound(idiom);
        System.out.println("电脑回合：" + computerIdiom);
        //5.接受用户的接龙成语,如果用户选择需要提示,则给用户提供一个可接龙的成语作为提示.重复2-7步骤,直到用户输入exit
        while (true) {
            System.out.println("请输入一个成语：");
            String userAnswer = sc.nextLine();
            if ("exit".equals(userAnswer)) {
                System.out.println("游戏结束");
                break;
            }
            //6.判断该成语是否合法
            if (!RefereeSystem.isValidIdiom(userAnswer)) {
                System.out.println("您输入的成语被使用过或不存在，请重新输入：");
                userAnswer = sc.nextLine();
            }
            //8.将用户输入成语加入已使用成语列表
            RefereeSystem.updateUsedIdioms(userAnswer);
            //9.查找符合难度要求的,未被使用过的成语作为电脑回合的成语,展示该成语,并加入已使用成语列表.
            computerIdiom = RefereeSystem.doOneRound(userAnswer);
            System.out.println("电脑回合：" + computerIdiom);
        }
        
    }



    @Override
    protected void mainFlow() {
        selectRefereeSystem();

        doMainFlow();
    }  
}
