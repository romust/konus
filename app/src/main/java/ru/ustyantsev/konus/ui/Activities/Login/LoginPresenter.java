package ru.ustyantsev.konus.ui.Activities.Login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.ustyantsev.konus.common.Log;

public class LoginPresenter {
    private LoginView view;
    private FirebaseAuth mAuth;

    public LoginPresenter(){}

    public LoginPresenter(LoginView view){
        this.view = view;
    }

    void onCreateView() {
        mAuth = FirebaseAuth.getInstance();
    }

    void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();//
        updateUI(currentUser);
    }
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d("UPDATE");
        }
    }

}
