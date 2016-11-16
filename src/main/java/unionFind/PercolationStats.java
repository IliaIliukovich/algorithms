package unionFind;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private final int n;
    private final int nSquare;
    private final int trials;
    private double[] x;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.nSquare = n * n;
        this.x = new double[trials];
        executeAlgorithm();
    }

    private void executeAlgorithm() {
        int randomIndex;
        int randomI;
        int randomJ;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                randomIndex = StdRandom.uniform(nSquare);
                randomI = randomIndex / n + 1;
                randomJ = randomIndex % n + 1;
                percolation.open(randomI, randomJ);
            }
            x[i] = countOpenProportion(percolation);
        }
    }

    private double countOpenProportion(Percolation percolation) {
        int sum = 0;
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (percolation.isOpen(i, j)) sum++;
            }
        }
        return (double) sum / (double) nSquare;
    }

    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < trials; i++) {
            sum += x[i];
        }
        return sum / (double) trials;
    }

    public double stddev() {
        double mean = mean();
        double squareSum = 0;
        for (int i = 0; i < trials; i++) {
            squareSum += (mean - x[i]) * (mean - x[i]);
        }
//        if (trials > 1)
            return  Math.sqrt(squareSum / (double) (trials - 1));
//        else return Double.POSITIVE_INFINITY;
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {

        PercolationStats stats = new PercolationStats(10000, 30);
        StdOut.println("Mean = " + stats.mean());
        StdOut.println("StdDev = " + stats.stddev());
        StdOut.println("Interval: " + stats.confidenceLo() + " " + stats.confidenceHi());

    }

}