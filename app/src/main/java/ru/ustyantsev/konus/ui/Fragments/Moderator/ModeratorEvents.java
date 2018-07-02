package ru.ustyantsev.konus.ui.Fragments.Moderator;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.RecyclerAdapters.EventsAdapter;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationDefaults;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class ModeratorEvents extends NavigationFragment {
FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    ArrayList<Event> eventsArrayList;
    FirebaseFirestore db;
    private FragmentReplacement fragmentReplace;
    Calendar dateAndTime = Calendar.getInstance();

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
        fab = v.findViewById(R.id.events_fab);
        eventsArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        setUpFireBase();
        loadData();
        fab.setOnClickListener(view -> {
            ModeratorEventsEdit moderatorEventsEdit = new ModeratorEventsEdit();
            fragmentReplace.fragmentReplacement(moderatorEventsEdit.newInstance(null));
        });

        NavigationDefaults.NavigationDefaultsHolder.navigationDefaults()
                .navigationIconListener(view-> getActivity().onBackPressed());
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.moderator_events)
                .includeToolbar()
                .toolbarTitle("Мероприятия")
                .toolbarNavigationIcon(NO_NAV_ICON);
    }

    private void loadData(){
        db.collection("events")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {

                    for(DocumentSnapshot doc: task.getResult()){
                        dateAndTime.setTimeInMillis(doc.getTimestamp("dateTime").getSeconds()*1000);
                        String date = DateUtils.formatDateTime(getActivity(),
                                dateAndTime.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                        String time = DateUtils.formatDateTime(getActivity(),
                                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
                        Event event = new Event (doc.getId(),
                                doc.getString("title"),
                                doc.getString("place"),
                                doc.getString("info"),
                                date,
                                time,
                                dateAndTime.getTimeInMillis());
                        eventsArrayList.add(event);
                        Log.d(doc.getString("title"));
                    }
                    adapter = new EventsAdapter();
                    adapter.onItemClickListener = (item) -> {
                        Bundle args = new Bundle();
                        args.putParcelable("event", item);
                        fragmentReplace.fragmentReplacement(ModeratorEventPage.newInstance(args));
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.addAll(eventsArrayList);
                })
                .addOnFailureListener(e -> Log.d("------error------"));
    }
    private void setUpFireBase(){
        db = FirebaseFirestore.getInstance();
    }
    private void setUpRecyclerView(View v){
        recyclerView = v.findViewById(R.id.moderator_events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
