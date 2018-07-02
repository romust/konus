package ru.ustyantsev.konus.ui.Fragments.Moderator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.AddingEvent;

import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.data.Rating;
import ru.ustyantsev.konus.data.RecyclerAdapters.StudentRatingAdapter;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;

import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;
import ru.ustyantsev.konus.utils.navigation.menu.MenuActions;


import static ru.ustyantsev.konus.utils.constants.NavigationIconType.CLOSE;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class ModeratorEventsEdit extends NavigationFragment {
    TextView etDate, etTime, etTitle, etPlace, etInfo;
    String title = "", place = "", date = "Дата", time = "Время", info = "", docId;
    Calendar dateAndTime = Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Utils utils;
    Bundle args;
    MenuItem eventEditBtn;


    public static ModeratorEventsEdit newInstance(Bundle args) {
        ModeratorEventsEdit fragment = new ModeratorEventsEdit();
        fragment.setArguments(args);
        fragment.args = args;
        return fragment;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        utils = new Utils(getActivity());
        utils.initProgressDialog();
        etDate = v.findViewById(R.id.event_edit_date);
        etTime = v.findViewById(R.id.event_edit_time);
        etTitle = v.findViewById(R.id.event_edit_title);
        etPlace = v.findViewById(R.id.event_edit_place);
        etInfo = v.findViewById(R.id.event_edit_info);
        etDate.setOnClickListener(this::setDate);
        etTime.setOnClickListener(this::setTime);
        eventEditBtn = v.findViewById(R.id.event_edit_btn);
        if(getArguments()!=null) {
            invalidateNavigation(changeNavigation());
            title = ((Event) args.getParcelable("event")).getTitle();
            place = ((Event) args.getParcelable("event")).getPlace();
            date = ((Event) args.getParcelable("event")).getDate();
            time = ((Event) args.getParcelable("event")).getTime();
            info = ((Event) args.getParcelable("event")).getInfo();
            docId = ((Event) args.getParcelable("event")).getDocId();
            dateAndTime.setTimeInMillis(((Event) args.getParcelable("event")).getDateTime());
            setInitialDate();
            setInitialTime();
            etTitle.setText(title);
            etPlace.setText(place);
            etInfo.setText(info);
        }
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.moderator_events_edit)
                .includeToolbar()
                .toolbarNavigationIcon(CLOSE)
                .menuRes(R.menu.edit, buildGlobalActions());
    }

    private NavigationBuilder changeNavigation(){
        return navigation(R.layout.moderator_events_edit)
                .includeToolbar()
                .toolbarNavigationIcon(CLOSE)
                .menuRes(R.menu.change_save, buildGlobalActions());
    }

    private MenuActions buildGlobalActions() {
        return new MenuActions.Builder()
                .action(R.id.event_edit_btn, () -> {
                    utils.showProgressDialog();
                        if(args == null) {
                            if (!etTitle.getText().toString().equals(title) &&
                                    !etPlace.getText().toString().equals(place) &&
                                    !etInfo.getText().toString().equals(info) &&
                                    !etDate.getText().toString().equals(date) &&
                                    !etTime.getText().toString().equals(time)) {
                                Timestamp timestamp = new Timestamp(dateAndTime.getTimeInMillis() / 1000, 0);
                                AddingEvent event = new AddingEvent(etTitle.getText().toString(),
                                        etPlace.getText().toString(),
                                        etInfo.getText().toString(),
                                        timestamp);
                                    DocumentReference docRef = db.collection("events").document();
                                    String id = docRef.getId();
                                    db.collection("events").document(id).set(event).addOnSuccessListener(aVoid -> {

                                        db.collection("students").get()
                                                .addOnCompleteListener(task -> {

                                                    for(DocumentSnapshot doc: task.getResult()){
                                                        //DocumentReference reference = db.collection("students").document(doc.getId());
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("name", doc.getString("name"));
                                                        data.put("group", doc.getString("group"));
                                                        data.put("present", false);
                                                        db.collection("events").document(id).collection("participants").document().set(data);
                                                    }
                                                    utils.hideProgressDialog();
                                                    utils.showToast("Мероприятие создано");
                                                    getActivity().onBackPressed();
                                                })
                                                .addOnFailureListener(e -> Log.d("------error------"));
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("title", etTitle.getText().toString());
                                        data.put("sent", false);
                                        data.put("approved", false);
                                        data.put("present", 0);
                                        data.put("missing", 0);
                                        db.collection("estimates").document(id).set(data);
                                    });

                            } else {
                                utils.hideProgressDialog();
                                utils.showToast("Все поля должны быть заполнены");
                            }
                        } else {
                            if (!etTitle.getText().toString().equals(title) ||
                                    !etPlace.getText().toString().equals(place) ||
                                    !etInfo.getText().toString().equals(info) ||
                                    !etDate.getText().toString().equals(date) ||
                                    !etTime.getText().toString().equals(time)) {
                                Timestamp timestamp = new Timestamp(dateAndTime.getTimeInMillis() / 1000, 0);
                                AddingEvent event = new AddingEvent(etTitle.getText().toString(),
                                        etPlace.getText().toString(),
                                        etInfo.getText().toString(),
                                        timestamp);
                                    db.collection("events").document(docId).set(event);
                                    utils.hideProgressDialog();
                                    utils.showToast("Изменения прошли успешно");
                                    getActivity().onBackPressed();
                                getActivity().onBackPressed();
                            } else {
                                utils.hideProgressDialog();
                                utils.showToast("Изменений нет");
                            }
                        }
                })
                .build();
    }

    public void setDate(View v) {
        new DatePickerDialog(getActivity(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime(View v) {
        new TimePickerDialog(getActivity(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    private void setInitialDate() {

        etDate.setText(DateUtils.formatDateTime(getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    private void setInitialTime() {
        etTime.setText(DateUtils.formatDateTime(getActivity(),
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
    }

    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDate();
    };
    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);
        setInitialTime();
    };
}
