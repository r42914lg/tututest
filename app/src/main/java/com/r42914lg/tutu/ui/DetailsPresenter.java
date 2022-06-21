package com.r42914lg.tutu.ui;

import static com.r42914lg.tutu.Constants.LOG;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.r42914lg.tutu.domain.CategoryDetailed;
import com.r42914lg.tutu.model.TuTuViewModel;

/**
 * Presenter for Second Fragment
 */
public class DetailsPresenter {
    public static final String TAG = "LG> DetailsPresenter";

    private final TuTuViewModel tuTuViewModel;
    private final Fragment fragment;

    public DetailsPresenter(TuTuViewModel tuTuViewModel, Fragment fragment) {
        this.tuTuViewModel = tuTuViewModel;
        this.fragment = fragment;
    }

    public void initDetailsView(IDetailsView iDetailsView) {
        // we need to hide FAB when Second Fragment starts
        tuTuViewModel.showFab(false);

        // subscribe to observables
        tuTuViewModel.getCategoryDetailedLiveData().observe(fragment, new Observer<CategoryDetailed>() {
            @Override
            public void onChanged(CategoryDetailed categoryDetailed) {
                iDetailsView.showDetails(categoryDetailed);
            }
        });

        if (LOG) {
            Log.d(TAG, ".initMainActivity");
        }
    }
}
