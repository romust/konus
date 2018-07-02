package ru.ustyantsev.konus.data.RecyclerAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Rating;


public class StudentRatingAdapter extends RecyclerView.Adapter<StudentRatingAdapter.RecyclerViewHolder>{
    private ArrayList<Rating> items = new ArrayList<>();
    public void addAll(List<Rating> students){
        int pos = getItemCount();
        this.items.addAll(students);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rating_item, parent, false);
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

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView ratingPosition;
        private TextView name;
        private TextView group;
        private TextView points;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            ratingPosition = itemView.findViewById(R.id.rating_position);
            name = itemView.findViewById(R.id.name);
            group = itemView.findViewById(R.id.group);
            points = itemView.findViewById(R.id.points);
        }
        void bind(Rating rating){
            ratingPosition.setText(rating.getRatingPosition());
            name.setText(rating.getName());
            group.setText(rating.getGroup());
            points.setText(rating.getPoints());
        }
    }
}
