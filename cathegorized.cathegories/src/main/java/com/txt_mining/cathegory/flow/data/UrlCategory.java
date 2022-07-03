package com.txt_mining.cathegory.flow.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UrlCategory {
    /** any url, which examines for categories */
    String url;

    /** matches by category*/
    Map<String,Integer> matches;

}
