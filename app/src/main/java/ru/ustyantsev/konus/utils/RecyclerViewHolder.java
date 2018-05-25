package ru.ustyantsev.konus.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Rating;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView ratingPosition;
    private TextView name;
    private TextView group;
    private TextView points;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        ratingPosition = itemView.findViewById(R.id.rating_position);
        name = itemView.findViewById(R.id.name);
        group = itemView.findViewById(R.id.group);
        points = itemView.findViewById(R.id.points);
    }
    public void bind(Rating rating){
        ratingPosition.setText(rating.getRatingPosition());
        name.setText(rating.getName());
        group.setText(rating.getGroup());
        points.setText(rating.getPoints());
    }
}
