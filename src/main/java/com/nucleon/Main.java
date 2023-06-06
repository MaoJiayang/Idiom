package com.nucleon;
import java.util.Scanner;

import com.nucleon.entity.Idiom;
import com.nucleon.game.GameFlow;
import com.nucleon.game.GamingLogic;

/**
 * Hello world!
 *
 */
public class Main 
{   
    public static void main( String[] args )
    {   
        /* 
        GameFlow gameFlow = new GameFlow();
        gameFlow.startGame();
        */
        Scanner sc = new Scanner(System.in);
        GamingLogic game = new GameFlow();
        game.loadGame();
        int gameType = 2;//1是字典模式.别的是接龙模式
        Boolean challengeMode = true;
        Boolean allowFurtherSearch = true;
        //提示用户选择游戏模式
        game.gameSetting(gameType,challengeMode,allowFurtherSearch);
        //提示用户从键盘输入成语
        System.out.print("exit退出,skip跳过本轮\n输入龙头>>>");
        String userIdiom = sc.nextLine();
            if ("exit".equals(userIdiom)) {
                System.out.println("游戏结束");
                sc.close();
                return;
            }
        Idiom computerIdiom = game.doOneRound(userIdiom);
        if(computerIdiom.getState() == 404) {
            System.out.println("该成语不存在，请重新输入！");
        }
        else {
            System.out.println("电脑接龙成语："+computerIdiom.getWord());
            System.out.println("当前分数："+game.getCurrentScore());
        }
        while (true) {
            //提示用户从键盘输入成语
            System.out.print("输入接龙成语>>>");
            userIdiom = sc.nextLine();
            if ("exit".equals(userIdiom)) {
                System.out.println("游戏结束");
                break;
            }
            if("skip".equals(userIdiom)) {
                System.out.println("跳过本轮.");
                Idiom hintIdiom = game.getHint(computerIdiom.getWord());
                System.out.println("电脑帮助成语："+hintIdiom.getWord());
                if(hintIdiom.getState() == 404) {
                    System.out.println("提示次数已经用完了！");
                    continue;
                }
                continue;
            }
            computerIdiom = game.doOneRound(userIdiom);
            if(computerIdiom.getState() == 404) {
                System.out.println("该成语不存在，请重新输入！");
                continue;
            }
            if (computerIdiom.getState() == 1) {
                System.out.println("电脑成语："+computerIdiom.getWord());
                System.out.println("你击杀了这条龙！奖励1000分.上面的成语是新的龙头.");
                continue;
            }
            System.out.println("电脑接龙成语："+computerIdiom.getWord());
            System.out.println("当前分数："+game.getCurrentScore());
        }
        sc.close();
    }
}
