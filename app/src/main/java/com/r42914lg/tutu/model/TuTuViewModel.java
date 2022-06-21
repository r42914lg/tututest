package com.r42914lg.tutu.model;

import static com.r42914lg.tutu.Constants.CATEGORIES_TO_RETURN;
import static com.r42914lg.tutu.Constants.LOG;
import static com.r42914lg.tutu.Constants.OFFSET_MAX;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.r42914lg.tutu.R;
import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.domain.CategoryDetailed;
import com.r42914lg.tutu.service.RestClient;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel implementation
 */
public class TuTuViewModel extends AndroidViewModel {
    public static final String TAG = "LG> TuTuViewModel";

    // list of categories - to be rendered on 1st fragment
    private final MutableLiveData<List<Category>> categoryListLiveData;
    // category details - to be rendered on 2nd fragment
    private final MutableLiveData<CategoryDetailed> categoryDetailedLiveData;
    // flag corresponding to progress bar visibility
    private final MutableLiveData<Boolean> progressBarFlagLiveData;
    // toolbar text
    private final MutableLiveData<String> liveToolBarTitle;
    // flag corresponding to FAB visibility
    private final MutableLiveData<Boolean> showFabLiveData;
    // text to be shown as Toast message
    private final MutableLiveData<String> toastLiveData;
    // title & text for dialog to be rendered on UI side
    private final MutableLiveData<TerminateDialogText> terminateDialogEventMutableLiveData;
    // flag indicating navigation to 2st fragment is needed
    private final MutableLiveData<Boolean> navigateToDetailedViewLiveData;

    // reference to LocalStorageHelper (implements save/read from file logic)
    private final LocalStorageHelper localStorageHelper;

    // current category chosen on 1st fragment
    private int categoryId;
    // flag corresponding to internet connectivity status
    private boolean isOnline;
    // true if feed (list) is populated from local storage (not from API)
    private boolean offlineCategoriesInFeed;

    public TuTuViewModel(@NonNull Application application) {
        super(application);

        // initialize LiveData members
        categoryListLiveData = new MutableLiveData<>();
        progressBarFlagLiveData = new MutableLiveData<>();
        liveToolBarTitle = new MutableLiveData<>();
        showFabLiveData = new MutableLiveData<>();
        toastLiveData = new MutableLiveData<>();
        terminateDialogEventMutableLiveData = new MutableLiveData<>();
        categoryDetailedLiveData = new MutableLiveData<>();
        navigateToDetailedViewLiveData = new MutableLiveData<>();

        // on start we set tool bar title text showing that we are offline
        // when later we get callback from NetworkTracker we will change it
        // to actual - online or offline
        liveToolBarTitle.setValue(MessageFormat.format(
                "{0}{1}{2}",
                getApplication().getApplicationContext().getString(R.string.app_name),
                " - ",
                getApplication().getApplicationContext().getString(R.string.status_offline))
        );

        // create listener to capture callbacks from LocalStorageHelper
        ILocalStorageListener listener = new ILocalStorageListener() {
            @Override
            public void onCategoriesLoadFromLocalSuccess(List<Category> categoryList) {
                offlineCategoriesInFeed = true;
                pushCategoriesToUI(categoryList);
            }

            @Override
            public void onCategoriesLoadFromLocalFailure() {
                progressBarFlagLiveData.setValue(false);
                toastLiveData.setValue(application
                        .getApplicationContext()
                        .getString(R.string.local_storage_error));
            }

            @Override
            public void onDetailsLoadFromLocalSuccess(CategoryDetailed categoryDetailed) {
                categoryDetailedLiveData.setValue(categoryDetailed);
                navigateToDetailedViewLiveData.setValue(true);
                progressBarFlagLiveData.setValue(false);
            }

            @Override
            public void onDetailsLoadFromLocalFailure() {
                progressBarFlagLiveData.setValue(false);
                toastLiveData.setValue(application
                        .getApplicationContext()
                        .getString(R.string.local_storage_error));
            }
        };

        localStorageHelper  =  new LocalStorageHelper(
                application,
                listener,
                Executors.newFixedThreadPool(2),
                HandlerCompat.createAsync(Looper.getMainLooper())
            );
    }

    /**
     * Called when user selects row in RecyclerView, looks for Category in list of Categories with
     * index == position in adapter
     * @param positionInAdapter - position in adapter
     */
    public void setCategoryId(int positionInAdapter) {
        this.categoryId = categoryListLiveData.getValue().get(positionInAdapter).getId();
    }

    /**
     * Requests feed update via API if online or reads feed from local storage if offline. If feed
     * has been already populated with categories and 'requestNewItems' parameter not TRUE
     * the method does nothing
     * @param requestNewItems - TRUE if new API request should be issued whether or not
     *                        categories have been already populated
     */
    public void requestFeedUpdate(boolean requestNewItems) {
        if (!requestNewItems && categoryListLiveData.getValue() != null && categoryListLiveData.getValue().size() > 0) {
            return;
        }

        if (isOnline) {
            requestFeedUpdateViaAPI(false);
        } else {
            localStorageHelper.readCategoriesFromFile();
        }
    }

