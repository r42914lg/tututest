package com.r42914lg.tutu.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r42914lg.tutu.R;
import com.r42914lg.tutu.domain.Category;

import java.text.MessageFormat;
import java.util.List;

/**
 * Adapter implementation for our RecyclerView
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedAdapterViewHolder> {
    public static final String TAG = "LG> WorkItemAdapter";

    private final List<Category> categoryList;

    public static class FeedAdapterViewHolder extends RecyclerView.ViewHolder{
        protected TextView titleTextView;
        protected TextView cluesTextView;
        protected TextView idTextView;

        public FeedAdapterViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.category_title_text);
            cluesTextView = itemView.findViewById(R.id.category_clues_count);
            idTextView = itemView.findViewById(R.id.category_id_text);
        }
    }

    public FeedAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public FeedAdapter.FeedAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_recycler_row, parent,false);
        return new FeedAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.FeedAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category current = categoryList.get(position);

        holder.titleTextView.setText(MessageFormat.format("{0}{1}",
                holder.itemView.getContext().getString(R.string.category_title_prefix),
                current.getTitle()));

        holder.cluesTextView.setText(MessageFormat.format("{0}{1}",
                holder.itemView.getContext().getString(R.string.category_clue_count),
                current.getCluesCount()));

        holder.idTextView.setText(MessageFormat.format("{0}{1}",
                holder.itemView.getContext().getString(R.string.category_id_prefix),
                current.getId()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}