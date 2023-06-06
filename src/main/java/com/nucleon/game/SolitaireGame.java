package com.nucleon.game;

import com.nucleon.entity.Idiom;

public class SolitaireGame extends GameFlow {

    private RefereeSystem RefereeSystem;
    
    public SolitaireGame() {
        //向控制台输出欢迎信息
        System.out.println("欢迎来到成语接龙游戏！");
    }
    protected SolitaireGame(Boolean challengeMode,Boolean allowFurtherSearch) {
        setRefereeSystem(challengeMode,allowFurtherSearch);
    }
    @Override
    public Idiom doOneRound(String userIdiom) {
        System.out.println("Solitaire中被调用");
        return RefereeSystem.doOneRound(userIdiom);
    }
    @Override
    public double getCurrentScore() {
        return RefereeSystem.getCurrentScore();
    }
    @Override
    public Idiom getHint(String computerIdiom) {
        return RefereeSystem.getHint(computerIdiom);
    }
    //选择难度
    private void setRefereeSystem(Boolean challengeMode,Boolean allowFurtherSearch) {
        if (!challengeMode) {
            if (!allowFurtherSearch) {
                RefereeSystem = new RefereeSystem(false,false);
            }else {
                RefereeSystem = new RefereeSystem(true,false);
            }
        }else {
            if (!allowFurtherSearch) {
                RefereeSystem = new RefereeSystem(false,true);
            } else {
                RefereeSystem = new RefereeSystem(true,true);
            }
        }
    }

}
