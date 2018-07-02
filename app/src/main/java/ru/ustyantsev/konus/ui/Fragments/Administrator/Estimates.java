package ru.ustyantsev.konus.ui.Fragments.Administrator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Estimate;
import ru.ustyantsev.konus.data.RecyclerAdapters.EstimatesAdapter;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.Moderator.EstimateEdit;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationDefaults;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;

import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class Estimates extends NavigationFragment {
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EstimatesAdapter adapter;
    ArrayList<Estimate> estimateArrayList;
    FirebaseFirestore db;
    private FragmentReplacement fragmentReplace;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }
    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        fab = v.findViewById(R.id.estimate_add);
        estimateArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        setUpFireBase();
        loadData();
        fab.setOnClickListener(view -> {
            DocumentReference ref = db.collection("estimates").document();
            Map<String, Object> map = new HashMap<>();
            map.put("title", "");
            map.put("present", Long.parseLong("0"));
            map.put("missing", Long.parseLong("0"));
            map.put("sent", true);
            map.put("approved", false);
            ref.set(map);
            Bundle args = new Bundle();
            args.putString("docId", ref.getId());
            args.putBoolean("admin", true);
            fragmentReplace.fragmentReplacement(EstimateEdit.newInstance(args));
            //fragmentReplace.fragmentReplacement(EstimateEdit.newInstance(args));
        });

        NavigationDefaults.NavigationDefaultsHolder.navigationDefaults()
                .navigationIconListener(view-> getActivity().onBackPressed());
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.estimates)
                .includeToolbar()
                .toolbarTitle("Сметы баллов")
                .toolbarNavigationIcon(NO_NAV_ICON);
    }

    private void loadData(){
        db.collection("estimates")
                .orderBy("approved")
                .get()
                .addOnCompleteListener(task -> {
                    for(DocumentSnapshot doc: task.getResult()){
                        if(doc.getBoolean("sent")) {
                            Estimate estimate = new Estimate(
                                    doc.getString("title"),
                                    doc.getLong("present"),
                                    doc.getLong("missing"),
                                    doc.getId(),
                                    doc.getBoolean("approved"));
                            estimateArrayList.add(estimate);
                        }
                    }
                    adapter = new EstimatesAdapter();
                    adapter.onItemClickListener = (item) -> {
                        Bundle args = new Bundle();
                        args.putString("docId", item.getEstimateId());
                        args.putBoolean("admin", true);
                        fragmentReplace.fragmentReplacement(EstimateEdit.newInstance(args));
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.addAll(estimateArrayList);
                })
                .addOnFailureListener(e -> Log.d("------error------"));
    }
    private void setUpFireBase(){
        db = FirebaseFirestore.getInstance();
    }
    private void setUpRecyclerView(View v){
        recyclerView = v.findViewById(R.id.estimates_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

