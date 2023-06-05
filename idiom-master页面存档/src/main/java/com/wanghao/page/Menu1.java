package com.wanghao.page;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class Menu1 extends JFrame {
    public Menu1() {
        // 设置窗口大小和位置
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // 创建面板和其上的控件
        JPanel panel = new JPanel(new BorderLayout());
        ImageIcon icon = new ImageIcon("image/带背景首页图.jpg");
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
        JButton gameChoiceButton = new JButton("游戏模式");
        JButton gradesButton = new JButton("历史记录");

        // 设置控件的样式
        background.setHorizontalAlignment(JLabel.CENTER);
        background.setVerticalAlignment(JLabel.CENTER);
        gameChoiceButton.setPreferredSize(new Dimension(120, 40));
        gradesButton.setPreferredSize(new Dimension(120, 40));
        gameChoiceButton.setFont(new Font("宋体", Font.PLAIN, 18));
        gradesButton.setFont(new Font("宋体", Font.PLAIN, 18));

        // 添加监听器
        gameChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameChoiceMenu1();  // 打开 GameChoiceMenu2 窗口
                Menu1.this.dispose();  // 关闭当前窗口
            }
        });

        gradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradeMenu1();  // 打开 GradeMenu1 窗口
                Menu1.this.dispose();  // 关闭当前窗口
            }
        });

        // 将控件添加到面板上
        panel.add(background, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(gameChoiceButton);
        buttonPanel.add(gradesButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 将面板添加到窗口上
        this.setContentPane(panel);

        this.setTitle("成语接龙游戏");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}