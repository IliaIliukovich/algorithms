package unionFind;

import edu.princeton.cs.algs4.StdOut;

public class Percolation {

    private int[] grid;
    private int[] sz;
    private final int n;

    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();
        this.n = n;
        grid = new int[this.n * this.n + 2];
        for (int i = 1; i < grid.length - 1; i++) {
            grid[i] = -1;
        }
        grid[grid.length - 1] = grid.length - 1;
        sz = new int[this.n * this.n + 2];
        for (int i = 0; i < sz.length; i++) {
            sz[i] = 1;
        }
    }

    public void open(int row, int col) {
        if (n != 1) {
            if (row < 1 || row > n || col < 1 || col > n)
                throw new IndexOutOfBoundsException();
            if (!isOpen(row, col)) {
                if (row == 1) {
                    grid[indexOf(row, col)] = 0;
                    sz[0]++;
                } else if (row == n) {
                    grid[indexOf(row, col)] = grid.length - 1;
                    sz[grid.length - 1]++;
                } else {
                    grid[indexOf(row, col)] = indexOf(row, col);
                }
                uniteWithNeighbours(row, col);
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

    private void checkAndUniteWithNeighbour(int row, int col, int neighbourRow, int neighbourCol) {
        if (isOpen(neighbourRow, neighbourCol)) {
            int ownRoot = root(indexOf(row, col));
            int neighbourRoot = root(indexOf(neighbourRow, neighbourCol));
            if (ownRoot == neighbourRoot) return;

            if (sz[ownRoot] < sz[neighbourRoot]) {
                grid[ownRoot] = neighbourRoot;
                sz[neighbourRoot] += sz[ownRoot];
            } else {
                grid[neighbourRoot] = ownRoot;
                sz[ownRoot] += sz[neighbourRoot];
            }

//            if (ownRoot > neighbourRoot) {
//                grid[ownRoot] = root(indexOf(neighbourRow, neighbourCol));
//            } else {
//                grid[neighbourRoot] = root(indexOf(row, col));
//            }
        }
    }

    private int root(int index) {
        while (index != grid[index]) {
//            grid[index] = grid[grid[index]];
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
        return (isOpen(row, col)) && root(indexOf(row, col)) == root(0);
    }

    public boolean percolates() {
        print();
        return root(0) == root(grid.length - 1);
    }

    private void print() {
        StdOut.println(grid[0]);
        for (int i = 1 ; i <= n ; i++) {
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