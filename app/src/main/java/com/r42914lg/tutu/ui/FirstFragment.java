package com.r42914lg.tutu.ui;

import static com.r42914lg.tutu.Constants.LOG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.r42914lg.tutu.Constants;
import com.r42914lg.tutu.databinding.FragmentFirstBinding;
import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.model.TuTuViewModel;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements IFeedView {
    public static final String TAG = "LG> FirstFragment";

    private FragmentFirstBinding binding;
    private FeedPresenter feedPresenter;
    private List<Category> categoryList;
    private FeedAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // get reference to ViewModel, instantiate presenter, call init on presenter
        TuTuViewModel tuTuViewModel = new ViewModelProvider(requireActivity()).get(TuTuViewModel.class);
        feedPresenter = new FeedPresenter(tuTuViewModel, this);
        feedPresenter.initFeedView(this);

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        // setup RecyclerView & adapter
        binding.feedRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));
        categoryList = new ArrayList<>(Constants.CATEGORIES_TO_RETURN);
        adapter = new FeedAdapter(categoryList);
        binding.feedRecycler.setAdapter(adapter);

        if (LOG) {
            Log.d(TAG, ".onCreateView empty adapter set");
        }

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set listener to clicks
        binding.feedRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.feedRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // calling corresponding presenter method
                        feedPresenter.onDetailsRequested(position);

                        if (LOG) {
                            Log.d(TAG, " item selected position --> " + position);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do nothing
                    }
                })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Called from presenter, renders categories in RecyclerView
     * @param categoryList - list of Categories
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showFeed(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);

        adapter.notifyDataSetChanged();
    }
}