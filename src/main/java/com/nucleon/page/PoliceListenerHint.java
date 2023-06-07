package com.nucleon.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nucleon.entity.Idiom;

public class PoliceListenerHint implements ActionListener{
	SolMenu viewS;

    public void setViewSolMenu(SolMenu solMenu) {
        this.viewS = solMenu;
    }

    public void actionPerformed(ActionEvent e) {
    	String input = viewS.wanjiashuru.getText().trim();
        if (!input.isEmpty() && SolMenu.computerResult!=null ) {
        	Idiom Hint = GameChoiceMenu1.game.getHint(SolMenu.computerResult);
        	//System.out.print("提示"+Hint.getWord()+"\n");
        	if(Hint.getState()==404) 
        	{JOptionPane.showMessageDialog(viewS,"提示次数用完了！");}
        	else if(Hint.getState()==403) 
        	{JOptionPane.showMessageDialog(viewS,"这个提示会让我的龙自杀,才不给你提示:)！");}
        	else
            {JOptionPane.showMessageDialog(viewS,"提示成语:"+Hint.getWord()+"\n"+"剩余提示次数："+Hint.getState());}
            
        }
    
    }
}