package ru.ustyantsev.konus.ui.Fragments.ModerateLogin;

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

    final static String TAG = "ooo";
    EditText etEmail, etPassword;
    Button btnLogin;
    String email, password;

    @Override
    public void onAttach(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity
        super.onAttach(context);
        presenter = new ModerateLoginPresenter(this, getActivity());
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
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        presenter.setData(email, password);
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}