package directedgraphs;

import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private Digraph digraph;

    public SAP(Digraph G) {
        this.digraph = G;
    }

    public int length(int v, int w) {
        if (v < 0 || v > digraph.V() - 1 || w < 0 || w > digraph.V() - 1) throw new IllegalArgumentException();
        int[] distanceAB = new int[digraph.V()];
        int minDistIndex = findMinDistIndex(v, w, new boolean[digraph.V()], new boolean[digraph.V()], new int[digraph.V()], distanceAB);
        return minDistIndex == -1 ? minDistIndex : distanceAB[minDistIndex];
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v > digraph.V() - 1 || w < 0 || w > digraph.V() - 1) throw new IllegalArgumentException();
        return findMinDistIndex(v, w, new boolean[digraph.V()], new boolean[digraph.V()], new int[digraph.V()], new int[digraph.V()]);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }

    private int findMinDistIndex(int v, int w, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceAB) {
        depthFirstSearchMark(v, isMarkedA, distanceA, 0);
        depthFirstSearchFindMinDistance(w, isMarkedA, isMarkedB, distanceA, distanceAB, 0);

        int minDistIndex = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (isMarkedA[i] && isMarkedB[i]) {
                minDistIndex = (minDistIndex != -1 && distanceAB[i] > distanceAB[minDistIndex]) ? minDistIndex : i;
            }
        }
        return minDistIndex;
    }

    private void depthFirstSearchMark(Integer id, boolean[] isMarkedA, int[] distance, int curDistance) {
        isMarkedA[id] = true;
        distance[id] = curDistance;
        digraph.adj(id).forEach(e -> {
            depthFirstSearchMark(e, isMarkedA, distance, curDistance + 1);
        });
    }

    private void depthFirstSearchFindMinDistance(Integer id, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceAB, int curDistance) {
        isMarkedB[id] = true;
        if (isMarkedA[id]) {
            distanceAB[id] = distanceA[id] + curDistance;
        }
        digraph.adj(id).forEach(e -> {
            depthFirstSearchFindMinDistance(e, isMarkedA, isMarkedB, distanceA, distanceAB, curDistance + 1);
        });
    }
}