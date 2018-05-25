package ru.ustyantsev.konus.ui.Fragments.StudentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.RecyclerAdapter;

public class StudentRating extends Fragment {
    private FragmentReplacement fragmentReplace;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    ArrayList<Rating> ratingArrayList;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_rating, null);
        ratingArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        setUpFireBase();
        loadData();
        return v;
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
                        adapter = new RecyclerAdapter();
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
