package org.example;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException, BadLocationException {
//        Crawler.shared.read(new URL("http://nubo.ru/pavel_egorov/new10.html"));
//        Crawler.shared.writeToIndex();

        HTMLParser parser = new HTMLParser();
        parser.readAllTextElements();
    }
}