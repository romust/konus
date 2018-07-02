package ru.ustyantsev.konus.data.RecyclerAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.AddingTransactionGroup;
import ru.ustyantsev.konus.data.Participant;
import ru.ustyantsev.konus.data.Rating;
import ru.ustyantsev.konus.data.TransactionGroups;
import ru.ustyantsev.konus.utils.Log;

public class TransactionGroupsAdapter extends RecyclerView.Adapter<TransactionGroupsAdapter.RecyclerViewHolder>  {
    private List<TransactionGroups> items;
    public OnItemClickListener onItemClickListener;
    public DeleteClickListener deleteClickListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(TransactionGroups group);
    }

    public interface DeleteClickListener {
        void deleteClick(String id);
    }


    public TransactionGroupsAdapter(List<TransactionGroups> groups, Context context){
        items = groups;
        this.context = context;

    }

    public void addAll(TransactionGroups group){
        int pos = getItemCount();
        this.items.add(group);
        notifyItemInserted(pos);
    }

    @Override
    public TransactionGroupsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_groups_item, parent, false);
        return new TransactionGroupsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionGroupsAdapter.RecyclerViewHolder holder, int position) {
        if(items.get(position).getStudents().size()>0) {
            holder.tvStudents.append(items.get(position).getStudents().get(0));
            for (int i = 1; i < items.get(position).getStudents().size(); i++) {
                holder.tvStudents.append(", " + items.get(position).getStudents().get(i));
            }
        }
        holder.etTransactionPoints.setText(items.get(position).getPoints());
        holder.etTransactionPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> map = new HashMap<>();
                if(editable.toString().equals("")){
                    map.put("points", Long.parseLong("0"));
                } else map.put("points", Long.parseLong(editable.toString()));
                db.collection("estimates").document(items.get(position).getEstimateId()).collection("groups").document(items.get(position).getGroupId())
                        .set(map, SetOptions.merge());
            }
        });
        holder.etTransactionInfo.setText(items.get(position).getInfo());
        holder.etTransactionInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> map = new HashMap<>();
                map.put("info", editable.toString());
                db.collection("estimates").document(items.get(position).getEstimateId()).collection("groups").document(items.get(position).getGroupId())
                        .set(map, SetOptions.merge());
            }
        });
        holder.groupDelete.setOnClickListener(view -> {
            deleteClickListener.deleteClick(items.get(position).getGroupId());
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        });
        holder.btnAdd.setOnClickListener(view ->{
            onItemClickListener.onItemClick(items.get(position));
        });
        holder.tvStudents.setOnClickListener(view -> onItemClickListener.onItemClick(items.get(position)));
    }

    @Override
    public int getItemCount() {
        Log.d(String.valueOf(items.size()));
        return items.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStudents;
        private TextView btnAdd;
        ImageView groupDelete;
        private EditText etTransactionPoints;
        private EditText etTransactionInfo;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvStudents = itemView.findViewById(R.id.tv_students);
            btnAdd = itemView.findViewById(R.id.btn_add_students);
            etTransactionPoints = itemView.findViewById(R.id.transaction_points);
            etTransactionInfo = itemView.findViewById(R.id.transaction_info);
            groupDelete = itemView.findViewById(R.id.group_delete);
        }
    }

}