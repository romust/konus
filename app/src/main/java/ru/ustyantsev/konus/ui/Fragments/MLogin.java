package ru.ustyantsev.konus.ui.Fragments;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MLogin {
    final static String TAG = "mytag";
    public FirebaseFirestore db;

    public void getName(final String name){
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for(DocumentSnapshot doc : task.getResult()) {
                                Student student = doc.toObject(Student.class);
                                student.setId(doc.getId());

                                Log.d(TAG, student.getName() + " " + student.getId());
                            }
                        } else {
                            Log.w(TAG, "Ошибка получения документа.", task.getException());
                        }
                    }
                });
    }
}
