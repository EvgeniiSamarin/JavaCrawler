package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    public static Crawler shared = new Crawler();
    String urlRegex = "href=\\\"http://(.*?)\\\"";
    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
    Matcher urlMatcher;

    InputStream inputStream = null;
    BufferedReader bufferedReader = null;
    String line;
    int count = 0;
    HashSet<String> urls = new HashSet<>();

    public void read(URL url) throws IOException {
        inputStream = url.openStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("href=") && urls.size() < 100) {
                Document doc = Jsoup.parse(line);
                String address = doc.select("a").attr("href").toString();
                if (!address.contains("new")) {
                    continue;
                }
                String finalAddress = "http://nubo.ru/pavel_egorov/".concat(address);
                System.out.println(finalAddress);
                urls.add(finalAddress);
                count++;
            }
        }
        inputStream.close();
        bufferedReader.close();
    }

    private static void downloadSite(String content, String filename) throws IOException {
        File file = new File(filename + ".html");

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)
        ) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToIndex() throws IOException {
        int i = 0;
        BufferedWriter writer = new BufferedWriter(new FileWriter("index.txt", true));
        for (String stringUrl: urls) {
            String line;
            URL url = new URL(stringUrl);
            inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "windows-1251"));
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            downloadSite(builder.toString(), String.valueOf(i));
            writer.append(i + " " + stringUrl + "\n");
            i++;
        }
        writer.close();
    }
}
