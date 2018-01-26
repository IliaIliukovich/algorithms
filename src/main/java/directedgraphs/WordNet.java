package directedgraphs;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Queue;


public class WordNet {

    private List<Synset> synsetList;
    private Map<String, List<Integer>> nounsMap;
    private Map<Integer, List<Integer>> hypernymMap;

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        synsetList = new ArrayList<>();
        In in = new In(synsets);
        String line;
        while ((line = in.readLine()) != null) {
            synsetList.add(new Synset(line));
        }

        nounsMap = new TreeMap<>();
        synsetList.forEach(synset -> {
            synset.sinonims.forEach(sinonim -> {
                List<Integer> list = (nounsMap.containsKey(sinonim)) ? nounsMap.get(sinonim) : new LinkedList();
                list.add(synset.id);
                if (!nounsMap.containsKey(sinonim)) {
                    nounsMap.put(sinonim, list);
                }
            });
        });

        hypernymMap = new HashMap<>(synsetList.size());
        in = new In(hypernyms);
        while ((line = in.readLine()) != null) {
            parseLine(line, hypernymMap);
        }

    }

    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int minDistIndex = -1;
        int minDist = -1;
        for (Integer idA : nounsMap.get(nounA)) {
            for (Integer idB : nounsMap.get(nounB)) {
                int[] distanceAB = new int[synsetList.size()];
                final boolean[] isMarkedA = new boolean[synsetList.size()];
                final boolean[] isMarkedB = new boolean[synsetList.size()];
                final int[] distanceA = new int[synsetList.size()];
                final int[] distanceB = new int[synsetList.size()];
                int curMinIndex = findMinDistIndex(idA, idB, isMarkedA, isMarkedB, distanceA, distanceB, distanceAB);
                if (minDistIndex == -1 || curMinIndex != -1 && distanceAB[curMinIndex] < minDist) {
                    minDistIndex = curMinIndex;
                    minDist = distanceAB[curMinIndex];
                }
            }
        }
        return minDist;
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        int minDistIndex = -1;
        int minDist = -1;
        for (Integer idA : nounsMap.get(nounA)) {
            for (Integer idB : nounsMap.get(nounB)) {
                int[] distanceAB = new int[synsetList.size()];
                int curMinIndex = findMinDistIndex(idA, idB, new boolean[synsetList.size()], new boolean[synsetList.size()], new int[synsetList.size()], new int[synsetList.size()], distanceAB);
                if (minDistIndex == -1 || curMinIndex != -1 && distanceAB[curMinIndex] < minDist) {
                    minDistIndex = curMinIndex;
                    minDist = distanceAB[curMinIndex];
                }
            }
        }
        if (minDistIndex != -1) {
            return synsetList.get(minDistIndex).print();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {

        WordNet wordNet = new WordNet("D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\synsets11.txt",
                "D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\hypernyms11AmbiguousAncestor.txt");

        System.out.println("Distance = " + wordNet.distance("a", "b"));
        System.out.println("Common ancestor = " + wordNet.sap("a", "b"));

    }

    private int findMinDistIndex(Integer idA, Integer idB, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceB, int[] distanceAB) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(idA, -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int curDist = node.prev == -1 ? 0 : distanceA[node.prev] + 1;
            if (!isMarkedA[node.current] || curDist < distanceA[node.current]) {
                isMarkedA[node.current] = true;
                distanceA[node.current] = curDist;
                if (hypernymMap.containsKey(node.current)) {
                    for (Integer id : hypernymMap.get(node.current)) {
                        queue.add(new Node(id, node.current));
                    }
                }
            }
        }

        queue = new LinkedList<>();
        queue.add(new Node(idB, -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int curDist = node.prev == -1 ? 0 : distanceB[node.prev] + 1;
            if (!isMarkedB[node.current] || curDist < distanceB[node.current]) {
                isMarkedB[node.current] = true;
                distanceB[node.current] = curDist;
                if (isMarkedA[node.current]) {
                    distanceAB[node.current] = distanceA[node.current] + distanceB[node.current];
                }
                if (hypernymMap.containsKey(node.current)) {
                    for (Integer id : hypernymMap.get(node.current)) {
                        queue.add(new Node(id, node.current));
                    }
                }
            }
        }

        int minDistIndex = -1;
        for (int i = 0; i < synsetList.size(); i++) {
            if (isMarkedA[i] && isMarkedB[i]) {
                minDistIndex = (minDistIndex != -1 && distanceAB[i] > distanceAB[minDistIndex]) ? minDistIndex : i;
            }
        }
        return minDistIndex;
    }

    private void parseLine(String line, Map<Integer, List<Integer>> hypernymMap) {
        String[] split = line.split(",");
        int id = Integer.parseInt(split[0]);
        List<Integer> hypernymList = hypernymMap.containsKey(id) ? hypernymMap.get(id) : new LinkedList<>();
        for (int i = 1; i < split.length; i++) {
            hypernymList.add(Integer.parseInt(split[i]));
        }
        if (!hypernymMap.containsKey(id)) {
            hypernymMap.put(id, hypernymList);
        }
    }

    private class Node {
        int current;
        int prev;

        public Node(int current, int prev) {
            this.current = current;
            this.prev = prev;
        }
    }

    private class Synset {
        int id;
        Set<String> sinonims = new TreeSet<>();
        String glos;

        Synset(String line) {
            String[] split = line.split(",");
            id = Integer.parseInt(split[0]);
            sinonims.addAll(Arrays.asList(split[1].split(" ")));
            glos = split[2];
        }

        @Override
        public String toString() {
            return id + " " + sinonims + " " + glos;
        }

        private String print() {
            if (sinonims.size() > 1) {
                StringBuilder builder = new StringBuilder();
                for (String sinonim : sinonims) {
                    builder.append(sinonim).append(" ");
                }
                return builder.toString();
            } else {
                return sinonims.iterator().next();
            }
        }
    }

}