package com.nucleon.page;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.*;

class RoundedButton extends JButton {
    private boolean hover; // 鼠标悬停标志
    private boolean pressed; // 按钮按下标志
    Color borderColor = new Color(200, 200, 200);//灰色
    Color endColor = new Color(211, 238, 193);//浅绿
    Color startColor = new Color(255, 255, 240);//几乎是白色
    Border buttonBorder = BorderFactory.createEmptyBorder(4, 12, 4, 12); // 边框

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(buttonBorder);

        // 添加鼠标事件监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 创建渐变颜色
        GradientPaint gradientPaint;
        if (pressed) {
            // 按钮按下时的渐变颜色
            gradientPaint = new GradientPaint(0, 0, startColor.darker(), 0, getHeight(), endColor.darker());
        } else if (hover) {
            // 鼠标悬停时的渐变颜色
            gradientPaint = new GradientPaint(0, 0, startColor.brighter(), 0, getHeight(), endColor.brighter());
        } else {
            // 普通状态下的渐变颜色
            gradientPaint = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
        }

        // 设置绘制区域
        Shape shape = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.setClip(shape);

        // 绘制渐变背景
        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 绘制边框
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(shape);

        super.paintComponent(g2);
        g2.dispose();
    }
}
