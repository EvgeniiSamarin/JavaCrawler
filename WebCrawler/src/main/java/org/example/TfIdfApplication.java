package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TfIdfApplication {

    private static final int TF_COEF = 100;
     private static final int IDF_COEF = 1;

    public static void main(String[] args) throws IOException {
        InvertedIndexApplication.init();
        Map<String, Map<Integer, Integer>> index = InvertedIndexApplication.invertedIndex;
        BufferedWriter writer = new BufferedWriter(new FileWriter("tfidf.txt", true));
        //idf
        Map<String, Double> wordIdfMap = new HashMap<>();
        for(String word: index.keySet()){
            double idf = idf(index.get(word).size(), 100);
            wordIdfMap.put(word, idf);
        }
        for(String word: index.keySet()){
            double wordIdf = wordIdfMap.get(word);
            for(Map.Entry<Integer, Integer> entry:index.get(word).entrySet()){
                double wordTf = tf(entry.getValue(), InvertedIndexApplication.docWords.get(entry.getKey()));
                String row = word + "[" + entry.getKey() + ".html]: " +
                        "TF (" + String.format("%.3f",wordTf) + ") * IDF (" + String.format("%.3f",wordIdf) +
                        ") = TF-IDF (" + String.format("%.3f", wordTf * wordIdf) +")";
                writer.append(row);
                writer.append("\n");
                System.out.println(word + "[" + entry.getKey() + ".txt]: " +
                        "TF (" + String.format("%.3f",wordTf) + ") * IDF (" + String.format("%.3f",wordIdf) +
                        ") = TF-IDF (" + String.format("%.3f", wordTf * wordIdf) +")");
            }
        }
    }

    public static double idf(int size, int docCount){
        return Math.log((double) docCount / size);
    }

    public static double tf(int size, int wordCount){
        return (double) size / wordCount * TF_COEF;
    }

    public static void initTfIdfMap(){
        Map<String, Map<Integer, Integer>> index = InvertedIndexApplication.invertedIndex;
        Map<String, Map<Integer, Double>> tfIdfMap = InvertedIndexApplication.tfIdfMap;
        //idf
        Map<String, Double> wordIdfMap = InvertedIndexApplication.wordIdfMap;
        for(String word: index.keySet()){
            double idf = idf(index.get(word).size(), InvertedIndexApplication.docCount);
            wordIdfMap.put(word, idf);
        }
        for(String word: index.keySet()){
            double wordIdf = wordIdfMap.get(word);
            for(Map.Entry<Integer, Integer> entry:index.get(word).entrySet()){
                double wordTf = tf(entry.getValue(), InvertedIndexApplication.docWords.get(entry.getKey()));
                if(tfIdfMap.get(word) == null) tfIdfMap.put(word, new TreeMap<>(Comparator.comparingInt(o -> o)));
                tfIdfMap.get(word).put(entry.getKey(), wordTf * wordIdf);
            }
        }
    }

}
