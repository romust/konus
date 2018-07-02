package ru.ustyantsev.konus.data.RecyclerAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Participant;
import ru.ustyantsev.konus.utils.Log;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.RecyclerViewHolder> implements SectionIndexer {
    private List<Participant> items;
    public ParticipantsAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<Integer> mSectionPositions;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Participant item);
    }

    public ParticipantsAdapter(List<Participant> participant, Context context){
        items = participant;
        this.context = context;
        Collections.sort(items, (t0, t1) -> t0.getName().compareTo(t1.getName()));
    }

    @Override
    public ParticipantsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.present_list_item, parent, false);
        return new ParticipantsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParticipantsAdapter.RecyclerViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.group.setText(items.get(position).getGroup());
        holder.mSwitch.setOnCheckedChangeListener(null);
        if(items.get(position).isPresent()){
            holder.mSwitch.setChecked(true);
        } else holder.mSwitch.setChecked(false);
        holder.mSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setMessage("Студент присутствует?");
                ad.setPositiveButton("Подтвердить", (dialog, arg1) -> {
                    items.get(position).setPresent(b);
                    items.set(position, items.get(position));
                    onItemClickListener.onItemClick(items.get(position));
                    notifyItemChanged(position);
                });
                ad.setNegativeButton("Отмена", (dialog, arg1) -> notifyItemChanged(position));
                ad.setCancelable(false);
                ad.show();
            } else {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setMessage("Студент отсутствует?");
                ad.setPositiveButton("Подтвердить", (dialog, arg1) -> {
                    items.get(position).setPresent(b);
                    items.set(position, items.get(position));
                    onItemClickListener.onItemClick(items.get(position));
                    notifyItemChanged(position);
                });
                ad.setNegativeButton("Отмена", (dialog, arg1) -> notifyItemChanged(position));
                ad.setCancelable(false);
                ad.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(String.valueOf(items.size()));
        return items.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        Switch mSwitch;
        private TextView name;
        private TextView group;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            group = itemView.findViewById(R.id.group);
            mSwitch = itemView.findViewById(R.id.present_switch);
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {

        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        Log.d(String.valueOf(items.size()));
        for (int i = 0, size = items.size(); i < size; i++) {

            String section = String.valueOf(items.get(i).getName().charAt(0)).toUpperCase();
            Log.d(section);
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }
}