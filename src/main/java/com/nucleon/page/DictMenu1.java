package com.nucleon.page;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class DictMenu1 extends JFrame{
	 public JTextField wanjiashuru;
	    public JTextArea Explanation;
	    public JButton queding,tuichu;
	    public Container con ;
	    public JLabel textlabel,textlabel1;
	    PoliceListenerDict listener;
	    private BackgroundPanel panel;
	    public DictMenu1() {
	        this.setSize(800, 600);
	        this.setLocationRelativeTo(null);
	        init();
	        setContentPane(panel);
	        setTitle("成语词典");
	        setVisible(true);
	        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
	        //JLabel label = (JLabel) getLayeredPane().getComponentsInLayer(Integer.MIN_VALUE)[0];

	    }

	    void init(){
	    	panel = new BackgroundPanel(new ImageIcon("image/backGround1.jpg"));
	    	panel.setBgImage(new ImageIcon("image/backGround1.jpg"));
	        //获取背景图片路径
	        ImageIcon  bg = new ImageIcon("image/backGround1.jpg");
	        //建立图像文本
	        JLabel  label  =  new JLabel(bg);
	        //设置图片的大小
	        label.setSize(700,500);
	        //把背景图片添加到最底层
	        getLayeredPane().add(label,Integer.MIN_VALUE);
	        //panel =  (JPanel)getContentPane();
	        //将内容窗口设为透明
	        panel.setOpaque(false);
	        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 20));

	        Font font=new Font("楷体",Font.PLAIN,18);
	        textlabel= new JLabel("请输入成语：");
	        textlabel.setFont(font);
	        //创建文本框
	        wanjiashuru = new JTextField();
	        wanjiashuru.setFont(font);
	        wanjiashuru.setPreferredSize(new Dimension(100,35));//设置文本框大小
	        //创建按钮
	        queding = new JButton("确定");
	        queding.setFont(font);
	        listener = new PoliceListenerDict();
	        listener.setViewDictMenu1(this);
	        queding.addActionListener(listener);
	        Explanation = new JTextArea(4,40);
	        Explanation.setFont(font);
	        Explanation.setEditable(false);//设置为不可编辑
	        Explanation.setLineWrap(true);//文字比控件的宽度还长时会自动换行
	        Explanation.setWrapStyleWord(true);//在单词边界换行

	        textlabel1= new JLabel("成语详情：");
	        textlabel1.setFont(font);

	        tuichu= new JButton("退出");
	        tuichu.setFont(font);
	        tuichu.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                new GameChoiceMenu1();  // 打开 GameChoiceMenu1 窗口
	                DictMenu1.this.dispose();  // 关闭当前窗口
	            }
	        });
	        //将控件添加到面板中
	        panel.add(textlabel);
	        panel.add(wanjiashuru);
	        panel.add(queding);
	        panel.add(textlabel1);
	        panel.add(Explanation);
	        
	        panel.add(tuichu);
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
