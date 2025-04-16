package Brick;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class BrickBreakerPanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks;
    private int currentLevel = 1;
    private final int maxLevel = 3;

    private Timer timer;
    private int delay = 8;

    private int playerX;
    private int ballPosX;
    private int ballPosY;
    private int ballDirX;
    private int ballDirY;

    private BrickMap map;
    private Random random;

    public BrickBreakerPanel() {
        random = new Random();
        setupLevel();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    private void setupLevel() {
        int rows = 2 + currentLevel;
        int cols = 7;
        map = new BrickMap(rows, cols, currentLevel);
        totalBricks = rows * cols;
        resetGame();
    }

    private void resetGame() {
        play = true;
        score = 0;
        playerX = 250 + random.nextInt(200);
        ballPosX = 100 + random.nextInt(400);
        ballPosY = 300 + random.nextInt(100);
        ballDirX = random.nextBoolean() ? -1 : 1;
        ballDirY = -2 - (int)((currentLevel > 1 ? (currentLevel - 1) * 0.5 : 0)); // ball gets faster per level
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        map.draw((Graphics2D) g);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 550, 30);
        g.drawString("Level: " + currentLevel, 20, 30);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.CYAN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            if (currentLevel < maxLevel) {
                g.drawString("Level Cleared! Press Enter", 170, 300);
            } else {
                g.drawString("🎉 You Won All Levels!", 180, 300);
            }
        }

        if (ballPosY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(map.map[i][j] - 1, i, j);
                            if (map.map[i][j] == 0) totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
                                ballDirX = -ballDirX;
                            else
                                ballDirY = -ballDirY;

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballDirX;
            ballPosY += ballDirY;

            if (ballPosX < 0 || ballPosX > 670) ballDirX = -ballDirX;
            if (ballPosY < 0) ballDirY = -ballDirY;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            playerX = Math.min(playerX + (20 + (currentLevel - 1) * 5), 600);

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            playerX = Math.max(playerX - (20 + (currentLevel - 1) * 5), 10);

        if (e.getKeyCode() == KeyEvent.VK_ENTER && !play) {
            if (totalBricks <= 0 && currentLevel < maxLevel) {
                currentLevel++;
            }
            setupLevel();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
