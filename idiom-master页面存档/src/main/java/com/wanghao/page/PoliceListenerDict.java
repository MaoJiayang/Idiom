package com.wanghao.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoliceListenerDict implements ActionListener {

    DictMenu1 viewD;

    public void setViewDictMenu1(DictMenu1 viewD) {
        this.viewD = viewD;
    }

    public void actionPerformed(ActionEvent e) {
        String str = viewD.wanjiashuru.getText();
        viewD.textShow.append(str);
    }
}
