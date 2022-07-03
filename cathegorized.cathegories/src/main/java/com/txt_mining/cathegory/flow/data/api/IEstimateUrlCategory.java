package com.txt_mining.cathegory.flow.data.api;

import com.txt_mining.cathegory.flow.data.UrlCategory;

/**
 * Declares functionality  to estimate cathegory of url
 * and return the result pair
 *
 */
public interface IEstimateUrlCategory {
    /**
     * relates a url to a cathegory(ries)
     * @param url              - url id
     * @param text             - text, which is behind the ur;
     * @return
     */
    UrlCategory mapUrl2Cathegory(String url, String text);
    void set(IEstimateCategories matcher);

}
