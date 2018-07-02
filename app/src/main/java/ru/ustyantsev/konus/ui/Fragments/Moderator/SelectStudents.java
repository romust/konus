package ru.ustyantsev.konus.ui.Fragments.Moderator;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.GroupsStudents;
import ru.ustyantsev.konus.data.Participant;
import ru.ustyantsev.konus.data.RecyclerAdapters.ParticipantsAdapter;
import ru.ustyantsev.konus.data.RecyclerAdapters.SelectStudentsAdapter;
import ru.ustyantsev.konus.data.TransactionGroups;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class SelectStudents extends NavigationFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Utils utils = new Utils(getActivity());
    private IndexFastScrollRecyclerView recyclerView;
    private SelectStudentsAdapter adapter;
    FragmentReplacement fragmentReplace;
    ArrayList<Participant> participantsArrayList;

    public static SelectStudents newInstance(Bundle args) {
        SelectStudents fragment = new SelectStudents();
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

        setUpRecyclerView(v);
        loadData();
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.present_list)
                .includeToolbar()
                .toolbarTitle("Выбор студентов")
                .toolbarNavigationIcon(BACK)
                .menuRes(R.menu.ok, buildGlobalActions());
    }

    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.ok, () -> {
                    List<Participant> items = adapter.getItems();
                    List<String> students = new ArrayList<>();
                    for(int i = 0; i<items.size(); i++){
                        if(items.get(i).isPresent()){
                            students.add(items.get(i).getName());
                        }
                    }
                    Map<String, Object> a = new HashMap<>();
                    a.put("students", students);
                    db.collection("estimates").document(((TransactionGroups) getArguments().getParcelable("group")).getEstimateId()).collection("groups").document(((TransactionGroups)getArguments().getParcelable("group")).getGroupId())
                            .set(a, SetOptions.merge());
                    getActivity().onBackPressed();
                })
                .build();
    }

    private void loadData() {
        participantsArrayList = new ArrayList<>();
        db.collection("students")
                .get()
                .addOnCompleteListener(task -> {

                    for (DocumentSnapshot doc : task.getResult()) {
                        boolean flag = false;
                        if(((TransactionGroups)getArguments().getParcelable("group")).getStudents().contains(doc.getString("name"))){
                            flag = true;
                        }
                        Participant participant = new Participant(
                                doc.getString("name"),
                                doc.getString("group"),
                                flag,
                                null);
                        Log.d(participant.getName());
                        participantsArrayList.add(participant);
                    }
                    adapter = new SelectStudentsAdapter(participantsArrayList, getActivity());
                    /*adapter.onItemClickListener = (item) -> {
                        db.collection("estimates").document(getArguments().getString("estimateId")).collection("participants").document(getArguments().getString("estimateId"))
                                .set(item, SetOptions.merge());
                    };*/
                    initialiseUI();

                });
    }

    protected void initialiseUI() {
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setIndexTextSize(12);
        recyclerView.setIndexBarColor(R.color.colorGrey);//#33334c
        recyclerView.setIndexBarCornerRadius(45);
        recyclerView.setIndexBarTransparentValue((float) 0.4);
        recyclerView.setIndexbarMargin(4);
        recyclerView.setPreviewPadding(2);
        recyclerView.setIndexBarTextColor("#595959");
        recyclerView.setIndexBarVisibility(true);
        recyclerView.setIndexbarHighLateTextColor("#33334c");
        recyclerView.setIndexBarHighLateTextVisibility(true);
    }

    private void setUpRecyclerView(View v) {
        recyclerView = v.findViewById(R.id.participants_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
