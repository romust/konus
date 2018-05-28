package ru.ustyantsev.konus.ui.Activities.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Administrator.AdministratorActivity;
import ru.ustyantsev.konus.ui.Activities.Moderator.ModeratorActivity;
import ru.ustyantsev.konus.ui.Activities.Student.StudentActivity;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;

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
