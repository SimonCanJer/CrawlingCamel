package com.txt_mining.cathegory.flow.data.implementation;

import com.txt_mining.cathegory.flow.data.UrlCategory;
import com.txt_mining.cathegory.flow.data.api.ICategorizedUrlStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of cathegorized storage
 */
public class CategorizedUrlStorage implements ICategorizedUrlStorage {
    Map<String, Map<String, Integer>> mapCatheg = new ConcurrentHashMap<>();

    /**
     * adds information about cathegory match
     * @param cath- cathegory with matches per URL
     */
    @Override
    public void add(UrlCategory cath) {
       cath.getMatches().entrySet().forEach((e)->{
           if(e.getValue()>0) {
               Map<String, Integer> perUrl = mapCatheg.computeIfAbsent(e.getKey(), (k) -> new ConcurrentHashMap<>());
               perUrl.put(cath.getUrl(), e.getValue());
           }
        });

    }

    @Override
    public Map<String, Integer> match(String cathegory) {
        if(!mapCatheg.containsKey(cathegory))
            return new HashMap<>();
        return Collections.unmodifiableMap(mapCatheg.get(cathegory));
    }
}
