package com.txt_mining.cathegory.flow.data.implementation;

import com.txt_mining.cathegory.flow.data.api.IEstimateCategories;
import com.txt_mining.cathegory.flow.data.api.ILoadCategories;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * just  simple implementation of
 * {@link IEstimateCategories}
 */
@Slf4j
public class EstimateCategories implements IEstimateCategories {
    private ILoadCategories loader;

    @Override
    public Map<String, Integer> match(String text) {
        Map<String,Integer> matches= new HashMap<>();
        loader.cathegories().forEach((c)->{

            int match= 0;
            int all= c.getKeywords().length;
            for(String s:c.getKeywords()){
                if(text.contains(s))
                    match+=1;
            }
            if(match==0)
                return;
            match*=100;
            match/=all;
            matches.put(c.getName(),match);
        });
        return matches;
    }

    @Override
    public void set(ILoadCategories loader) {
        log.debug("setting loader");
        this.loader= loader;

    }
}
