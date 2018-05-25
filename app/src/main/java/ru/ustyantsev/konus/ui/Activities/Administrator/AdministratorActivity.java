package ru.ustyantsev.konus.ui.Activities.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Login.LoginView;
import ru.ustyantsev.konus.ui.Activities.Sheets;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.AdministratorActivity.AdminSettings;
import ru.ustyantsev.konus.ui.Fragments.StudentActivity.StudentRating;

public class AdministratorActivity extends AppCompatActivity implements FragmentReplacement{

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.admin_fragment_container);
    StudentRating studentRating = new StudentRating();
    AdminSettings adminSettings = new AdminSettings();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_admin_rating:
                    fragmentReplacement(studentRating);
                    return true;
                case R.id.navigation_admin_events:
                    return true;
                case R.id.navigation_admin_transactions:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent2 = new Intent(getApplicationContext(), LoginView.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_admin_settings:
                    fragmentReplacement(adminSettings);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.admin_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            fragment = new StudentRating(); //создаем экземпляр фрагмента StudentLoginView
            fm.beginTransaction().add(R.id.admin_fragment_container, fragment).commit();//и добавляем его в контейнер
        }
    }

    @Override
    public void fragmentReplacement(Fragment fragment) { //реализация метода интерфейса фрагмента
        fm.beginTransaction().replace(R.id.admin_fragment_container, fragment).addToBackStack(null).commit(); //меняем фрагмент на другой и запихиваем его в backStack
    }

}
