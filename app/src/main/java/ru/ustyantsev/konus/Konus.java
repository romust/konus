package ru.ustyantsev.konus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.ui.Activities.Administrator.AdministratorActivity;
import ru.ustyantsev.konus.ui.Activities.Login.LoginPresenter;
import ru.ustyantsev.konus.ui.Activities.Moderator.ModeratorActivity;
import ru.ustyantsev.konus.ui.Activities.Student.StudentActivity;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationDefaults;

import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.constants.NavigationIconType.CLOSE;
import static ru.ustyantsev.konus.utils.constants.NavigationIconType.DOWN;
import static ru.ustyantsev.konus.utils.constants.NavigationIconType.ENTER;
import static ru.ustyantsev.konus.utils.constants.NavigationIconType.UP;
import static ru.ustyantsev.konus.utils.constants.NavigationItemType.ACCOUNT;
import static ru.ustyantsev.konus.utils.constants.NavigationItemType.MESSAGES;
import static ru.ustyantsev.konus.utils.constants.NavigationItemType.SEARCH;
import static ru.ustyantsev.konus.utils.navigation.NavigationDefaults.NavigationDefaultsHolder.initDefaults;

public class Konus extends Application {
    private static Context appContext;
    Utils utils;

    @Override
    public void onCreate() {
        appContext = this;

        initDefaults(new NavigationDefaults()
                .navigationIcon(BACK, R.drawable.arrow_left)
                .navigationIcon(CLOSE, R.drawable.close)
                .navigationIcon(ENTER, R.drawable.subdirectory_arrow_left)
                .navigationIcon(UP, R.drawable.arrow_up)
                .navigationIcon(DOWN, R.drawable.arrow_down)
                .navigationItem(SEARCH, R.string.nav_item_search, R.drawable.search, R.color.colorPrimary)
                .navigationItem(ACCOUNT, R.string.nav_item_account, R.drawable.account, R.color.colorAccent)
                .navigationItem(MESSAGES, R.string.nav_item_messages, R.drawable.message_text, R.color.colorPrimaryDark)
                .defaultNavigationIconType(ENTER)
                .defaultBottomNavigationItem(ACCOUNT)
                .navigationIconListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        if (context instanceof Activity) {
                            ((Activity) context).onBackPressed();
                        }
                    }
                }));
        utils = new Utils(getApplicationContext());
        utils.updateUI();
        super.onCreate();


    }

    public Context appContext() {
        return appContext;
    }

}
