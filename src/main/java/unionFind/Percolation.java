package unionFind;

import edu.princeton.cs.algs4.StdOut;

public class Percolation {

    private int[] grid;
    private final int n;

    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();
        this.n = n;
        grid = new int[this.n * this.n + 2];
        for (int i = 1; i < grid.length - 1; i++) {
            grid[i] = -1;
        }
        grid[grid.length - 1] = grid.length - 1;
    }

    public void open(int row, int col) {
        if (n != 1) {
            if (row < 1 || row > n || col < 1 || col > n)
                throw new IndexOutOfBoundsException();
            if (!isOpen(row, col)) {
                if (row == 1) {
                    grid[indexOf(row, col)] = 0;
                    uniteWithNeighbours(row, col);
                } else if (row == n) {
                    grid[indexOf(row, col)] = grid.length - 1;
                    bottomUniteWithNeighbours(row, col);
                } else {
                    grid[indexOf(row, col)] = indexOf(row, col);
                    uniteWithNeighbours(row, col);
                }
            }
        } else {
            grid[1] = 0;
            grid[2] = 0;
        }
    }

    private void uniteWithNeighbours(int row, int col) {
        if (row > 1)
            checkAndUniteWithNeighbour(row, col, row - 1, col);
        if (row < n)
            checkAndUniteWithNeighbour(row, col, row + 1, col);
        if (col > 1)
            checkAndUniteWithNeighbour(row, col, row, col - 1);
        if (col < n)
            checkAndUniteWithNeighbour(row, col, row, col + 1);
    }

    private void bottomUniteWithNeighbours(int row, int col) {
        bottomCheckAndUniteWithNeighbour(row, col, row - 1, col);
        if (col > 1)
            bottomCheckAndUniteWithBottom(row, col, row, col - 1);
        if (col < n)
            bottomCheckAndUniteWithBottom(row, col, row, col + 1);
    }

    private void checkAndUniteWithNeighbour(int row, int col, int neighbourRow, int neighbourCol) {
        if (isOpen(neighbourRow, neighbourCol)) {
            int ownRoot = subRoot(indexOf(row, col));
            int neighbourRoot = subRoot(indexOf(neighbourRow, neighbourCol));
            if (ownRoot == neighbourRoot) return;
            if (ownRoot < neighbourRoot) {
                grid[ownRoot] = neighbourRoot;
            } else {
                grid[neighbourRoot] = ownRoot;
            }
        }
    }

    private void bottomCheckAndUniteWithNeighbour(int row, int col, int neighbourRow, int neighbourCol) {
        if (isOpen(neighbourRow, neighbourCol)) {
            int ownRoot = root(indexOf(row, col));
            int neighbourRoot = root(indexOf(neighbourRow, neighbourCol));
            if (ownRoot == neighbourRoot) return;
            grid[neighbourRoot] = indexOf(row, col);
        }
    }

    private void bottomCheckAndUniteWithBottom(int row, int col, int neighbourRow, int neighbourCol) {
        if (isOpen(neighbourRow, neighbourCol)) {
                grid[indexOf(row, col)] = indexOf(neighbourRow, neighbourCol);
        }
    }

    private int root(int index) {
        while (index != grid[index]) {
//            grid[index] = grid[grid[index]];
            index = grid[index];
        }
        return index;
    }

    private int subRoot(int index) {
        while (index != grid[index] && grid[index] <= indexOf(n, n)) {
            index = grid[index];
        }
        return index;
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IndexOutOfBoundsException();
        return grid[indexOf(row, col)] != -1;
    }

    public boolean isFull(int row, int col)  {
        return (isOpen(row, col)) && subRoot(indexOf(row, col)) == subRoot(0);
    }

    public boolean percolates() {
//        print();
        return root(0) == root(grid.length - 1);
    }

    private void print() {
        StdOut.println(grid[0]);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[indexOf(i, j)] != -1)StdOut.print(String.format("%3d ", grid[indexOf(i, j)]));
                else StdOut.print("xxx ");
            }
            StdOut.print("\n");
        }
        StdOut.println(grid[grid.length - 1]);
        StdOut.println();
    }

    public static void main(String[] args) {
    }

    private int indexOf(int row, int col) {
        return (row - 1) * n + col;
    }

}