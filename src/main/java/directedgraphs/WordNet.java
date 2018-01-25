package directedgraphs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class WordNet {

    private List<Synset> synsetList;
    private Map<String, Integer> nounsMap;
    private Map<Integer, List<Integer>> hypernymMap;

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        try {
            synsetList = new ArrayList<>();
            Files.lines(new File(synsets).toPath()).forEach(line -> {
                synsetList.add(new Synset(line));
            });

            nounsMap = new TreeMap<>();
            synsetList.forEach(synset -> {
                synset.sinonims.forEach(sinonim -> {
                    nounsMap.put(sinonim, synset.id);
                });
            });

            hypernymMap = new HashMap<>(synsetList.size());
            Files.lines(new File(hypernyms).toPath()).forEach(line -> {
                parseLine(line, hypernymMap);
            });
        } catch (IOException e) {
            throw new IllegalArgumentException();
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
        int minDistIndex = findMinDistIndex(nounA, nounB, new boolean[synsetList.size()], new boolean[synsetList.size()], new int[synsetList.size()], distanceAB);
        return minDistIndex == -1 ? minDistIndex : distanceAB[minDistIndex];
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int minDistIndex = findMinDistIndex(nounA, nounB, new boolean[synsetList.size()], new boolean[synsetList.size()], new int[synsetList.size()], new int[synsetList.size()]);
        if (minDistIndex != -1) {
            return synsetList.get(minDistIndex).print();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {

        WordNet wordNet = new WordNet("D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\synsets100-subgraph.txt",
                "D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\hypernyms100-subgraph.txt");

        System.out.println("Distance = " + wordNet.distance("zymase", "zymase"));
        System.out.println("Common ancestor = " + wordNet.sap("zymase", "zymase"));
        System.out.println("Distance = " + wordNet.distance("transaminase", "transaminase"));
        System.out.println("Common ancestor = " + wordNet.sap("transaminase", "transaminase"));

    }

    private int findMinDistIndex(String nounA, String nounB, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceAB) {
        depthFirstSearchMark(nounsMap.get(nounA), isMarkedA, distanceA, 0);
        depthFirstSearchFindMinDistance(nounsMap.get(nounB), hypernymMap, isMarkedA, isMarkedB, distanceA, distanceAB, 0);

        int minDistIndex = -1;
        for (int i = 0; i < synsetList.size(); i++) {
            if (isMarkedA[i] && isMarkedB[i]) {
                minDistIndex = (minDistIndex != -1 && distanceAB[i] > distanceAB[minDistIndex]) ? minDistIndex : i;
            }
        }
        return minDistIndex;
    }

    private void depthFirstSearchMark(Integer id, boolean[] isMarkedA, int[] distance, int curDistance) {
        isMarkedA[id] = true;
        distance[id] = curDistance;
        if (hypernymMap.containsKey(id)) {
            hypernymMap.get(id).forEach(e -> {
                depthFirstSearchMark(e, isMarkedA, distance, curDistance + 1);
            });
        }
    }

    private void depthFirstSearchFindMinDistance(Integer id, Map<Integer, List<Integer>> hypernymMap, boolean[] isMarkedA, boolean[] isMarkedB, int[] distanceA, int[] distanceAB, int curDistance) {
        isMarkedB[id] = true;
        if (isMarkedA[id]) {
            distanceAB[id] = distanceA[id] + curDistance;
        }
        if (hypernymMap.containsKey(id)) {
            hypernymMap.get(id).forEach(e -> {
                depthFirstSearchFindMinDistance(e, hypernymMap, isMarkedA, isMarkedB, distanceA, distanceAB, curDistance + 1);
            });
        }
    }

    private void parseLine(String line, Map<Integer, List<Integer>> hypernymMap) {
        String[] split = line.split(",");
        int id = Integer.parseInt(split[0]);
        List<Integer> hypernymList = new LinkedList<>();
        for (int i = 1; i < split.length; i++) {
            hypernymList.add(Integer.parseInt(split[i]));
        }
        hypernymMap.put(id, hypernymList);
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