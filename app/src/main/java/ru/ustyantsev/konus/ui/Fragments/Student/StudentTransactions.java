package ru.ustyantsev.konus.ui.Fragments.Student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ru.ustyantsev.konus.R;
import ru.ustyantsev.konus.data.RecyclerAdapters.EstimatesAdapter;
import ru.ustyantsev.konus.data.RecyclerAdapters.TransactionsAdapter;
import ru.ustyantsev.konus.data.Transaction;
import ru.ustyantsev.konus.ui.Activities.utils.FragmentReplacement;
import ru.ustyantsev.konus.ui.Fragments.Moderator.EstimateEdit;
import ru.ustyantsev.konus.utils.Log;
import ru.ustyantsev.konus.utils.navigation.NavigationBuilder;
import ru.ustyantsev.konus.utils.navigation.NavigationDefaults;
import ru.ustyantsev.konus.utils.navigation.NavigationFragment;

import static ru.ustyantsev.konus.utils.navigation.AutoLayoutNavigationBuilder.navigation;
import static ru.ustyantsev.konus.utils.navigation.NavigationBuilder.NO_NAV_ICON;

public class StudentTransactions extends NavigationFragment {
    private RecyclerView recyclerView;
    private TransactionsAdapter adapter;
    ArrayList<Transaction> transactionsArrayList;
    FirebaseFirestore db;
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
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        transactionsArrayList = new ArrayList<>();
        setUpRecyclerView(v);
        setUpFireBase();
        loadData();
        NavigationDefaults.NavigationDefaultsHolder.navigationDefaults()
                .navigationIconListener(view -> getActivity().onBackPressed());
    }

    @Override
    protected NavigationBuilder buildNavigation() {
        return navigation(R.layout.student_transactions)
                .includeToolbar()
                .toolbarTitle("История транзакций")
                .toolbarNavigationIcon(NO_NAV_ICON);
    }

    private void loadData() {
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences("student", Context.MODE_PRIVATE);
        db.collection("students").document(pref.getString("docId", null)).collection("transactions")
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Transaction transaction = new Transaction(
                                doc.getString("title"),
                                doc.getString("info"),
                                String.valueOf(doc.getLong("points")));
                        transactionsArrayList.add(transaction);
                    }
                    adapter = new TransactionsAdapter();
                    recyclerView.setAdapter(adapter);
                    adapter.addAll(transactionsArrayList);
                })
                .addOnFailureListener(e -> Log.d("------error------"));
    }

    private void setUpFireBase() {
        db = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView(View v) {
        recyclerView = v.findViewById(R.id.transactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
