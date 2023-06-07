package com.wanghao.page;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SolChooseMenu extends JFrame{

    public JButton funmode,challengemode;
    public Container con ;
    public JLabel funlabel,challengelabel;
    private BackgroundPanel panel;
    public SolChooseMenu() {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        init();
        setContentPane(panel);
        setTitle("模式选择");
        setVisible(true);
        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
        JLabel label = (JLabel) getLayeredPane().getComponentsInLayer(Integer.MIN_VALUE)[0];

    }

    void init(){
    	panel = new BackgroundPanel(new ImageIcon("image/背景图备选1.jpg"));
    	panel.setBgImage(new ImageIcon("image/背景图备选1.jpg"));
        //获取背景图片路径
        ImageIcon  bg = new ImageIcon("image/背景图备选1.jpg");
        //建立图像文本
        JLabel  label  =  new JLabel(bg);
        //设置图片的大小
        label.setSize(700,500);
        //把背景图片添加到最底层
        getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        //panel =  (JPanel)getContentPane();
        //将内容窗口设为透明
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 100));
        Font font=new Font("楷体",Font.PLAIN,18);
        funlabel= new JLabel("娱乐模式：同音不同字");
        funlabel.setFont(font);
        challengelabel= new JLabel("挑战模式：同音同字");
        challengelabel.setFont(font);
        //创建按钮
        funmode = new JButton("娱乐模式");
        funmode.setFont(font);
        challengemode= new JButton("挑战模式");
        challengemode.setFont(font);
        challengemode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SolMenu1();  // 打开 GameChoiceMenu2 窗口
                SolChooseMenu.this.dispose();  // 关闭当前窗口
            }
        });
        //将控件添加到面板中

        panel.add(funmode);
        panel.add(funlabel);
        panel.add(challengemode);
        panel.add(challengelabel);
        panel.setOpaque(false);
        getContentPane().add(panel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (e.getComponent() instanceof BackgroundPanel) {
                    BackgroundPanel bgPanel = (BackgroundPanel) e.getComponent();
                    Image img = new ImageIcon(bgPanel.getBgImage()).getImage()
                            .getScaledInstance(getContentPane().getWidth(), getContentPane().getHeight(), Image.SCALE_SMOOTH);
                    bgPanel.setBgImage(new ImageIcon(img));
                }
            }
        });


    }
}
