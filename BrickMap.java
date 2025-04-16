package Brick;

import java.awt.*;

public class BrickMap {
    public int[][] map;
    public int brickWidth;
    public int brickHeight;

    public BrickMap(int row, int col, int level) {
        map = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = Math.min(level, 3); // Level determines strength: 1 to 3
            }
        }
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    switch (map[i][j]) {
                        case 3: g.setColor(Color.RED); break;
                        case 2: g.setColor(Color.ORANGE); break;
                        case 1: g.setColor(Color.WHITE); break;
                    }
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
