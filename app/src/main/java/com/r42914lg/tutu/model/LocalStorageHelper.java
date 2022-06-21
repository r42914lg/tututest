package com.r42914lg.tutu.model;

import static com.r42914lg.tutu.Constants.LOG;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.r42914lg.tutu.Constants;
import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.domain.CategoryDetailed;
import com.r42914lg.tutu.service.RestClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Implements saving/reading of Categories & Details to local storage as json files
 */
public class LocalStorageHelper {
    public static final String TAG = "LG> LocalStorageHelper";

    private final ILocalStorageListener dataLoaderListener;
    private final Application application;
    private final Executor executor;
    private final Handler resultHandler;

    public LocalStorageHelper(Application application, ILocalStorageListener dataLoaderListener,  Executor executor, Handler resultHandler) {
        this.application = application;
        this.dataLoaderListener = dataLoaderListener;
        this.executor = executor;
        this.resultHandler = resultHandler;

        if (LOG) {
            Log.d(TAG, ": instance created");
        }
    }

    public void readCategoriesFromFile() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Category> categoryList = new ArrayList<>(Constants.CATEGORIES_TO_RETURN);
                String jsonString = loadJSONFromAsset(Constants.LOCAL_FILE_NAME_FEED);
                boolean failureFlag = false;

                if (jsonString == null) {
                    failureFlag = true;
                }

                if (!failureFlag) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category c = RestClient.gson().fromJson(jsonArray.getJSONObject(i).toString(), Category.class);
                            categoryList.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failureFlag = true;
                    }
                }

                boolean finalFailureFlag = failureFlag;

                resultHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (LOG) {
                            Log.d(TAG, ".readCategoriesFromFile: finished "
                                    + (finalFailureFlag ? "with error":"successfully")
                                    + " ...calling listener on UI thread");
                        }
                        if (finalFailureFlag) {
                            dataLoaderListener.onCategoriesLoadFromLocalFailure();
                        } else {
                            dataLoaderListener.onCategoriesLoadFromLocalSuccess(categoryList);
                        }
                    }
                });
            }
        });
    }

    public void saveCategoriesToFile(List<Category> clueDetailsList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                StringBuffer buffer = new StringBuffer("[");
                for (int i = 0; i < clueDetailsList.size(); i++) {
                    if (i > 0) {
                        buffer.append(",");
                    }
                    buffer.append(RestClient.gson().toJson(clueDetailsList.get(i)));
                }
                buffer.append("]");

                try {
                    FileOutputStream fos = application.openFileOutput(Constants.LOCAL_FILE_NAME_FEED, Context.MODE_PRIVATE);
                    fos.write(buffer.toString().getBytes());
                    fos.close();

                    if (LOG) {
                        Log.d(TAG, ".saveCategoriesToFile: saved Clue Details, items saved - " + clueDetailsList.size());
                    }
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void readDetailsFromFile(int categoryId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CategoryDetailed c = null;
                String jsonString = loadJSONFromAsset(Constants.LOCAL_FILE_NAME_DETAILS);
                boolean failureFlag = false;

                if (jsonString == null) {
                    failureFlag = true;
                }

                if (!failureFlag) {
                    c = RestClient.gson().fromJson(jsonString, CategoryDetailed.class);
                    failureFlag = !c.getId().equals(categoryId);
                }

                CategoryDetailed finalC = c;
                boolean finalFailureFlag = failureFlag;

                resultHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, ".readDetails romFile: finished "
                                + (finalFailureFlag ? "with error":"successfully")
                                + " ...calling listener on UI thread");

                        if (finalFailureFlag) {
                            dataLoaderListener.onDetailsLoadFromLocalFailure();
                        } else {
                            dataLoaderListener.onDetailsLoadFromLocalSuccess(finalC);
                        }
                    }
                });
            }
        });
    }

    public void saveDetailsToFile(CategoryDetailed categoryDetailed) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = application.openFileOutput(Constants.LOCAL_FILE_NAME_DETAILS, Context.MODE_PRIVATE);
                    fos.write(RestClient.gson().toJson(categoryDetailed).getBytes());
                    fos.close();

                    if (LOG) {
                        Log.d(TAG, ".saveDetailsToFile: saved Category Details with id - " + categoryDetailed.getId());
                    }
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String loadJSONFromAsset(String fullPath) {
        String json;

        byte[] buffer = readFileFromAsset(fullPath);
        if (buffer == null) {
            return null;
        }

        json = new String(buffer, StandardCharsets.UTF_8);
        if (LOG) {
            Log.d(TAG, ".loadJSONFromAsset: JSON read from asset SUCCESS");
        }
        return json;
    }

    private byte[] readFileFromAsset(String fullPath) {
        byte[] buffer;

        try {
            InputStream is = application.openFileInput(fullPath);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            buffer = null;
        }

        return buffer;
    }
}
