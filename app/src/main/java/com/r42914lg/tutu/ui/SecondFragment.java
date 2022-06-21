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

import com.r42914lg.tutu.R;
import com.r42914lg.tutu.databinding.FragmentSecondBinding;
import com.r42914lg.tutu.domain.CategoryDetailed;
import com.r42914lg.tutu.model.TuTuViewModel;

public class SecondFragment extends Fragment implements IDetailsView {
    public static final String TAG = "LG> SecondFragment";

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // get reference to ViewModel, instantiate presenter, call init on presenter
        TuTuViewModel tuTuViewModel = new ViewModelProvider(requireActivity()).get(TuTuViewModel.class);
        DetailsPresenter detailsPresenter = new DetailsPresenter(tuTuViewModel, this);
        detailsPresenter.initDetailsView(this);

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (LOG) {
            Log.d(TAG, ".onViewCreated");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void showDetails(CategoryDetailed categoryDetailed) {
        binding.detailCategoryId.setText(String.format("%s%d", getString(R.string.category_id_prefix), categoryDetailed.getId()));
        binding.detailCategoryTitle.setText(String.format("%s%s", getString(R.string.category_title_prefix), categoryDetailed.getTitle()));
        binding.detailCluesCount.setText(String.format("%s%d", getString(R.string.category_clue_count), categoryDetailed.getCluesCount()));

        StringBuffer buffer = new StringBuffer();
        categoryDetailed.getClues().forEach(clue -> buffer.append(clue.asString()));
        binding.detailClues.setText(buffer);
    }
}