package com.txt_mining.cathegory.flow.data.api;

import java.util.Map;

/**
 * This interface declares functionality to range text matching with
 * cathegories
 */
public interface IEstimateCategories {
    /**
     * estimate matching of the text with cathegories
     * @param text : text to be test for match
     * @return     : map of matching (key- cathegory, value - percent of match);
     * @return     : map of matching (key- cathegory, value - percent of match);
     */

    Map<String, Integer> match(String text);

    /**
     * setter of interface to get cathegories
     * @param loader : {@link ILoadCategories}
     */
    void set(ILoadCategories loader);
}
