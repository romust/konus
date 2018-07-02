package ru.ustyantsev.konus.ui.Fragments.Student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.RecyclerAdapters.EventsAdapter;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationDefaults;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;

import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class StudentEvents extends NavigationFragment{
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    ArrayList<Event> eventsArrayList;
    FirebaseFirestore db;
    private FragmentReplacement fragmentReplace;
    private Long firestoreTimeStamp;

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
        eventsArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        db = FirebaseFirestore.getInstance();
        loadData();

        NavigationDefaults.NavigationDefaultsHolder.navigationDefaults()
                .navigationIconListener(view-> getActivity().onBackPressed());
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.student_events)
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
                        firestoreTimeStamp = doc.getTimestamp("dateTime").getSeconds()*1000;
                        Date timestamp = new Date(firestoreTimeStamp);
                        String date = new SimpleDateFormat("dd MMMM yyyy")
                                .format(timestamp);
                        String time = new SimpleDateFormat("HH:mm")
                                .format(timestamp);
                        Event event = new Event (doc.getId(),
                                doc.getString("title"),
                                doc.getString("place"),
                                doc.getString("info"),
                                date,
                                time,
                                firestoreTimeStamp);
                        eventsArrayList.add(event);
                        Log.d(doc.getString("title"));
                    }

                    adapter = new EventsAdapter();
                    adapter.onItemClickListener = (item) -> {
                        Bundle args = new Bundle();
                        args.putParcelable("event", item);
                        fragmentReplace.fragmentReplacement(StudentEventPage.newInstance(args));
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.addAll(eventsArrayList);
                })
                .addOnFailureListener(e -> Log.d("------error------"));
    }
    private void setUpRecyclerView(View v){
        recyclerView = v.findViewById(R.id.student_events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
