package com.nucleon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.nucleon.entity.Idiom;
import com.nucleon.game.GameFlow;
import com.nucleon.game.GamingLogic;

public class IdiomGame extends JFrame implements GamingLogic {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;
    private JButton hintButton;
    private JLabel scoreLabel;
    private int score;
    private Idiom currentIdiom;
    private GamingLogic game;

    public IdiomGame() {
        super("成语接龙");
        game = new GameFlow();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
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

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().trim();
                if (!input.isEmpty()) {
                    Idiom result = doOneRound(input);
                    if (result.getState() == 404) {
                        JOptionPane.showMessageDialog(IdiomGame.this, "成语不存在");
                    } else if (result.getState() == 1) {
                        JOptionPane.showMessageDialog(IdiomGame.this, "杀龙！");
                    } else {
                        currentIdiom = result;
                        outputArea.append(result.getWord() + "\n");
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
                    } else {
                        JOptionPane.showMessageDialog(IdiomGame.this, "提示: " + hint.getWord());
                        currentIdiom = hint;
                    }
                }
            }
        });
    }

    @Override
    public void loadGame() {
        // TODO: 实现加载游戏数据的逻辑
        game.loadGame();
    }

    @Override
    public void gameSetting(int gameType, Boolean challengeMode, Boolean allowFurtherSearch) {
        // TODO: 实现游戏设置的逻辑
        game.gameSetting(2, false, false);
    }

    @Override
    public Idiom doOneRound(String userIdiom) {
        // TODO: 实现每一轮游戏的逻辑
        return game.doOneRound(userIdiom);
    }

    @Override
    public Idiom getHint(String computerIdiom) {
        // TODO: 实现获取提示的逻辑
        return game.getHint(computerIdiom);
    }

    @Override
    public double getCurrentScore() {
        // TODO: 实现获取当前分数的逻辑
        return game.getCurrentScore();
    }

    public static void main(String[] args) {
        IdiomGame game = new IdiomGame();
        game.loadGame();
        game.gameSetting(2, false, true);
        game.setVisible(true);
    }
}