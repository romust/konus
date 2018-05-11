package ru.ustyantsev.konus.ui.Fragments.StudentLogin;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.ustyantsev.konus.R;

public class StudentLoginView extends Fragment implements View.OnClickListener{
    Button btnReplace, btnLogin;
    EditText etName;
    public StudentLoginPresenter studentLoginPresenter;

    @Override
    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        super.onAttach(context);
        studentLoginPresenter = new StudentLoginPresenter(this);
        studentLoginPresenter.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auth_student, null);
        etName = v.findViewById(R.id.etName);
        btnReplace = v.findViewById(R.id.btnReplace);
        btnLogin = v.findViewById(R.id.btnLogin);
        btnReplace.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                studentLoginPresenter.setName(etName.getText().toString());
                break;
            case R.id.btnReplace:
                studentLoginPresenter.onButtonClicked();
                break;
        }
    }
}