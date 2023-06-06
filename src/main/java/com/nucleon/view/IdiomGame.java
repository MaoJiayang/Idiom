package com.nucleon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.nucleon.entity.Idiom;
import com.nucleon.game.GameFlow;
import com.nucleon.game.GamingLogic;
import com.nucleon.game.Dict;

public class IdiomGame extends JFrame implements GamingLogic {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;
    private JButton hintButton;
    private JLabel scoreLabel;
    private int score;
    private Idiom currentIdiom;
    private GamingLogic game;
    private JCheckBox challengeModeCheckBox;
    private JCheckBox allowFurtherSearchCheckBox;
    private JComboBox<String> gameTypeComboBox;

    public IdiomGame() {
        super("成语接龙");
        game = new GameFlow();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        submitButton = new JButton("提交");
        hintButton = new JButton("提示");
        scoreLabel = new JLabel("分数: 0");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        inputPanel.add(hintButton, BorderLayout.WEST);

        JPanel settingPanel = new JPanel(new GridLayout(3, 2));
        gameTypeComboBox = new JComboBox<>(new String[]{"字典模式", "接龙模式"});
        challengeModeCheckBox = new JCheckBox("(仅对接龙有效)挑战模式开启");
        allowFurtherSearchCheckBox = new JCheckBox("(仅对接龙有效)允许同音接龙");
        settingPanel.add(new JLabel("游戏类型:"));
        settingPanel.add(gameTypeComboBox);
        settingPanel.add(new JLabel("挑战模式:"));
        settingPanel.add(challengeModeCheckBox);
        settingPanel.add(new JLabel("允许同音接龙:"));
        settingPanel.add(allowFurtherSearchCheckBox);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);
        add(settingPanel, BorderLayout.WEST);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().trim();
                if (!input.isEmpty()) {
                    Idiom result = doOneRound(input);
                    if (result.getState() == 404) {
                        JOptionPane.showMessageDialog(IdiomGame.this, "成语被使用过或不存在");
                    } else if (result.getState() == 1) {
                        JOptionPane.showMessageDialog(IdiomGame.this, "成功击杀一条龙,奖励一千分！\n接下来将会给出一个随机成语作为新龙头");
                        currentIdiom = result;
                        outputArea.append("本轮成语:" + Dict.parseIdiomToString(result) + "----------------------------------------" + "\n");
                        inputField.setText("");
                        score = (int) getCurrentScore();
                        scoreLabel.setText("分数: " + score);
                    } else {
                        currentIdiom = result;
                        outputArea.append("本轮成语:" + Dict.parseIdiomToString(result) + "----------------------------------------" +"\n");
                        inputField.setText("");
                        score = (int) getCurrentScore();
                        scoreLabel.setText("分数: " + score);
                    }
                }
            }
        });

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIdiom != null) {
                    Idiom hint = getHint(currentIdiom.getWord());
                    if (hint.getState() == 404) {
                        JOptionPane.showMessageDialog(IdiomGame.this, "提示次数已用完");
                    }else if(hint.getState() == 403){
                        JOptionPane.showMessageDialog(IdiomGame.this, "提示这个成语会让我自杀,我才不给你:)");
                    }else {
                        JOptionPane.showMessageDialog(IdiomGame.this, "提示: " + hint.getWord());
                        currentIdiom = hint;
                    }
                }
            }
        });

        // 添加复选框和勾选框的 ActionListener
        challengeModeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean challengeMode = challengeModeCheckBox.isSelected();
                boolean allowFurtherSearch = allowFurtherSearchCheckBox.isSelected();
                gameSetting(gameTypeComboBox.getSelectedIndex() + 1, challengeMode, allowFurtherSearch);
            }
        });

        allowFurtherSearchCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean challengeMode = challengeModeCheckBox.isSelected();
                boolean allowFurtherSearch = allowFurtherSearchCheckBox.isSelected();
                gameSetting(gameTypeComboBox.getSelectedIndex() + 1, challengeMode, allowFurtherSearch);
            }
        });

        gameTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean challengeMode = challengeModeCheckBox.isSelected();
                boolean allowFurtherSearch = allowFurtherSearchCheckBox.isSelected();
                int gameType = gameTypeComboBox.getSelectedIndex() + 1;
                game.gameSetting(gameType, challengeMode, allowFurtherSearch);
            }
        });
    }

    @Override
    public void loadGame() {
        game.loadGame();
    }

    @Override
    public void gameSetting(int gameType, Boolean challengeMode, Boolean allowFurtherSearch) {
        // TODO: 实现游戏设置的逻辑
        gameTypeComboBox.setSelectedIndex(gameType - 1);
        challengeModeCheckBox.setSelected(challengeMode);
        allowFurtherSearchCheckBox.setSelected(allowFurtherSearch);
        game.gameSetting(gameType, challengeMode, allowFurtherSearch);
    }

    @Override
    public Idiom doOneRound(String userIdiom) {
        return game.doOneRound(userIdiom);
    }

    @Override
    public Idiom getHint(String computerIdiom) {
        return game.getHint(computerIdiom);
    }

    @Override
    public double getCurrentScore() {
        return game.getCurrentScore();
    }

    public static void main(String[] args) {
        IdiomGame game = new IdiomGame();
        game.loadGame();
        game.gameSetting(2, false, true);
        game.setVisible(true);
    }
}