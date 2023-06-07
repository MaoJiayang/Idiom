package com.nucleon.page;

import javax.swing.*;

import com.nucleon.game.GameFlow;
import com.nucleon.game.GamingLogic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class GameChoiceMenu1 extends JFrame {
	public static GamingLogic game;	
    public GameChoiceMenu1() {
    	game=new GameFlow();
    	game.loadGame();
        // 设置窗口大小和位置
    	
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // 创建面板和其上的控件
        JPanel panel = new JPanel(new BorderLayout());
        final ImageIcon icon = new ImageIcon("image/backGround3.jpg");
        JLabel background = new JLabel(icon) {
            @Override
            public void paintComponent(Graphics g) {
                // 计算缩放比例
                double sx = (double) getWidth() / icon.getIconWidth();
                double sy = (double) getHeight() / icon.getIconHeight();
                // 等比例缩放并绘制背景图
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform transform = AffineTransform.getScaleInstance(sx, sy);
                g2d.drawImage(icon.getImage(), transform, this);
            }
        };
        JButton DictionaryButton = new JButton("成语词典");
        JButton SolitaireButton = new JButton("成语接龙");
        JButton ReturnButton = new JButton("返回首页");

        // 设置控件的样式
        background.setHorizontalAlignment(JLabel.CENTER);
        background.setVerticalAlignment(JLabel.CENTER);
        DictionaryButton.setPreferredSize(new Dimension(120, 40));
        SolitaireButton.setPreferredSize(new Dimension(120, 40));
        ReturnButton.setPreferredSize(new Dimension(120, 40));
        DictionaryButton.setFont(new Font("宋体", Font.PLAIN, 18));
        SolitaireButton.setFont(new Font("宋体", Font.PLAIN, 18));
        ReturnButton.setFont(new Font("宋体", Font.PLAIN, 18));

        // 添加监听器
        DictionaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DictMenu1();  // 打开 DictMenu1 窗口
                GameChoiceMenu1.this.dispose();  // 关闭当前窗口
                game.gameSetting(1,true,true);
            }
        });

        SolitaireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SolChooseMenu1();  // 打开 SolChooseMenu1 窗口
                GameChoiceMenu1.this.dispose();  // 关闭当前窗口
            }
        });

        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu1();  // 打开 Menu1 窗口
                GameChoiceMenu1.this.dispose();  // 关闭当前窗口
            }
        });
        
        // 将控件添加到面板上
        panel.add(background, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(DictionaryButton);
        buttonPanel.add(SolitaireButton);
        buttonPanel.add(ReturnButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 将面板添加到窗口上
        this.setContentPane(panel);
        this.setTitle("游戏模式选择");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
