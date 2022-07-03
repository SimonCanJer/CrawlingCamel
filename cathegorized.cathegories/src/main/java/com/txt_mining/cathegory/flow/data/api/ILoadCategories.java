package com.txt_mining.cathegory.flow.data.api;

import com.txt_mining.cathegory.flow.data.Category;

import java.io.InputStream;
import java.util.List;

public interface ILoadCategories {

  ///  void load(String json);
    void load(InputStream is);
    List<Category> cathegories();
}
