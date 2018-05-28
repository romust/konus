package ru.ustyantsev.konus.ui.Activities.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.ModerateLogin.ModerateLoginView;
import ru.ustyantsev.konus.ui.Fragments.StudentActivity.StudentRating;
import ru.ustyantsev.konus.ui.Fragments.StudentLogin.StudentLoginView;
import ru.ustyantsev.konus.utils.Log;

public class LoginView extends FragmentActivity implements FragmentReplacement { //implements IloginView, View.OnClickListener{
    LoginPresenter presenter;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity");
        presenter = new LoginPresenter(this, this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_view);

        if(savedInstanceState == null) {
                fragment = new StudentLoginView(); //создаем экземпляр фрагмента StudentLoginView
                fm.beginTransaction().add(R.id.container, fragment).commit();//и добавляем его в контейнер

        }
    }

    @Override
    public void fragmentReplacement(Fragment fragment) { //реализация метода интерфейса фрагмента
        fm.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit(); //меняем фрагмент на другой и запихиваем его в backStack
    }
}
