package ru.ustyantsev.konus.ui.Fragments.Student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.Event;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;

import static ru.ustyantsev.konus.utils.constants.NavigationIconType.BACK;
import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;

public class StudentEventPage extends NavigationFragment {
    public TextView tvDate, tvTime, tvTitle, tvPlace, tvInfo;
    Utils utils;

    public static StudentEventPage newInstance(Bundle args) {
        StudentEventPage fragment = new StudentEventPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        utils = new Utils(getActivity());
        tvDate = v.findViewById(R.id.event_page_date);
        tvTime = v.findViewById(R.id.event_page_time);
        tvTitle = v.findViewById(R.id.event_page_title);
        tvPlace = v.findViewById(R.id.event_page_place);
        tvInfo = v.findViewById(R.id.event_page_info);
        tvTitle.setText(((Event) getArguments().getParcelable("event")).getTitle());
        tvPlace.setText(((Event) getArguments().getParcelable("event")).getPlace());
        tvDate.setText(((Event) getArguments().getParcelable("event")).getDate());
        tvTime.setText(((Event) getArguments().getParcelable("event")).getTime());
        tvInfo.setText(((Event) getArguments().getParcelable("event")).getInfo());
        Log.d(String.valueOf((Long) getArguments().getLong("dateTime")));
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.student_event_page)
                .includeToolbar()
                .toolbarNavigationIcon(BACK);
    }

}
