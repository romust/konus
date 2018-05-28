package ru.ustyantsev.konus.ui.Activities.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Administrator.AdministratorActivity;
import ru.ustyantsev.konus.ui.Activities.Moderator.ModeratorActivity;
import ru.ustyantsev.konus.ui.Activities.Student.StudentActivity;
import ru.ustyantsev.konus.utils.Log;

public class Utils {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    private Context context;
    private ProgressDialog progressDialog;
    public Utils (Context context){
        this.context = context;
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

    public void updateUI(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if(currentUser!=null) {
            if(!db.collection("administrators").document(currentUser.getUid()).getId().isEmpty()){
                Log.d("admin есть!");
                Intent intent = new Intent(context, AdministratorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
            else if(!db.collection("moderators").document(currentUser.getUid()).getId().isEmpty()){
                Log.d("Модератор обнаружен!!!");
                Intent intent = new Intent(context, ModeratorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }
        else{
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

    public void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
