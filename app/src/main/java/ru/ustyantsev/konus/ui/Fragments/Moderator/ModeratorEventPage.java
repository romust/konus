package ru.ustyantsev.konus.ui.Fragments.Moderator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;

import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class ModeratorEventPage extends NavigationFragment {
    public TextView tvDate, tvTime, tvTitle, tvPlace, tvInfo;
    Utils utils;
    FirebaseFirestore db;
    FloatingActionButton fab;
    Button btnParticipants, btnEstimate;
    FragmentReplacement fragmentReplace;


    public static ModeratorEventPage newInstance(Bundle args) {
        ModeratorEventPage fragment = new ModeratorEventPage();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentReplacement) {//1.4 если activity имплементит интерфейс фрагмента
            fragmentReplace = (FragmentReplacement) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        utils = new Utils(getActivity());
        tvDate = v.findViewById(R.id.event_page_date);
        tvTime = v.findViewById(R.id.event_page_time);
        tvTitle = v.findViewById(R.id.event_page_title);
        tvPlace = v.findViewById(R.id.event_page_place);
        tvInfo = v.findViewById(R.id.event_page_info);
        btnParticipants = v.findViewById(R.id.btn_participants);
        btnEstimate = v.findViewById(R.id.btn_estimate);
        tvTitle.setText(((Event) getArguments().getParcelable("event")).getTitle());
        tvPlace.setText(((Event) getArguments().getParcelable("event")).getPlace());
        tvDate.setText(((Event) getArguments().getParcelable("event")).getDate());
        tvTime.setText(((Event) getArguments().getParcelable("event")).getTime());
        tvInfo.setText(((Event) getArguments().getParcelable("event")).getInfo());
        btnParticipants.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putParcelable("event", getArguments().getParcelable("event"));
            fragmentReplace.fragmentReplacement(PresentList.newInstance(args));
        });
        btnEstimate.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString("docId", ((Event) getArguments().getParcelable("event")).getDocId());
            args.putBoolean("admin", false);
            fragmentReplace.fragmentReplacement(EstimateEdit.newInstance(args));
        });
        fab = v.findViewById(R.id.events_edit_fab);
        fab.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putParcelable("event", getArguments().getParcelable("event"));
            fragmentReplace.fragmentReplacement(ModeratorEventsEdit.newInstance(args));
        });
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.moderator_event_page)
                .includeToolbar()
                .toolbarTitle("Мероприятие")
                .toolbarNavigationIcon(BACK)
                .menuRes(R.menu.event_delete, buildGlobalActions());
    }
    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.event_delete, () -> {
                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                    ad.setMessage("Удалить это мероприятие?");
                    ad.setPositiveButton("Удалить", (dialog, arg1) -> {
                        DocumentReference ref = db.collection("events").document(((Event) getArguments().getParcelable("event")).getDocId());
                        ref.delete();
                        ref.collection("participants")
                                .get()
                                .addOnCompleteListener(task -> {
                                    for(DocumentSnapshot doc: task.getResult()){
                                        ref.collection("participants").document(doc.getId()).delete();
                                    }
                                });
                        getActivity().onBackPressed();
                    });
                    ad.setNegativeButton("Отмена", (dialog, arg1) -> {});
                    ad.show();
                })
                .build();
    }
}
