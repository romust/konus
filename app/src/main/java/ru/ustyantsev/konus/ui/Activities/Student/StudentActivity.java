package ru.ustyantsev.konus.ui.Activities.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Login.LoginView;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.Student.StudentEvents;
import ru.ustyantsev.konus.ui.Fragments.Student.StudentRating;
import ru.ustyantsev.konus.ui.Fragments.Student.StudentTransactions;

public class StudentActivity extends AppCompatActivity implements FragmentReplacement {
    SharedPreferences pref;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.student_fragment_container);
    StudentRating studentRating = new StudentRating();
    StudentEvents studentEvents = new StudentEvents();
    StudentTransactions transactions = new StudentTransactions();




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.student_navigation_rating:
                    fragmentReplacement(studentRating);
                    return true;
                case R.id.student_navigation_events:
                    fragmentReplacement(studentEvents);
                    return true;
                case R.id.student_navigation_history:
                    /**/
                    fragmentReplacement(transactions);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.student_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            fragment = new StudentRating(); //создаем экземпляр фрагмента StudentLoginView
            fm.beginTransaction().add(R.id.student_fragment_container, fragment).commit();//и добавляем его в контейнер
        }
    }
    @Override
    public void fragmentReplacement(Fragment fragment) { //реализация метода интерфейса фрагмента
        fm.beginTransaction().replace(R.id.student_fragment_container, fragment).addToBackStack(null).commit(); //меняем фрагмент на другой и запихиваем его в backStack
    }
}
