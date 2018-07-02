package ru.ustyantsev.konus.ui.Fragments.Administrator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.ui.Activities.Login.LoginView;
import ru.ustyantsev.konus.ui.Activities.Sheets;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class AdminSettings extends NavigationFragment implements View.OnClickListener {
    private final MenuActions globalMenuActions = buildGlobalActions();
    EditText etSyncSheet, etAddDataToRating;
    Button btnEditEtSyncSheet, btnSetToZero, btnDeleteRating;
    Button btnAddDataToRating;
    SharedPreferences pref;
    Sheets sheets;


    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.admin_settings)
                .includeToolbar()
                .toolbarTitle("Настройки")
                .toolbarNavigationIcon(NO_NAV_ICON)
                .menuRes(R.menu.exit, globalMenuActions);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSyncSheet = view.findViewById(R.id.etSyncSheet);
        etAddDataToRating = view.findViewById(R.id.etAddDataToRating);
        btnEditEtSyncSheet = view.findViewById(R.id.btnEditEtSyncSheet);
        btnAddDataToRating = view.findViewById(R.id.btnAddDataToRating);
        btnDeleteRating = view.findViewById(R.id.btnDeleteRating);
        btnSetToZero = view.findViewById(R.id.btnSetToZero);
        sheets = new Sheets(getActivity());

        btnEditEtSyncSheet.setOnClickListener(this);
        btnAddDataToRating.setOnClickListener(this);
        btnSetToZero.setOnClickListener(this);
        btnDeleteRating.setOnClickListener(this);

    }

    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.exit, ()->{
                    FirebaseAuth.getInstance().signOut();
                    Intent intent2 = new Intent(getActivity(), LoginView.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                })
                .build();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnEditEtSyncSheet:
                if(!etSyncSheet.isEnabled()){
                    etSyncSheet.setEnabled(true);
                } else {
                    etSyncSheet.setEnabled(false);
                    pref = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("googleSheetReference", sheets.generateSheetId(etSyncSheet.getText().toString()));
                    editor.commit();
                    sheets.updateSheet();
                }
                break;
            case R.id.btnAddDataToRating:
                if(!etAddDataToRating.getText().toString().equals("")) {
                    sheets.addDataToRating(sheets.generateSheetId(etAddDataToRating.getText().toString()));
                    break;
                } else break;
            case R.id.btnSetToZero:
                sheets.setToZeroRating();
                break;
            case R.id.btnDeleteRating:
                sheets.deleteRating();
        }
    }
}





