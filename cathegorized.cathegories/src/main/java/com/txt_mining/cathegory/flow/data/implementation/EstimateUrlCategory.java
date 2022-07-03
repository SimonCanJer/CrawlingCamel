package com.txt_mining.cathegory.flow.data.implementation;

import com.txt_mining.cathegory.flow.data.UrlCategory;
import com.txt_mining.cathegory.flow.data.api.IEstimateCategories;
import com.txt_mining.cathegory.flow.data.api.IEstimateUrlCategory;

public class EstimateUrlCategory implements IEstimateUrlCategory {
    private IEstimateCategories match;

    @Override
    public UrlCategory mapUrl2Cathegory(String url, String text) {
        UrlCategory uc= new UrlCategory();
        uc.setUrl(url);
        uc.setMatches(match.match(text));
        return uc;
    }

    @Override
    public void set(IEstimateCategories matcher) {
        this.match=matcher;

    }
}
