package ru.ustyantsev.konus.ui.Fragments.StudentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;

public class StudentEvents extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_events, null);
        return v;
    }

    private FragmentReplacement fragmentReplace;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }
}