    /**
     * Requests category details update via API if online or reads feed from local storage if offline.
     */
    public void requestDetailsUpdate() {
        if (isOnline) {
            requestDetailsViaAPI();
        } else {
            localStorageHelper.readDetailsFromFile(categoryId);
        }
    }

    /**
     * Conducts API call to update feed. Makes progress bar on UI visible before API call starts,
     * hides progress bar on UI once API call finishes. On success notifies UI via live data,
     * saves categories to local storage. Toasts UI on failure with error message.
     * @param backgroundThread - TRUE if invoked from background thread (e.g. from NetworkTracker)
     */
    private void requestFeedUpdateViaAPI(boolean backgroundThread) {
        if (backgroundThread) {
            progressBarFlagLiveData.postValue(true);
        } else {
            progressBarFlagLiveData.setValue(true);
        }

        RestClient.getInstance().getApi().getCategories(CATEGORIES_TO_RETURN, new Random().nextInt(OFFSET_MAX)).
                enqueue(new Callback<List<Category>>() {

            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                offlineCategoriesInFeed = false;
                List<Category> categoryList = response.body();
                pushCategoriesToUI(categoryList);
                localStorageHelper.saveCategoriesToFile(categoryList);
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressBarFlagLiveData.setValue(false);
                toastLiveData.setValue(getApplication()
                        .getApplicationContext()
                        .getString(R.string.retrofit_error));
            }
        });
    }

    /**
     * Conducts API call to update category details. Makes progress bar on UI visible before
     * API call starts, hides progress bar on UI once API call finishes.
     * On success notifies UI via live data, saves categories to local storage.
     * Toasts UI on failure with error message.
     */
    private void requestDetailsViaAPI() {
        progressBarFlagLiveData.setValue(true);

        if (LOG) {
            Log.d(TAG, ".requestDetailsViaAPI: requesting details for Category ID = " + categoryId);
        }

        RestClient.getInstance().getApi().getDetailedCategory(categoryId).enqueue(new Callback<CategoryDetailed>() {
            @Override
            public void onResponse(Call<CategoryDetailed> call, Response<CategoryDetailed> response) {
                CategoryDetailed categoryDetailed = response.body();
                categoryDetailedLiveData.setValue(categoryDetailed);
                navigateToDetailedViewLiveData.setValue(true);
                progressBarFlagLiveData.setValue(false);
                localStorageHelper.saveDetailsToFile(categoryDetailed);
            }

            @Override
            public void onFailure(Call<CategoryDetailed> call, Throwable t) {
                t.printStackTrace();

                progressBarFlagLiveData.setValue(false);

                if (isOnline) {
                    toastLiveData.setValue(getApplication()
                            .getApplicationContext()
                            .getString(R.string.retrofit_error));
                } else {
                    localStorageHelper.readCategoriesFromFile();
                }
            }
        });
    }

    /**
     * Sets live data value to trigger UI notification, hides progress bar
     * @param categoryDetailedList - list of Categories
     */
    private void pushCategoriesToUI(List<Category> categoryDetailedList) {
        categoryListLiveData.setValue(categoryDetailedList);
        progressBarFlagLiveData.setValue(false);
    }

    /**
     * PermissionHelper calls that method if any permissions are missing - triggers termination
     * dialog on UI
     */
    public void onPermissionsCheckFailed() {
        terminateDialogEventMutableLiveData.setValue(
                new TerminateDialogText(
                        getApplication().getString(R.string.dialog_terminate_no_permissions_title),
                        getApplication().getString(R.string.dialog_terminate_no_permissions_text)
                )
        );
    }

    /**
     * NetworkTracker calls that method when connection status changes
     * @param isOnline - TRUE if there is internet connection
     */
    public void setNetworkStatus(boolean isOnline) {
        this.isOnline = isOnline;

        // update tool bar text with connection status
        liveToolBarTitle.postValue(MessageFormat.format(
                "{0}{1}{2}",
                getApplication().getApplicationContext().getString(R.string.app_name),
                " - ",
                getApplication().getApplicationContext().getString(isOnline ? R.string.status_online : R.string.status_offline))
        );

        if (isOnline && offlineCategoriesInFeed) {
            requestFeedUpdateViaAPI(true);
        }
    }

    /**
     * Called from UI, notifies UI to show/hide FAB
     * @param flag - TRUE if FAB should be visible
     */
    public void showFab(boolean flag) {
        showFabLiveData.setValue(flag);
    }

    // getters for LiveData
    public MutableLiveData<List<Category>> getCategoriesLiveData() { return categoryListLiveData; }
    public MutableLiveData<Boolean> getProgressBarFlagLiveData() { return progressBarFlagLiveData; }
    public MutableLiveData<String> getLiveToolBarTitle() { return liveToolBarTitle; }
    public MutableLiveData<Boolean> getShowFabLiveData() { return showFabLiveData; }
    public MutableLiveData<String> getToastLiveData() { return toastLiveData; }
    public MutableLiveData<TerminateDialogText> getTerminateDialogEventMutableLiveData() { return terminateDialogEventMutableLiveData; }
    public MutableLiveData<CategoryDetailed> getCategoryDetailedLiveData() { return categoryDetailedLiveData; }
    public MutableLiveData<Boolean> getNavigateToDetailedViewLiveData() { return navigateToDetailedViewLiveData; }
}
