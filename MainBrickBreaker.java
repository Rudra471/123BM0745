package Brick;

import javax.swing.*;

public class MainBrickBreaker {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        BrickBreakerPanel gamePanel = new BrickBreakerPanel();

        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker Game");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePanel);
        obj.setVisible(true);
    }
}