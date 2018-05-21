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
import ru.ustyantsev.konus.utils.Log;

public class ModerateLoginPresenter {
    private ModerateLoginView view;
    private FirebaseAuth mAuth;
    private LoginPresenter loginPresenter;
    private final Context context;

    ModerateLoginPresenter(ModerateLoginView view, Context context) {
        this.view = view;
        this.context = context;
        loginPresenter = new LoginPresenter(context);
    }

    void onCreateView() {
        mAuth = FirebaseAuth.getInstance();
    }

    void moderateLogIn(String email, String password) {
        loginPresenter.showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase Аутентификация: успешно!");
                            loginPresenter.hideProgressDialog();
                            loginPresenter.updateUI();
                        } else {
                            loginPresenter.hideProgressDialog();
                            Log.d("Firebase Аутентификация: не пройдено!" + task.getException());
                            view.showToast("Неверный логин или пароль!");
                        }
                    }
                });
    }
    public void hideKeyboard(View v){
        loginPresenter.hideKeyboard(v);
    }
}
