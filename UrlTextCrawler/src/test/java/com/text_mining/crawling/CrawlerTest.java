package com.text_mining.crawling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerTest {
    @Test
     public void test(){

        Crawler crawler= new Crawler();
        String res= crawler.parseThisUrl("https://www.msn.com/en-nz?refurl=%2fen-nz%2ftravel%2ftripideas%2f70-of-the-planets-most-breathtaking-sights%2fss-AAIUpDp%3ffromMaestro%3dtrue");
        assertNotEquals(0,res.length());
    }

}