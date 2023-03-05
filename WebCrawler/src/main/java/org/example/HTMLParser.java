package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    // MARK: - Constants
    private static final String ruNameRegEx = "[А-ЯЁ][-А-яЁё]+";
    private static final Pattern pattern = Pattern.compile(ruNameRegEx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);

    // MARK: - Func
    public void readAllTextElements() throws IOException {
        Set<String> wordsSet = new HashSet<>();
        for (int i = 0; i < 100; ++i) {
            File file = new File(i + ".html");
            Document doc = Jsoup.parse(file, "UTF-8");
            String text = doc.text();
            StringTokenizer tokenizer = new StringTokenizer(text);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                word = word.replace("\"", "");
                word = word.replace(")", "");
                word = word.replace("(", "");
                word = word.replace("'", "");

                if (isRuWord(word)) {
                    wordsSet.add(word);
                }
            }
        }
        writeTokens(wordsSet);
    }

    private void writeTokens(Set<String> wordsSet) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("tokens.txt", true));
        for (String word: wordsSet) {
            writer.append(word).append("\n");
        }
        writer.close();
    }

    private boolean isRuWord(String testString) {
        Matcher m = pattern.matcher(testString);
        return m.matches();
    }
}
