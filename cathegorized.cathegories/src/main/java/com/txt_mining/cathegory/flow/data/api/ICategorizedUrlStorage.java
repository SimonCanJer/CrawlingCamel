package com.txt_mining.cathegory.flow.data.api;

import com.txt_mining.cathegory.flow.data.UrlCategory;

import java.util.Map;

/**
 * interface to keep urls matching by cathegory
 */
public interface ICategorizedUrlStorage {
    /**
     * add url matching result
     * @param cat - instance of  url to catego match
     */
    void add(UrlCategory cat);


    /**
     *  patch: matching url (with percentage by category)
     *
     * @param category         category name
     * @return                 url to percentage of leyword match
     */
    Map<String,Integer> match(String category);

}
