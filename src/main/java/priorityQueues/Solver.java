package priorityQueues;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

public class Solver {

    private Board initial;
    private Sample current;
    private Sample currentTwin;
    private MinPQ<Sample> minPQ = new MinPQ<>();
    private MinPQ<Sample> minPQForTwin = new MinPQ<>();
    private ArrayList<Board> solution = null;
    private int moves;

    public Solver(Board initial) {
        this.initial = initial;
        this.execute();
    }

    public boolean isSolvable() { return moves() > -1; }

    public int moves() { return moves; }

    public Iterable<Board> solution() { return solution; }

    private void execute() {
        moves = 0;
        current = new Sample(null, initial, moves);
        currentTwin = new Sample(null, initial.twin(), moves);
        minPQ.insert(current);
        minPQForTwin.insert(currentTwin);
        current = findCurrent(minPQ);
        currentTwin = findCurrent(minPQForTwin);

        while ((!current.board.isGoal() && !currentTwin.board.isGoal())) {
            doStep();
        }

        if (currentTwin.board.isGoal()) moves = -1;
        else {
            ArrayList<Board> reverseSolution = new ArrayList<>(moves + 1);
            reverseSolution.add(current.board);
            Sample tmp = current;
            for (int i = 0; i < moves; i++) {
                reverseSolution.add(tmp.previous.board);
                tmp = tmp.previous;
            }
            solution = new ArrayList<>(moves + 1);
            for (int i = 0; i < moves + 1; i++) {
                solution.add(reverseSolution.get(moves - i));
            }
        }
    }

    private void doStep() {
        current = findCurrent(minPQ);
        currentTwin = findCurrent(minPQForTwin);
        moves = current.numberOfMoves;
    }

    private Sample findCurrent(MinPQ<Sample> boardMinPQ) {
        Sample sample = boardMinPQ.delMin();
        Sample previous = sample.previous;
        Iterable<Board> neighbors = sample.board.neighbors();
        for (Board neighbor : neighbors) {
            if (previous == null || !neighbor.equals(previous.board)) {
                boardMinPQ.insert(new Sample(sample, neighbor, sample.numberOfMoves + 1));
            }
        }
        return sample;
    }

    private class Sample implements Comparable<Sample> {
        private Sample previous;
        private Board board;
        private int priority;
        private int numberOfMoves;

        Sample(Sample previous, Board board, int moves) {
            this.board = board;
            this.previous = previous;
            this.priority = moves + board.manhattan();
            this.numberOfMoves = moves;
        }
        @Override
        public int compareTo(Sample that) {
            return this.priority - that.priority;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}