package ru.ustyantsev.konus.ui.Fragments.Authentication;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;

import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;

public class StudentLoginPresenter {
    StudentLoginView view;
    private FirebaseFirestore db;
    SharedPreferences pref;
    private Context context;
    private FragmentReplacement fragmentReplace; //1.2 создаем объект интерфейса
    Utils utils;

    StudentLoginPresenter(StudentLoginView view, Context context){
        this.view = view;
        this.context = context;
        utils = new Utils(context);
        utils.initProgressDialog();
    }

    void onModeratorButtonClicked(){
        ModerateLoginView moderateLoginView = new ModerateLoginView();
        fragmentReplace.fragmentReplacement(moderateLoginView);//1.7 вызываем метод интерфейса, в activity
    }

    void findFragmentReplacement() { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }
    public void studentLogIn(final String name){
        utils.showProgressDialog();
        db = FirebaseFirestore.getInstance();
        db.collection("students")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pref = context.getSharedPreferences("student", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            for(DocumentSnapshot doc : task.getResult()) {
                                editor.putString("name", doc.get("name").toString());
                                editor.putString("docId", doc.getId());
                                editor.commit();
                            }
                            if(pref.getString("name", null)!=null) {
                                utils.hideProgressDialog();
                                utils.updateUI();
                            }
                            else{
                                utils.hideProgressDialog();
                                view.showToast("Проверьте правильность введенных данных");
                            }

                        } else {
                            utils.hideProgressDialog();
                            Log.d("Ошибка получения документов: " + task.getException());
                        }
                    }
                });
    }
    public void hideKeyboard(View v){
        utils.hideKeyboard(v);
    }


}
