package ru.ustyantsev.konus.ui.Fragments.Moderator;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.Participant;
import ru.ustyantsev.konus.data.RecyclerAdapters.ParticipantsAdapter;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class PresentList extends NavigationFragment {
    Utils utils;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private IndexFastScrollRecyclerView recyclerView;
    private ParticipantsAdapter adapter;
    //FragmentReplacement fragmentReplace;
    ArrayList<Participant> participantsArrayList;

    public static PresentList newInstance(Bundle args) {
        PresentList fragment = new PresentList();
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }*/

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
                .toolbarTitle("Присутствующие")
                .toolbarNavigationIcon(BACK);
    }

    private void loadData() {
        participantsArrayList = new ArrayList<>();
        DocumentReference ref = db.collection("events").document(((Event) getArguments().getParcelable("event")).getDocId());
        ref.collection("participants")
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Participant participant = new Participant(
                                doc.getString("name"),
                                doc.getString("group"),
                                doc.getBoolean("present"),
                                doc.getId());
                        Log.d(participant.getName());
                        participantsArrayList.add(participant);
                    }
                    adapter = new ParticipantsAdapter(participantsArrayList, getActivity());
                    adapter.onItemClickListener = (item) -> {
                        ref.collection("participants").document(item.getDocId())
                                .set(item, SetOptions.merge());
                    };
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
