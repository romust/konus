package ru.ustyantsev.konus.data.RecyclerAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Estimate;

public class EstimatesAdapter extends RecyclerView.Adapter<EstimatesAdapter.RecyclerViewHolder> {
    private ArrayList<Estimate> items = new ArrayList<>();
    public EstimatesAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Estimate item);
    }



    public void addAll(List<Estimate> estimate) {
        int pos = getItemCount();
        this.items.addAll(estimate);
        notifyItemRangeInserted(pos, this.items.size());
    }
    public void addAll(Estimate estimate){
        int pos = getItemCount();
        this.items.add(estimate);
        notifyItemInserted(pos);
    }
    @Override
    public EstimatesAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_item, parent, false);
        return new EstimatesAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstimatesAdapter.RecyclerViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.mCard.setOnClickListener(view -> onItemClickListener.onItemClick(items.get(position)));
        if(items.get(position).isApproved()){
            holder.logo.setImageResource(R.drawable.ok);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView mCard;
        private TextView title;
        ImageView logo;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mCard = itemView.findViewById(R.id.estimate_item);
            title = itemView.findViewById(R.id.estimate_title);
            logo = itemView.findViewById(R.id.estimate_logo);
        }
    }
}