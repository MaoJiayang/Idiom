package com.nucleon.page;

import javax.swing.*;

import com.nucleon.game.GameFlow;
import com.nucleon.game.GamingLogic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class SolChooseMenu1 extends JFrame{
    public SolChooseMenu1() {
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
        // 将控件添加到面板上
        panel.add(background, BorderLayout.CENTER);
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        JButton ylButton = new JButton("娱乐模式");
        ylButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JComboBox<String> comboBox = new JComboBox<String>();
                comboBox.addItem("同字模式");
                comboBox.addItem("同音模式");
                // 添加下拉框事件监听器
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedItem = (String)comboBox.getSelectedItem();
                        if (selectedItem.equals("同字模式")) {
                            // 点击同字模式时，跳转到 SolMenuYLTZ 窗口
                            new SolMenu();
                            SolChooseMenu1.this.dispose();// 关闭当前窗口
                            GamingLogic game= new GameFlow();
                            game.loadGame();
                            GameChoiceMenu1.game.gameSetting(2,false,false);
                        } else if (selectedItem.equals("同音模式")) {
                            // 点击同音模式时，跳转到 SolMenuYLTY 窗口
                            new SolMenu();
                            SolChooseMenu1.this.dispose();  // 关闭当前窗口
                            GamingLogic game= new GameFlow();
                            game.loadGame();
                            GameChoiceMenu1.game.gameSetting(2,false,true);
                        }
                    }
                });
                // 创建一个 panel，将 comboBox 添加到其中
                JPanel panel = new JPanel();
                panel.add(comboBox);
                // 将 panel 添加到 buttonPanel 中，实现在同一窗口中体现
                buttonPanel.add(panel);
                // 重新绘制 buttonPanel，使得新添加的 panel 能够显示出来
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });
        JButton tzButton = new JButton("挑战模式");
        tzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JComboBox<String> comboBox = new JComboBox<String>();
                comboBox.addItem("同字模式");
                comboBox.addItem("同音模式");
                
                // 添加下拉框事件监听器
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedItem = (String)comboBox.getSelectedItem();
                        if (selectedItem.equals("同字模式")) {
                            // 点击同字模式时，跳转到 SolMenuTZTZ 窗口
                            new SolMenu();
                            SolChooseMenu1.this.dispose(); // 关闭当前窗口
                            GamingLogic game= new GameFlow();
                            game.loadGame();
                            GameChoiceMenu1.game.gameSetting(2,true,false);
                        } else if (selectedItem.equals("同音模式")) {
                            // 点击同音模式时，跳转到 SolMenuTZTY 窗口
                            new SolMenu();
                            SolChooseMenu1.this.dispose();  // 关闭当前窗口
                            GamingLogic game= new GameFlow();
                            game.loadGame();
                            GameChoiceMenu1.game.gameSetting(2,true,true);
                        }
                    }
                });
                // 创建一个 panel，将 comboBox 添加到其中
                JPanel panel = new JPanel();
                panel.add(comboBox);
                // 将 panel 添加到 buttonPanel 中，实现在同一窗口中体现
                buttonPanel.add(panel);
                // 重新绘制 buttonPanel，使得新添加的 panel 能够显示出来
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });
        
        JButton ReturnButton = new JButton("返回上级");
        buttonPanel.add(ylButton);
        buttonPanel.add(tzButton);
        buttonPanel.add(ReturnButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // 设置控件的样式
        background.setHorizontalAlignment(JLabel.CENTER);
        background.setVerticalAlignment(JLabel.CENTER);
        ylButton.setPreferredSize(new Dimension(120, 40));
        tzButton.setPreferredSize(new Dimension(120, 40));
        ReturnButton.setPreferredSize(new Dimension(120, 40));
        ylButton.setFont(new Font("宋体", Font.PLAIN, 18));
        tzButton.setFont(new Font("宋体", Font.PLAIN, 18));
        ReturnButton.setFont(new Font("宋体", Font.PLAIN, 18));

        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameChoiceMenu1();  // 打开 GameChoiceMenu1 窗口
                SolChooseMenu1.this.dispose();  // 关闭当前窗口
            }
        });
        

        // 将面板添加到窗口上
        this.setContentPane(panel);
        this.setTitle("游戏模式选择");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
