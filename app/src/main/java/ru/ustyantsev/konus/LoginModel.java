package ru.ustyantsev.konus;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class LoginModel {
    final static String TAG = "mytag";

    public FirebaseFirestore db;
    public void fireStore(){
        db = FirebaseFirestore.getInstance();

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("1", "20");
        transaction.put("2", "25");
        transaction.put("3", "15");

// Добавьте новый документ со сгенерированным ID
        db.collection("transaction").document("12")
                .set(transaction)
                // Устанавливаем слушатели успеха/провала
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot успешно записан!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Ошибка добавления документа", e);
                    }
                });
        db.collection("transaction").document("12").collection("trans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, "1: " +doc.getId() + " " + doc.get("1"));
                            }
                        } else {
                            Log.w(TAG, "Ошибка получения документа.", task.getException());
                        }
                    }
                });
    }
    public String toPlusTwo(String name){
        return name + "her";
    }
}
