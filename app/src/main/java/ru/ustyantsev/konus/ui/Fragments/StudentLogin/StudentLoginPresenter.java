package ru.ustyantsev.konus.ui.Fragments.StudentLogin;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;
import ru.ustyantsev.konus.ui.Activities.Student.StudentActivity;
import ru.ustyantsev.konus.ui.Fragments.ModerateLogin.ModerateLoginView;
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
    private LoginPresenter loginPresenter;
    private FragmentReplacement fragmentReplace; //1.2 создаем объект интерфейса


    StudentLoginPresenter(StudentLoginView view, Context context){
        this.view = view;
        this.context = context;
        loginPresenter = new LoginPresenter(context);
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
        loginPresenter.showProgressDialog();
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
                                editor.commit();
                                Log.d("SAVED");
                            }
                            if(pref.getString("name", null)!=null) {
                                loginPresenter.hideProgressDialog();
                                loginPresenter.updateUI();
                            }
                            else{
                                loginPresenter.hideProgressDialog();
                                view.showToast("Проверьте правильность введенных данных");
                            }

                        } else {
                            loginPresenter.hideProgressDialog();
                            Log.d("Ошибка получения документов: " + task.getException());
                        }
                    }
                });
    }
    public void hideKeyboard(View v){
        loginPresenter.hideKeyboard(v);
    }


}
