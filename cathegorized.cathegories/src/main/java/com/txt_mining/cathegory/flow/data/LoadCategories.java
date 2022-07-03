package com.txt_mining.cathegory.flow.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt_mining.cathegory.flow.data.api.ILoadCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * implements category loading from a stream .
 *
 * Implements the {@link ILoadCategories}
 *
 */
public class LoadCategories implements ILoadCategories {
    static Logger log= LoggerFactory.getLogger(LoadCategories.class);
    List<Category> cathegories;

    @Override
    public void load(InputStream is) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            cathegories=mapper.readerFor(List.class).readValue(is);

            for(int i=0;i<cathegories.size();i++){
                cathegories.set(i,mapper.convertValue(cathegories.get(i), Category.class));
            }
        } catch (Exception e) {
            log.error("cannot read json {}",e.getMessage());
        }

    }
    @Override
    public List<Category> cathegories() {
        return cathegories;
    }
}
