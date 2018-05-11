package ru.ustyantsev.konus.ui.Activities.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.common.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.ModerateLogin.ModerateLoginView;
import ru.ustyantsev.konus.ui.Fragments.StudentLogin.StudentLoginView;

public class LoginView extends FragmentActivity implements FragmentReplacement { //implements IloginView, View.OnClickListener{
    LoginPresenter presenter;
    FragmentManager fm = getSupportFragmentManager();
    ModerateLoginView moderateLoginView;
    Fragment fragment = fm.findFragmentById(R.id.container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        presenter = new LoginPresenter(this);
        presenter.onCreateView();
        if (fragment == null) { //если контейнер фрагментов пустой
            fragment = new StudentLoginView(); //создаем экземпляр фрагмента StudentLoginView
            fm.beginTransaction()             //и добавляем его в контейнер
                    .add(R.id.container, fragment)
                    .commit();
        }
        moderateLoginView = new ModerateLoginView();
    }
    @Override
    public void onStart(){
        super.onStart();
        presenter.checkCurrentUser();
    }

    @Override
    public void fragmentReplacement() { //реализация метода интерфейса фрагмента
        fm.beginTransaction().replace(R.id.container, moderateLoginView).addToBackStack(null).commit(); //меняем фрагмент на другой и запихиваем его в backStack
    }
}
