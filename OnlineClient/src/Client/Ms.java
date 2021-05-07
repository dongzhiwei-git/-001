package Client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Ms implements Serializable {
    public int[][] allChess = new int[16][16]; // ÆåÅÌÊý×é
    public boolean[][] printed = new boolean[16][16];
    public Stack<Point> pointList = new Stack<>();

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }

    public Point getPoint() {
        if (!pointList.isEmpty()) {
            return pointList.peek();
        }
        return null;
    }

    public Point removePoint() {
        if (!pointList.isEmpty()) {
            return pointList.pop();
        }
        return null;
    }

    public void addPoint(int x, int y) {
        pointList.push(new Point(x, y));
    }

    public Ms() {

    }

    public void chushihua() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                allChess[i][j] = 0;
                printed[i][j] = false;
            }
        }
    }

    @Override
    public String toString() {
        return "Myfiar [allChess=" + Arrays.toString(allChess) + "]";
    }
}

