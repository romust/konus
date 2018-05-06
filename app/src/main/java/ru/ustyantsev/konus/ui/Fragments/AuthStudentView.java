package ru.ustyantsev.konus.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.ustyantsev.konus.R;

public class AuthStudentView extends Fragment implements View.OnClickListener{
    Button button;
    public PAuthStudentView pAuthStudentView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auth_student, null);
        button = v.findViewById(R.id.button);
        button.setOnClickListener(this);
        pAuthStudentView = new PAuthStudentView(this);
        return v;
    }
    @Override
    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        super.onAttach(context);
        pAuthStudentView.onAttac(context);
    }

    @Override
    public void onClick(View view) {
        pAuthStudentView.onButtonClicked();
    }
}
