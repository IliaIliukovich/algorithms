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
    private Map<String, Integer> nounsMap;
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
                nounsMap.put(sinonim, synset.id);
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
        int[] distanceAB = new int[synsetList.size()];
        final boolean[] isMarkedA = new boolean[synsetList.size()];
        final boolean[] isMarkedB = new boolean[synsetList.size()];
        final int[] distanceA = new int[synsetList.size()];
        final int[] distanceB = new int[synsetList.size()];
        int minDistIndex = findMinDistIndex(nounA, nounB, isMarkedA, isMarkedB, distanceA, distanceB, distanceAB);

//        for (int i = 0; i < synsetList.size(); i++) {
//            System.out.println("i = " + i + " IsMarkedA = " + isMarkedA[i] + " IsMarkedB = " + isMarkedB[i] + " DistanseA = " + distanceA[i] + " DistanseB = " + distanceB[i] + " DistanseAB = " + distanceAB[i]);
//        }
        return minDistIndex == -1 ? minDistIndex : distanceAB[minDistIndex];
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int minDistIndex = findMinDistIndex(nounA, nounB, new boolean[synsetList.size()], new boolean[synsetList.size()], new int[synsetList.size()], new int[synsetList.size()] , new int[synsetList.size()]);
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

    private int findMinDistIndex(String nounA, String nounB, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceB, int[] distanceAB) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(nounsMap.get(nounA), -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (!isMarkedB[node.current]) {
                isMarkedA[node.current] = true;
                distanceA[node.current] = node.prev == -1 ? 0 : distanceA[node.prev] + 1;
                if (hypernymMap.containsKey(node.current)) {
                    for (Integer id : hypernymMap.get(node.current)) {
                        queue.add(new Node(id, node.current));
                    }
                }
            }
        }

        queue = new LinkedList<>();
        queue.add(new Node(nounsMap.get(nounB), -1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (!isMarkedB[node.current]) {
                isMarkedB[node.current] = true;
                distanceB[node.current] = node.prev == -1 ? 0 : distanceB[node.prev] + 1;
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

    private class Node {
        int current;
        int prev;

        public Node(int current, int prev) {
            this.current = current;
            this.prev = prev;
        }
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