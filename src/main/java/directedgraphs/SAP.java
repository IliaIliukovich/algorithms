package directedgraphs;

import edu.princeton.cs.algs4.Digraph;

import java.util.LinkedList;
import java.util.Queue;

public class SAP {

    private Digraph digraph;

    public SAP(Digraph G) {
        this.digraph = G;
    }

    public int length(int v, int w) {
        if (v < 0 || v > digraph.V() - 1 || w < 0 || w > digraph.V() - 1) throw new IllegalArgumentException();
        int[] distanceAB = new int[digraph.V()];
        int minDistIndex = findMinDistIndex(v, w, new boolean[digraph.V()], new boolean[digraph.V()], new int[digraph.V()], new int[digraph.V()], distanceAB);
        return minDistIndex == -1 ? minDistIndex : distanceAB[minDistIndex];
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v > digraph.V() - 1 || w < 0 || w > digraph.V() - 1) throw new IllegalArgumentException();
        return findMinDistIndex(v, w, new boolean[digraph.V()], new boolean[digraph.V()], new int[digraph.V()], new int[digraph.V()], new int[digraph.V()]);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        check(v, w);
        int minDist = -1;
        int minDistIndex = -1;
        for (Integer idA : v) {
            for (Integer idB : w) {
                int[] distanceAB = new int[digraph.V()];
                final boolean[] isMarkedA = new boolean[digraph.V()];
                final boolean[] isMarkedB = new boolean[digraph.V()];
                final int[] distanceA = new int[digraph.V()];
                final int[] distanceB = new int[digraph.V()];
                int curMinIndex = findMinDistIndex(idA, idB, isMarkedA, isMarkedB, distanceA, distanceB, distanceAB);
                if (minDistIndex == -1 || curMinIndex != -1 && distanceAB[curMinIndex] < minDist) {
                    minDistIndex = curMinIndex;
                    minDist = distanceAB[curMinIndex];
                }
            }
        }
        return minDist;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        check(v, w);
        int minDistIndex = -1;
        int minDist = -1;
        for (Integer idA : v) {
            for (Integer idB : w) {
                int[] distanceAB = new int[digraph.V()];
                int curMinIndex = findMinDistIndex(idA, idB, new boolean[digraph.V()], new boolean[digraph.V()], new int[digraph.V()], new int[digraph.V()], distanceAB);
                if (minDistIndex == -1 || curMinIndex != -1 && distanceAB[curMinIndex] < minDist) {
                    minDistIndex = curMinIndex;
                    minDist = distanceAB[curMinIndex];
                }
            }
        }
        return minDistIndex;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }

    private void check(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer idA : v) {
            for (Integer idB : w) {
                if (idA < 0 || idA > digraph.V() - 1 || idB < 0 || idB > digraph.V() - 1) throw new IllegalArgumentException();
            }
        }
    }

    private int findMinDistIndex(int v, int w, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceB, int[] distanceAB) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(v, -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int curDist = node.prev == -1 ? 0 : distanceA[node.prev] + 1;
            if (!isMarkedA[node.current] || curDist < distanceA[node.current]) {
                isMarkedA[node.current] = true;
                distanceA[node.current] = curDist;
                for (Integer id : digraph.adj(node.current)) {
                    queue.add(new Node(id, node.current));
                }
            }
        }

        queue = new LinkedList<>();
        queue.add(new Node(w, -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int curDist = node.prev == -1 ? 0 : distanceB[node.prev] + 1;
            if (!isMarkedB[node.current] || curDist < distanceB[node.current]) {
                isMarkedB[node.current] = true;
                distanceB[node.current] = curDist;
                if (isMarkedA[node.current]) {
                    distanceAB[node.current] = distanceA[node.current] + distanceB[node.current];
                }
                for (Integer id : digraph.adj(node.current)) {
                    queue.add(new Node(id, node.current));
                }
            }
        }

        int minDistIndex = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (isMarkedA[i] && isMarkedB[i]) {
                minDistIndex = (minDistIndex != -1 && distanceAB[i] > distanceAB[minDistIndex]) ? minDistIndex : i;
            }
        }
        return minDistIndex;
    }

    private class Node {
        int current;
        int prev;

        public Node(int current, int prev) {
            this.current = current;
            this.prev = prev;
        }
    }
}