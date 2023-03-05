package org.example;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.morphology.russian.RussianAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LemmatizationCore {

    public void analyze() throws IOException {
        RussianAnalyzer analyzer = new RussianAnalyzer();
        HashMap<String, ArrayList<String>> tokenMap = new HashMap<>();
        File file = new File("tokens.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            TokenStream stream = analyzer.tokenStream("", line);
            stream.reset();
            while (stream.incrementToken()) {
                String lemma = stream.getAttribute(CharTermAttribute.class).toString();
                if (tokenMap.containsKey(lemma)) {
                    ArrayList<String> words = tokenMap.get(lemma);
                    words.add(line);
                    tokenMap.put(lemma, words);
                } else {
                    ArrayList<String> words = new ArrayList<>();
                    words.add(line);
                    tokenMap.put(lemma, words);
                }
            }
            stream.end();
            stream.close();
        }
        writeLemmasMap(tokenMap);
    }

    private void writeLemmasMap(HashMap<String, ArrayList<String>> tokenMap) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("lemmas.txt", true));
        for(Map.Entry<String, ArrayList<String>> entry : tokenMap.entrySet()) {
            String lemma = entry.getKey();
            ArrayList<String> keys = entry.getValue();
            writer.append(lemma).append(" ");
            keys.forEach(
            (word) ->
                {
                    try {
                        writer.append(word).append(" ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
            writer.append("\n");
        }
        writer.close();
    }
}
