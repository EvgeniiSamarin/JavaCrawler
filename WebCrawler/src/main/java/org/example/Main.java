package org.example;

import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        Crawler.shared.read(new URL("http://nubo.ru/pavel_egorov/new10.html"));
        Crawler.shared.writeToIndex();
    }
}