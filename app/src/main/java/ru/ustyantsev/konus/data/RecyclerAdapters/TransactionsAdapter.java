package ru.ustyantsev.konus.data.RecyclerAdapters;

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
import ru.ustyantsev.konus.data.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.RecyclerViewHolder> {
    private ArrayList<Transaction> items = new ArrayList<>();


    public void addAll(List<Transaction> transactions) {
        int pos = getItemCount();
        this.items.addAll(transactions);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @Override
    public TransactionsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list_item, parent, false);
        return new TransactionsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionsAdapter.RecyclerViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.info.setText(items.get(position).getInfo());
        holder.points.setText(items.get(position).getPoints());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView info;
        private TextView points;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.student_transaction_title);
            info = itemView.findViewById(R.id.student_transaction_info);
            points = itemView.findViewById(R.id.student_transaction_points);

        }
    }
}
