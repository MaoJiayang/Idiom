package com.wanghao.page;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

public class GradeMenu1 extends JFrame{
    public JTextArea GradeShow;
    public JButton TuiChu;
    public Container Con ;
    private BackgroundPanel Panel;
    public JLabel TextLabel;
    public GradeMenu1() {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        init();
        setContentPane(Panel);
        setTitle("本局成绩");
        setVisible(true);
        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
        JLabel label = (JLabel) getLayeredPane().getComponentsInLayer(Integer.MIN_VALUE)[0];

    }
	void init() {
    	Panel = new BackgroundPanel(new ImageIcon("image/背景图备选1.jpg"));
    	Panel.setBgImage(new ImageIcon("image/背景图备选1.jpg"));
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
        Panel.setOpaque(false);
        Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 100));

        Font font=new Font("宋体",Font.PLAIN,18);
        TextLabel= new JLabel("本局得分：");
        TextLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        //创建按钮
        GradeShow = new JTextArea(4,30);
        GradeShow.setFont(font);
        TuiChu= new JButton("退出");
        TuiChu.setFont(new Font("宋体", Font.PLAIN, 18));
        TuiChu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu1();  // 打开 GameChoiceMenu1 窗口
                GradeMenu1.this.dispose();  // 关闭当前窗口
            }
        });
        //将控件添加到面板中
        Panel.add(TextLabel,BorderLayout.CENTER);
        Panel.add(GradeShow,BorderLayout.CENTER);
        Panel.add(TuiChu,BorderLayout.CENTER);
        Panel.setOpaque(false);
        getContentPane().add(Panel);

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
