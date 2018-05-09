package ru.ustyantsev.konus.ui.Fragments;
import android.content.Context;

import ru.ustyantsev.konus.ui.Activities.FragmentReplacement;

public class PAuthStudentView {
    AuthStudentView view;
    MLogin mLogin;


    private FragmentReplacement fragmentReplace; //1.2 создаем объект интерфейса
    public PAuthStudentView(AuthStudentView view){
        this.view = view;
        mLogin = new MLogin();
    }

    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity

        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }
    public void setName(String name){
        mLogin.getName(name);
    }

    public void onButtonClicked(){
        fragmentReplace.fragmentReplacement();//1.7 вызываем метод интерфейса, в activity
    }
}
