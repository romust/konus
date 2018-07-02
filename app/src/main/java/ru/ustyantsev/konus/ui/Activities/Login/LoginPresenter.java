package ru.ustyantsev.konus.ui.Activities.Login;

import android.content.Context;

public class LoginPresenter {
    private LoginView view; // view нужен, чтобы использовать методы view
    private Context context;

    public LoginPresenter(Context context){
        this.context = context;
    }

    public LoginPresenter(LoginView view, Context context){
        this.view = view;
        this.context = context;
    }
}
