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

public class ModerateLoginView extends Fragment implements View.OnClickListener {
    public ModerateLoginPresenter presenter;
    EditText etEmail, etPassword;
    Button btnLogin;

    @Override
    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        super.onAttach(context);
        presenter = new ModerateLoginPresenter(this, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auth_moderate, null);
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        btnLogin = v.findViewById(R.id.btnModerateLogin);
        btnLogin.setOnClickListener(this);
        presenter.onCreateView();
        return v;
    }

    @Override
    public void onClick(View view) {
        presenter.hideKeyboard(view);
        if(!etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            presenter.moderateLogIn(etEmail.getText().toString(), etPassword.getText().toString());
        }
        else{
            showToast("Заполните все поля");
        }
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
