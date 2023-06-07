package com.nucleon.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import com.nucleon.game.Dict;

public class PoliceListenerDict implements ActionListener {

	DictMenu1 viewD;

    public void setViewDictMenu1(DictMenu1 viewD) {
        this.viewD = viewD;
    }

    public void actionPerformed(ActionEvent e) {
        String str = viewD.wanjiashuru.getText();
        String output=Dict.parseIdiomToString(GameChoiceMenu1.game.doOneRound(str));
        viewD.Explanation.setText(output);
        
    }
}
