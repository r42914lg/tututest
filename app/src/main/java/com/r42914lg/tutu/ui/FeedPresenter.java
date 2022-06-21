package com.r42914lg.tutu.ui;

import static com.r42914lg.tutu.Constants.LOG;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.r42914lg.tutu.R;
import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.model.TuTuViewModel;

import java.util.List;

/**
 * Presenter for First Fragment
 */
public class FeedPresenter {
    public static final String TAG = "LG> DetailsPresenter";

    private final TuTuViewModel tuTuViewModel;
    private final Fragment fragment;

    public FeedPresenter(TuTuViewModel tuTuViewModel, Fragment fragment) {
        this.tuTuViewModel = tuTuViewModel;
        this.fragment = fragment;
    }

    public void initFeedView(IFeedView iFeedView) {

        // we need to make FAB visible when First Fragment starts
        tuTuViewModel.showFab(true);

        // subscribe for observables

        tuTuViewModel.getCategoriesLiveData().observe(fragment, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                iFeedView.showFeed(categoryList);
                if (LOG) {
                    Log.d(TAG, ".initFeedView: clueDetails received, setting adapter...");
                }
            }
        });

        tuTuViewModel.getNavigateToDetailedViewLiveData().observe(fragment, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    tuTuViewModel.getNavigateToDetailedViewLiveData().setValue(false);

                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });

        // call ViewModel method to initiate feed update with requestNewItems parameter == false
        // since we do not need new items - we just need to ensure list is loaded
        tuTuViewModel.requestFeedUpdate(false);

        if (LOG) {
            Log.d(TAG, ".initFeedView");
        }
    }

    /**
     * Called from Fragment when user selects a raw from a list - sets category ID on ViewModel,
     * requests category details
     * @param positionInAdapter - position in adapter
     */
    public void onDetailsRequested(int positionInAdapter) {
        tuTuViewModel.setCategoryId(positionInAdapter);
        tuTuViewModel.requestDetailsUpdate();

        if (LOG) {
            Log.d(TAG, ".onDetailsRequested");
        }
    }
}
