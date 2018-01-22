package directedgraphs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class WordNet {

    private List<Synset> synsetList = new LinkedList<>();
    private List<Hypernym> hypernymList = new LinkedList<>();

    public WordNet(String synsets, String hypernyms) {

        try {
            Files.lines(new File(synsets).toPath()).forEach(line -> {
                synsetList.add(new Synset(line));
            });

            Files.lines(new File(hypernyms).toPath()).forEach(line -> {
                hypernymList.add(new Hypernym(line));
            });
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }


    }

    public Iterable<String> nouns() {
        return synsetList.stream().flatMap(e -> e.sinonims.stream()).collect(Collectors.toCollection(TreeSet::new));
    }

    public boolean isNoun(String word) {
        Set set = (Set) nouns();
        return set.contains(word);
    }

    public int distance(String nounA, String nounB) {

        Synset synsetA = synsetList.stream().filter(e -> e.sinonims.contains(nounA)).findAny().get();

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    public static void main(String[] args) {

        WordNet wordNet = new WordNet("D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\synsets100-subgraph.txt",
                "D:\\ideaProjects\\algorithms3\\src\\test-data\\directedgraphs\\wordnet\\hypernyms100-subgraph.txt");

        wordNet.synsetList.forEach(System.out::println);
        wordNet.hypernymList.forEach(System.out::println);
        wordNet.nouns().forEach(System.out::println);
        System.out.println(wordNet.isNoun("qwe"));
        System.out.println(wordNet.isNoun("zymase"));

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
    }

    private class Hypernym {

        int id;
        List<Integer> hypernyms = new LinkedList<>();

        Hypernym(String line) {
            String[] split = line.split(",");
            id = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) {
                hypernyms.add(Integer.parseInt(split[i]));
            }
        }

        @Override
        public String toString() {
            return id + " " + hypernyms;
        }
    }
}