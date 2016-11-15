package priorityQueues;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private char[] blocks;
    private int n;

    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new char[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i * n + j] = (char) blocks[i][j];
            }
        }
    }

    private Board(char[] blocks) {
        this.n = (int) Math.sqrt(blocks.length);
        this.blocks = new char[n * n];
        System.arraycopy(blocks, 0, this.blocks, 0, blocks.length);
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int numberOutOfPlace = 0;
        int properValue = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == 0) {
                properValue++;
                continue;
            }
            if (blocks[i] != ++properValue) numberOutOfPlace++;
        }
        return numberOutOfPlace;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                manhattan += findDistance(blocks[i * n + j], i, j, n);
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        char [] copy = new char[blocks.length];
        System.arraycopy(blocks, 0, copy, 0, blocks.length);
        if (copy[0] != 0 && copy[1] != 0) exchange(copy, 0, 0, 0, 1);
        else  exchange(copy, 1, 0, 1, 1);
        return new Board(copy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(blocks, board.blocks);
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbours = new ArrayList<>(4);
        int currentI = 0;
        int currentJ = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i * n + j] == 0) {
                    currentI = i;
                    currentJ = j;
                    break;
                }

            }
        }
        if (currentI != 0) {
            neighbours.add(neighbourBoard(currentI, currentJ, currentI - 1, currentJ));
        }
        if (currentI != n - 1) {
            neighbours.add(neighbourBoard(currentI, currentJ, currentI + 1, currentJ));
        }
        if (currentJ != 0) {
            neighbours.add(neighbourBoard(currentI, currentJ, currentI, currentJ - 1));
        }
        if (currentJ != n - 1) {
            neighbours.add(neighbourBoard(currentI, currentJ, currentI, currentJ + 1));
        }
        return neighbours;
    }

    private Board neighbourBoard(int currentI, int currentJ,
                                 int neighbourI, int neighbourJ) {
        char [] copy = new char[blocks.length];
        System.arraycopy(blocks, 0, copy, 0, blocks.length);
        exchange(copy, currentI, currentJ, neighbourI, neighbourJ);
        return new Board(copy);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(dimension() + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(String.format("%2d ", (int) blocks[i * n + j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private int findDistance(int value, int i, int j, int size) {
        if (value == 0) return 0;
        int properI = (value - 1) / size;
        int properJ = value - properI * size - 1;
        return  Math.abs(i - properI) + Math.abs(j - properJ);
    }

    private void exchange(char[] exchangeBlocks, int i1, int j1, int i2, int j2) {
        char tmp = exchangeBlocks[i1 * n + j1];
        exchangeBlocks[i1 * n + j1] = exchangeBlocks[i2 * n + j2];
        exchangeBlocks[i2 * n + j2] = tmp;
    }

    public static void main(String[] args) {

    }

}