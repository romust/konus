package ru.ustyantsev.konus.ui.Fragments.AdministratorActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Sheets;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;

public class AdminSettings extends Fragment implements View.OnClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_settings, null);
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

    @Override
    public void onClick(View view) {

    }
}

