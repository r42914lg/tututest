package com.r42914lg.tutu.ui;

import com.r42914lg.tutu.domain.Category;
import java.util.List;

/**
 * This interface defines what First Fragment should render on UI
 */
public interface IFeedView {
    void showFeed(List<Category> clues);
}
