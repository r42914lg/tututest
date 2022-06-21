package com.r42914lg.tutu.model;

import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.domain.CategoryDetailed;

import java.util.List;

/**
 * This interface defines 4 callback methods to notify listener on result of read/save operations
 * from/to local file
 */
public interface ILocalStorageListener {
    void onCategoriesLoadFromLocalSuccess(List<Category> categoryList);
    void onCategoriesLoadFromLocalFailure();
    void onDetailsLoadFromLocalSuccess(CategoryDetailed categoryDetailed);
    void onDetailsLoadFromLocalFailure();
}
