package ru.ustyantsev.konus.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Rating;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    private ArrayList<Rating> items = new ArrayList<>();
    public void addAll(List<Rating> fakeItems){
        int pos = getItemCount();
        this.items.addAll(fakeItems);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
