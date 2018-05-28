package ru.ustyantsev.konus.ui.Fragments.ModerateLogin;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;

public class ModerateLoginPresenter {
    private ModerateLoginView view;
    private FirebaseAuth mAuth;
    private LoginPresenter loginPresenter;
    private final Context context;
    Utils utils;

    ModerateLoginPresenter(ModerateLoginView view, Context context) {
        this.view = view;
        this.context = context;
        loginPresenter = new LoginPresenter(context);
        utils = new Utils(context);
        utils.initProgressDialog();
    }

    void onCreateView() {
        mAuth = FirebaseAuth.getInstance();
    }

    void moderateLogIn(String email, String password) {
            utils.showProgressDialog();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Firebase Аутентификация: успешно!");
                                utils.hideProgressDialog();
                                utils.updateUI();
                            } else {
                                utils.hideProgressDialog();
                                Log.d("Firebase Аутентификация: не пройдено!" + task.getException());
                                view.showToast("Неверный логин или пароль!");
                            }
                        }
                    });
    }
    public void hideKeyboard(View v){
        utils.hideKeyboard(v);
    }
}
