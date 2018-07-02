package ru.ustyantsev.konus.ui.Fragments.Authentication;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.ustyantsev.konus.R;

public class StudentLoginView extends Fragment implements View.OnClickListener{
    public StudentLoginPresenter presenter;
    EditText etName;
    Button btnReplace, btnLogin;


    @Override
    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        super.onAttach(context);
        presenter = new StudentLoginPresenter(this, context);
        presenter.findFragmentReplacement();

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
        presenter.hideKeyboard(view);
        switch (view.getId()) {
            case R.id.btnLogin:
                if(!etName.getText().toString().equals("")) {
                    presenter.studentLogIn(etName.getText().toString());
                }
                else showToast("Заполните поле ФИО");
                break;
            case R.id.btnReplace:
                presenter.onModeratorButtonClicked();
                break;
        }
    }
    public void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
