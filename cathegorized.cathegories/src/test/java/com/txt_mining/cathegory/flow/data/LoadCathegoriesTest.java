package com.txt_mining.cathegory.flow.data;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadCathegoriesTest {

    @Test
    void load() {
        String json="[{\"name\":\"cathegory\", \"keywords\":[\"word1\",\"word2\"]}]";
        InputStream is= new ByteArrayInputStream(json.getBytes());
        loadString(is,1);
        is= this.getClass().getClassLoader().getResourceAsStream("cathegories.json");
        loadString(is, 2);

    }

    private void loadString(InputStream is,int entries) {

        LoadCategories loader= new LoadCategories();
        try {
            loader.load(is);
            List map= loader.cathegories();
            assertEquals(entries,map.size());
            assertTrue(map.get(0)instanceof Category);


        }
        catch(Exception e){
            fail("json problem");

        }
    }
}