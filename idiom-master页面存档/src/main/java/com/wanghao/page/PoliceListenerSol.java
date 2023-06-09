package com.wanghao.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoliceListenerSol implements ActionListener {
    SolMenu1 viewS;

    public void setViewSolMenu(SolMenu1 viewS) {
        this.viewS = viewS;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 根据需要将 viewS 或 viewD 替换为对应的视图变量
        String str = viewS.wanjiashuru.getText();
        viewS.textShow.append(str);
    }
}
