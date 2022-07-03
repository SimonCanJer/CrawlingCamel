package com.text_mining.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Crawler {
   public  String parseThisUrl(String url){
        StringBuilder builder=new StringBuilder();

        try {
            Document doc = (Document) Jsoup.connect(url).get();
            builder.append(doc.body().text());
            Elements coll=doc.select("link[href]");

          //  doc.select("link[href]").forEach((el)->all.add(el.text()));
            doc.select("a").forEach((el)->builder.append(el.text()).append(" "));
            doc.select("p").forEach((el)->builder.append(el.text()).append(" "));;
            doc.select("b").forEach((el)->builder.append(el.text()).append(" "));;
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
