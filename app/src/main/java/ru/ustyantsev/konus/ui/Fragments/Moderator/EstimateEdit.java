package ru.ustyantsev.konus.ui.Fragments.Moderator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.AddingTransactionGroup;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.RecyclerAdapters.TransactionGroupsAdapter;
import ru.ustyantsev.konus.data.TransactionGroups;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static java.lang.Math.abs;
import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class EstimateEdit extends NavigationFragment implements TransactionGroupsAdapter.OnItemClickListener, TransactionGroupsAdapter.DeleteClickListener{
    Utils utils;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FragmentReplacement fragmentReplace;
    private RecyclerView recyclerView;
    private TransactionGroupsAdapter adapter;
    EditText etTransactionTitle, etPresent, etMissing;
    ArrayList<TransactionGroups> groupsArrayList;
    FloatingActionButton fab;

    public static EstimateEdit newInstance(Bundle args) {
        EstimateEdit fragment = new EstimateEdit();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
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
        db = FirebaseFirestore.getInstance();
        utils = new Utils(getActivity());
        groupsArrayList = new ArrayList<>();
        super.onViewCreated(v, savedInstanceState);

        etTransactionTitle = v.findViewById(R.id.transaction_title);
        if(getArguments().getBoolean("admin")){
            //ll.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            etTransactionTitle.setEnabled(true);
            etTransactionTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", editable.toString());
                    db.collection("estimates").document(getArguments().getString("docId"))
                            .set(map, SetOptions.merge());
                }
            });
        }
        etPresent = v.findViewById(R.id.et_present);
        etMissing = v.findViewById(R.id.et_missing);
        fab = v.findViewById(R.id.estimate_fab);
        if(getArguments().getBoolean("admin")){
            invalidateNavigation(changeNavigation());
        }
        loadInitPrefs();
        etPresent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> map = new HashMap<>();
                if(editable.toString().equals("")){
                    map.put("present", Long.parseLong("0"));
                } else map.put("present", Long.parseLong(editable.toString()));
                db.collection("estimates").document(getArguments().getString("docId"))
                        .set(map, SetOptions.merge());
            }
        });
        etMissing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> map = new HashMap<>();
                if(editable.toString().equals("")||editable.toString().equals("-")){
                    map.put("missing", Long.parseLong("0"));
                } else map.put("missing", Long.parseLong(editable.toString()));
                db.collection("estimates").document(getArguments().getString("docId"))
                        .set(map, SetOptions.merge());
            }
        });
        setUpRecyclerView(v);
        loadData();
        fab.setOnClickListener(view -> {
            DocumentReference ref = db.collection("estimates").document(getArguments().getString("docId")).collection("groups").document();
            List<String> list = new ArrayList<>();

            AddingTransactionGroup addingGroup = new AddingTransactionGroup(
                    list,
                    Long.valueOf(0),
                    ""
            );
            TransactionGroups group = new TransactionGroups(
                    list,
                    "",
                    "",
                    getArguments().getString("docId"),
                    ref.getId()
            );
            adapter.addAll(group);
            ref.set(addingGroup);
        });
    }

    private void loadInitPrefs(){
        db.collection("estimates").document(getArguments().getString("docId")).get()
                .addOnSuccessListener(doc -> {
                   etTransactionTitle.setText(doc.getString("title"));
                   etPresent.setText(String.valueOf(doc.getLong("present")));
                   etMissing.setText(String.valueOf(doc.getLong("missing")));
                });
    }
    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.estimate_edit)
                .includeToolbar()
                .toolbarTitle("Смета баллов")
                .toolbarNavigationIcon(BACK)
                .menuRes(R.menu.send_estimate, buildGlobalActions());
    }

    private NavigationBuilder changeNavigation(){
        return navigation(R.layout.moderator_events_edit)
                .includeToolbar()
                .toolbarTitle("Смета баллов")
                .toolbarNavigationIcon(BACK)
                .menuRes(R.menu.approve, buildGlobalActions());
    }

    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.send_estimate, () -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("sent", true);
                    db.collection("estimates").document(getArguments().getString("docId"))
                            .set(map, SetOptions.merge());
                })
                .action(R.id.approve, () ->{
                    DocumentReference ref = db.collection("estimates").document(getArguments().getString("docId"));//данная смета баллов
                    ref.get()
                            .addOnSuccessListener(doc -> {
                                if(doc.getBoolean("approved")){
                                    utils.showToast("Невозможно выполнить транзакцию.\nБаллы этой сметы уже начисленны");
                                } else {
                                    db.collection("events").get()//существует ли мероприятие с данным id
                                            .addOnCompleteListener(task -> {
                                                boolean exist = false;
                                                for(DocumentSnapshot doc1: task.getResult()){
                                                    if(doc1.getId().equals(getArguments().getString("docId"))){
                                                        exist = true;
                                                    } else exist = false;
                                                }
                                                if(exist){
                                                    if(!String.valueOf(doc.getLong("present")).equals("0")&&!String.valueOf(doc.getLong("missing")).equals("0")){
                                                        db.collection("events").document(getArguments().getString("docId")).collection("participants")
                                                                .get()
                                                                .addOnCompleteListener(task1 -> {
                                                                    for(DocumentSnapshot doc2: task1.getResult()){

                                                                        if(!String.valueOf(doc.getLong("present")).equals("0")) {
                                                                            if (doc2.getBoolean("present")) {
                                                                                db.collection("students").whereEqualTo("name", doc2.getString("name"))
                                                                                        .get().addOnCompleteListener(task2 -> {
                                                                                    for (DocumentSnapshot doc3 : task2.getResult()) {
                                                                                        Map<String, Object> map = new HashMap<>();
                                                                                        map.put("points", doc3.getLong("points") + doc.getLong("present"));
                                                                                        db.collection("students").document(doc3.getId()).set(map, SetOptions.merge());
                                                                                        Map<String, Object> map2 = new HashMap<>();
                                                                                        map2.put("title", doc.getString("title"));
                                                                                        map2.put("info", "За присутствие");
                                                                                        map2.put("points", doc.getLong("present"));
                                                                                        db.collection("students").document(doc3.getId()).collection("transactions").add(map2);
                                                                                    }

                                                                                });
                                                                            }
                                                                        }
                                                                        if(!String.valueOf(doc.getLong("missing")).equals("0")) {
                                                                            if (!doc2.getBoolean("present")) {
                                                                                db.collection("students").whereEqualTo("name", doc2.getString("name"))
                                                                                        .get().addOnCompleteListener(task2 -> {
                                                                                    for (DocumentSnapshot doc3 : task2.getResult()) {
                                                                                        Map<String, Object> map = new HashMap<>();
                                                                                        map.put("points", doc3.getLong("points")+doc.getLong("missing"));
                                                                                        db.collection("students").document(doc3.getId()).set(map, SetOptions.merge());
                                                                                        Map<String, Object> map2 = new HashMap<>();
                                                                                        map2.put("title", doc.getString("title"));
                                                                                        map2.put("info", "За отсутствие");
                                                                                        map2.put("points", doc.getLong("missing"));
                                                                                        db.collection("students").document(doc3.getId()).collection("transactions").add(map2);
                                                                                    }

                                                                                });
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                                ref.collection("groups")
                                                        .get()
                                                        .addOnCompleteListener(task2->{
                                                            for(DocumentSnapshot doc4: task2.getResult()){
                                                                if(!String.valueOf(doc4.getLong("points")).equals("0") && !doc4.getString("info").equals("")){
                                                                    ArrayList <String> a = (ArrayList<String>) doc4.get("students");
                                                                    for(int i=0;i<a.size();i++){
                                                                        Log.d(a.get(i));
                                                                        db.collection("students")
                                                                                .whereEqualTo("name", a.get(i)).get()
                                                                                .addOnCompleteListener(task12 -> {
                                                                                    for(DocumentSnapshot doc5: task12.getResult()){
                                                                                        Log.d(doc5.getString("name"));
                                                                                        Map<String, Object> map = new HashMap<>();
                                                                                        map.put("points", doc5.getLong("points")+doc4.getLong("points"));
                                                                                        db.collection("students").document(doc5.getId()).set(map, SetOptions.merge());
                                                                                        Map<String, Object> map2 = new HashMap<>();
                                                                                        map2.put("title", doc.getString("title"));
                                                                                        map2.put("info", doc4.getString("info"));
                                                                                        map2.put("points", doc4.getLong("points"));
                                                                                        db.collection("students").document(doc5.getId()).collection("transactions")
                                                                                                .add(map2);
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            }
                                                        });
                                            });
                                }
                            });
                })
                .action(R.id.estimate_delete, () ->{
                    db.collection("events").get()
                            .addOnCompleteListener(task -> {
                                boolean exist = false;
                                for(DocumentSnapshot doc: task.getResult()){
                                    if(doc.getId().equals(getArguments().getString("docId"))){
                                        exist = true;
                                    } else exist = false;
                                }
                                if(exist){
                                    utils.showToast("Невозможно удалить смету, пока существует мероприятие с которым она связана.\nСначала удалите мероприятие");
                                } else {
                                    db.collection("estimates").document(getArguments().getString("docId")).delete();
                                    utils.showToast("Смета баллов удалена");
                                    getActivity().onBackPressed();
                                }
                            });
                })
                .build();
    }

    private void loadData() {

        db.collection("estimates").document(getArguments().getString("docId"))
                .collection("groups").orderBy("points", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    for(DocumentSnapshot doc: task.getResult()){
                        ArrayList <String> a = (ArrayList<String>) doc.get("students");
                        TransactionGroups group = new TransactionGroups(a, String.valueOf(doc.getLong("points")), doc.getString("info"), getArguments().getString("docId"), doc.getId());
                        groupsArrayList.add(group);
                    }
                    adapter = new TransactionGroupsAdapter(groupsArrayList, getActivity());
                    adapter.deleteClickListener = this;
                    adapter.onItemClickListener = this;
                    recyclerView.setAdapter(adapter);
                });
    }

    private void setUpRecyclerView(View v) {
        recyclerView = v.findViewById(R.id.transaction_groups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(TransactionGroups group) {
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        fragmentReplace.fragmentReplacement(SelectStudents.newInstance(args));
    }

    @Override
    public void deleteClick(String id) {
        db.collection("estimates").document(getArguments().getString("docId")).collection("groups").document(id).delete();
    }
}

