package ru.ustyantsev.konus.ui.Fragments.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Rating;
import ru.ustyantsev.konus.ui.Activities.Login.LoginView;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.data.RecyclerAdapters.StudentRatingAdapter;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static android.content.Context.MODE_PRIVATE;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class StudentRating extends NavigationFragment {
    private final MenuActions globalMenuActions = buildGlobalActions();
    private FragmentReplacement fragmentReplace;
    private RecyclerView recyclerView;
    private StudentRatingAdapter adapter;
    ArrayList<Rating> ratingArrayList;
    FirebaseFirestore db;
    SharedPreferences pref;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        ratingArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        setUpFireBase();
        loadData();
    }
    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.student_rating)
                .includeToolbar()
                .toolbarTitle("Рейтинг КОНУС")
                .toolbarNavigationIcon(NO_NAV_ICON)
                .menuRes(R.menu.exit, buildGlobalActions());
    }
    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.exit, ()->{
                    pref = getActivity().getSharedPreferences("student", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("name");
                    editor.remove("docId");
                    editor.commit();
                    Intent intent = new Intent (getActivity(), LoginView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .build();
    }

    private void loadData(){

        db.collection("students")
                .orderBy("points", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        for(DocumentSnapshot querySnapshot: task.getResult()){
                            i+=1;
                            Rating rating = new Rating (String.valueOf(i),
                                                        querySnapshot.getString("name"),
                                                        querySnapshot.getString("group"),
                                                        querySnapshot.getLong("points").toString());
                            ratingArrayList.add(rating);
                        }
                        adapter = new StudentRatingAdapter();
                        recyclerView.setAdapter(adapter);
                        adapter.addAll(ratingArrayList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("------error------");
                    }
                });
    }
    private void setUpFireBase(){
        db = FirebaseFirestore.getInstance();
    }
    private void setUpRecyclerView(View v){
        recyclerView = v.findViewById(R.id.rating_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
}
