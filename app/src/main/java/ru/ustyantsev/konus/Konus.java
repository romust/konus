package ru.ustyantsev.konus;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.ui.Activities.Administrator.AdministratorActivity;
import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;
import ru.ustyantsev.konus.ui.Activities.Moderator.ModeratorActivity;
import ru.ustyantsev.konus.ui.Activities.Student.StudentActivity;
import ru.ustyantsev.konus.utils.Log;

public class Konus extends Application {
    /*LoginPresenter loginPresenter;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    private boolean admin = false;*/
    @Override
    public void onCreate() {
        /*Log.d("Application");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            db = FirebaseFirestore.getInstance();
            db.collection("administrators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for(DocumentSnapshot doc : task.getResult()) {
                            if(doc.getId().equals(currentUser.getUid())){admin = true;                                    }
                            Log.d("SAVED");
                        }
                        if(admin){
                            Log.d("APPPPPPPPPPPPP");
                            Intent intent = new Intent(getApplicationContext(), AdministratorActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), ModeratorActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    } else {
                        Log.d("Ошибка получения документа.");
                    }
                }
            });
        }
        else {
            SharedPreferences pref;
            pref = getApplicationContext().getSharedPreferences("student", getApplicationContext().MODE_PRIVATE);
            String userStudent = pref.getString("name", null);
            if(userStudent!=null){
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
            }

        }*/
        super.onCreate();

    }



}
