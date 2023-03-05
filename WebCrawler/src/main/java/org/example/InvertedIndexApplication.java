package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.morphology.russian.RussianAnalyzer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class InvertedIndexApplication {
    protected static Map<String, Map<Integer, Integer>> invertedIndex = new HashMap<String, Map<Integer, Integer>>();
    protected static Map<Integer, Integer> docWords = new HashMap<>();
    protected static int docMaxNumber = 0;
    protected static int docCount = 0;
    protected static Analyzer analyzer;

    static {
        try {
            analyzer = new RussianAnalyzer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Введите запрос:");
            String line = sc.nextLine();
            ListEvaluator evaluator = new ListEvaluator();
            System.out.println("Результаты поиска:");
            try{
                System.out.println(evaluator.evaluate(line));
            }
            catch (Exception e){
                System.err.println("Произошла ошибка при парсинге выражения.");
            }
            System.out.println("--------------");
        }
    }

    public static void init() throws IOException {
        for (int i = 0; i < 100; ++i) {
            File file = new File(i + ".html");
            indexFile(file);
        }
    }

    private static void indexFile(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        String text = doc.text();
        Integer fileName = Integer.valueOf(file.getName().replace(".html", ""));
        TokenStream stream = analyzer.tokenStream("field", text);
        stream.reset();
        int countWords = 0;
        while (stream.incrementToken()) {
            String lemma = stream.getAttribute(CharTermAttribute.class).toString();
            if(!lemma.matches("[0-9]+") && lemma.length() != 1) {
                invertedIndex.computeIfAbsent(lemma, k -> new TreeMap<>(new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                }));
                invertedIndex.get(lemma).putIfAbsent(fileName, 0);
                invertedIndex.get(lemma).put(fileName, invertedIndex.get(lemma).get(fileName) + 1);
            }
            countWords++;
        }
        if(docMaxNumber < fileName) docMaxNumber = fileName;
        docCount++;
        docWords.put(fileName, countWords);
        stream.end();
        stream.close();
    }
}
