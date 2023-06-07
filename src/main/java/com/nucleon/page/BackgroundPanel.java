package com.nucleon.page;

import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel{
    private Image bgImage = null;

    public BackgroundPanel(ImageIcon imageIcon) {
        bgImage = imageIcon.getImage();
    }

    public void setBgImage(ImageIcon imageIcon) {
        bgImage = imageIcon.getImage();
    }

    public Image getBgImage() {
        return bgImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
