package com.nucleon.game;
import com.nucleon.entity.Idiom;

public interface GamingLogic {

    void loadGame();//加载游戏数据,数据预处理
    void gameSetting(int gameType,Boolean challengeMode,Boolean allowFurtherSearch);//游戏设置,主要是初始化裁判系统
    /*
     * 说明:游戏初始难度根据不同模式写在裁判系统里了.裁判系统具体有什么功能,可以看裁判系统的定义
     * gameType:1是字典模式.别的是接龙模式.字典模式后两个参数随便填,参数无效
     * challengeMode:是否挑战模式
     * allowFurtherSearch:是否允许同音接龙
     */
    //上面两个函数必须在每个游戏开始前调用一次,否则会出现循环调用导致堆栈溢出

    Idiom doOneRound(String userIdiom);//每一轮游戏的逻辑,用户输入一个成语,返回一个合理的成语.
    /*
     * 说明:null表示没有符合规则的成语,意味着成语被接完了.一般不会出现,需要在游戏流程中进行额外处理
     * 对成语对象使用getState()方法可以获取成语的状态
     * 返回成语的state为404表示该成语不合法,需要在游戏流程中进行额外提示
     * 返回成语的state为403,只会在这个成语是提示成语的时候出现,表示这个提示如果给了用户将会让电脑无法接龙.
     * 返回成语的state为1表示该成语是一个新的龙头,需要在游戏流程中进行额外提示
     */
    Idiom getHint(String computerIdiom);//获取提示
    /*
     * 说明:激活提示会消耗提示次数.提示次数写在了裁判系统里面
     * 如果返回一个404成语,说明提示次数已经用完了
     * 剩余提示次数写在成语状态中
     */
    double getCurrentScore();//获取当前分数

}
