package ru.ustyantsev.konus.ui.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Fragments.AuthModerateView;
import ru.ustyantsev.konus.ui.Fragments.AuthStudentView;
import ru.ustyantsev.konus.ui.Fragments.PAuthStudentView;

public class LoginView extends FragmentActivity implements PAuthStudentView.OnFragment1DataListener{ //implements IloginView, View.OnClickListener{
    /*LoginPresenter presenter;
    Button btn;
    EditText et;*/
    FragmentManager fm = getSupportFragmentManager();
    AuthModerateView authModerateView;
    Fragment fragment = fm.findFragmentById(R.id.container);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        if (fragment == null){ //если контейнер фрагментов пустой
            fragment = new AuthStudentView(); //создаем экземпляр фрагмента AuthStudentView
            fm.beginTransaction()             //и добавляем его в контейнер
                    .add(R.id.container, fragment)
                    .commit();
        }
        authModerateView = new AuthModerateView();
    }
    @Override
    public void onFragment1DataListener() { //реализация метода интерфейса фрагмента
        fm.beginTransaction().replace(R.id.container, authModerateView).addToBackStack(null).commit(); //меняем фрагмент на другой и запихиваем его в backStack

    }

        /*et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
        presenter = new LoginPresenter(this);*/

   /* public void replaceFragments() {
        Log.d("tt", "оуцтаотцуатуцщшрщрщгпшз");
        trans.replace(R.id.container, authModerateView);
        trans.commit();
    }*/



    /*@Override
    public String getName() {
        return et.getText().toString();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        presenter.onGetButtonClicked();
    }*/
}
