package ru.ustyantsev.konus.ui.Fragments.StudentLogin;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.ui.Activities.common.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.Student;

public class StudentLoginPresenter {
    StudentLoginView view;
    private FirebaseFirestore db;
    final static String TAG = "mytag";


    private FragmentReplacement fragmentReplace; //1.2 создаем объект интерфейса
    StudentLoginPresenter(StudentLoginView view){
        this.view = view;
    }

    void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity

        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }
    public void setName(final String name){
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

    void onButtonClicked(){
        fragmentReplace.fragmentReplacement();//1.7 вызываем метод интерфейса, в activity
    }
}
