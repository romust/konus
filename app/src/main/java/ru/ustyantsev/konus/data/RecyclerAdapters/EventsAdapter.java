package ru.ustyantsev.konus.data.RecyclerAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.RecyclerViewHolder> {
    private ArrayList<Event> items = new ArrayList<>();
    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Event item);
    }



    public void addAll(List<Event> events) {
        int pos = getItemCount();
        this.items.addAll(events);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @Override
    public EventsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moderator_events_item, parent, false);
        return new EventsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.RecyclerViewHolder holder, int position) {
        holder.bind(items.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView mCard;
        private TextView title;
        private TextView date;
        private TextView time;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            date = itemView.findViewById(R.id.event_date);
            time = itemView.findViewById(R.id.event_time);
            mCard = itemView.findViewById(R.id.event_item);
        }

        void bind(Event event, OnItemClickListener onItemClickListener) {
            title.setText(event.getTitle());
            date.setText("Дата: " + event.getDate());
            time.setText("Время: " + event.getTime());
            mCard.setOnClickListener(view -> onItemClickListener.onItemClick(event));
        }
    }
}
