package com.nucleon.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JOptionPane;


import com.nucleon.entity.Idiom;

public class PoliceListenerSol implements ActionListener{
    SolMenu viewS;

    public void setViewSolMenu(SolMenu solMenu) {
        this.viewS = solMenu;
    }

    public void actionPerformed(ActionEvent e) {
    	String input = viewS.wanjiashuru.getText().trim();
        
        if (!input.isEmpty()) {
            Idiom result = GameChoiceMenu1.game.doOneRound(input);
            if(result.getState()==1) {
            	JOptionPane.showMessageDialog(viewS,"你击杀了这条龙！奖励1000分.下面的成语是新的龙头."+ "\n"+result.getWord());
                viewS.textShow.setText(result.getWord() + "\n");
                SolMenu.computerResult=result.getWord();
            	}
            else if (result.getState()==404) {
            	JOptionPane.showMessageDialog(viewS,"该成语不存在,不符合接龙规则或已被使用\n\t请重新输入！");
            	}
            else{
            viewS.textShow.setText((result.getWord()) + "\n");
            SolMenu.computerResult=result.getWord();
            }
            String score = Double.toString(GameChoiceMenu1.game.getCurrentScore());
            viewS.gradeShow.setText(score);
        }
    
    }
}

