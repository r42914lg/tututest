package com.r42914lg.tutu.ui;

import static com.r42914lg.tutu.Constants.LOG;

import android.util.Log;
import com.r42914lg.tutu.model.TuTuViewModel;
import com.r42914lg.tutu.utils.NetworkTracker;
import com.r42914lg.tutu.utils.PermissionsHelper;

/**
 * Presenter for Main Activity
 */
public class CorePresenter {
    public static final String TAG = "LG> CorePresenter";

    private final TuTuViewModel tuTuViewModel;
    private final MainActivity mainActivity;

    public CorePresenter(TuTuViewModel tuTuViewModel, MainActivity mainActivity) {
        this.tuTuViewModel = tuTuViewModel;
        this.mainActivity = mainActivity;

        // Check permissions, start to monitor network state
        new PermissionsHelper(mainActivity, tuTuViewModel);
        new NetworkTracker(mainActivity, tuTuViewModel);
    }

    public void initCoreView(ICoreView iCoreView) {

        // subscribe to observables
        tuTuViewModel.getLiveToolBarTitle().observe(mainActivity, iCoreView::showNetworkStatus);
        tuTuViewModel.getShowFabLiveData().observe(mainActivity, iCoreView::showFabIcon);
        tuTuViewModel.getToastLiveData().observe(mainActivity, iCoreView::showToast);
        tuTuViewModel.getTerminateDialogEventMutableLiveData().observe(mainActivity, iCoreView::showTerminateDialog);

        tuTuViewModel.getProgressBarFlagLiveData().observe(mainActivity, aBoolean -> {
            if (aBoolean) {
                iCoreView.startProgressOverlay();
            } else {
                iCoreView.stopProgressOverlay();
            }
        });

        if (LOG) {
            Log.d(TAG, ".initCoreView");
        }
    }
}
