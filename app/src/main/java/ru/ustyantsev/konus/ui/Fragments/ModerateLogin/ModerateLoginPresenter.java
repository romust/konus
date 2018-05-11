package ru.ustyantsev.konus.ui.Fragments.ModerateLogin;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;

class ModerateLoginPresenter {
    private ModerateLoginView view;
    private FirebaseAuth mAuth;
    private LoginPresenter loginPresenter = new LoginPresenter();
    private final Context context;
    private final static String TAG = "ooo";

    ModerateLoginPresenter(ModerateLoginView view, Context context) {
        this.view = view;
        this.context = context;
    }

    void onCreateView() {
        mAuth = FirebaseAuth.getInstance();
    }

    void setData(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginPresenter.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            view.showToast("Authentication failed.");
                            loginPresenter.updateUI(null);
                        }
                    }
                });
    }
}
