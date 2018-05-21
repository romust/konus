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
import ru.ustyantsev.konus.utils.Log;

public class LoginPresenter {
    private LoginView view; // view нужен, чтобы использовать методы view
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean admin = false;

    public LoginPresenter(Context context){
        this.context = context;
        initProgressDialog();
    }

    public LoginPresenter(LoginView view, Context context){
        this.view = view;
        this.context = context;
    }

    public void updateUI() {
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
                                    Intent intent = new Intent(context, AdministratorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(context, ModeratorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }
                            } else {
                                Log.d("Ошибка получения документа.");
                            }
                        }
                    });
        }
        else {
            SharedPreferences pref;
            pref = context.getSharedPreferences("student", context.MODE_PRIVATE);
            String userStudent = pref.getString("name", null);
            if(userStudent!=null){
                Intent intent = new Intent(context, StudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

        }
    }
    public void initProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

    }
    public void showProgressDialog(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
    }
    public void hideProgressDialog(){
        progressDialog.dismiss();
    }
    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
