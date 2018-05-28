package ru.ustyantsev.konus.ui.Activities;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.sheets.v4.SheetsScopes;

import com.google.api.services.sheets.v4.model.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ustyantsev.konus.Konus;
import ru.ustyantsev.konus.data.AddingRating;
import ru.ustyantsev.konus.ui.Activities.utils.Utils;
import ru.ustyantsev.konus.utils.Log;

public class Sheets {
    private GoogleAccountCredential mCredential;
    private Context context;
    private Utils utils;
    private FirebaseFirestore db;
    private List<List<Object>> values;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};

    public Sheets(Context context) {
        this.context = context;
        utils = new Utils(context);
        utils.initProgressDialog();
        Konus application = new Konus();
        mCredential = GoogleAccountCredential.usingOAuth2(
                application.appContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        db = FirebaseFirestore.getInstance();
        String accountName = context.getSharedPreferences("ui.Activities.Administrator.AdministratorActivity",Context.MODE_PRIVATE)
                .getString("accountName", null);
        mCredential.setSelectedAccountName(accountName);
    }

    public String generateSheetId(String reference) {
        String ref = "";
        int slash = 0;
        for (int i = 0; i < reference.length(); i++) {
            if (reference.charAt(i) == '/') {
                slash += 1;
            }
            if (slash == 5) {
                if (reference.charAt(i) != '/') {
                    ref += reference.charAt(i);
                }
            }
        }
        return ref;
    }

    /**
     * Синхронизирует данные в Google таблице с данными Firestore
     * путем удаления старых значений и добавления новых
     */
    public void updateSheet() {
        utils.showProgressDialog();
        SharedPreferences pref;
        pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String spreadsheetId = pref.getString("googleSheetReference", null);
            values = new ArrayList<>();
            db.collection("students")
                    .orderBy("points", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        int i = 0;
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            i += 1;
                            Object ratingArrayList[] = {String.valueOf(i),
                                    querySnapshot.getString("name"),
                                    querySnapshot.getString("group"),
                                    String.valueOf(querySnapshot.getLong("points"))};
                            values.add(Arrays.asList(ratingArrayList));
                        }
                        new TaskUpdate(mCredential, spreadsheetId, values).execute();
                    })
                    .addOnFailureListener(e -> Log.d("------error------"));

    }

    /**
     * Считывает данные из Таблицы и записывает их в Firestore и в google Sheet к уже имеющимся     *
     */
    public void addDataToRating(String spreadsheetId) {
        utils.showProgressDialog();
        new MakeRequestTask(mCredential, spreadsheetId).execute();
    }

    /**
     * Обнуляет баллы как в Firestore так и в Google Таблице
     * а также очищает все остальные коллекции в базе данных
     */
    public void setToZeroRating() {
        utils.showProgressDialog();
        db.collection("students").get()
                .addOnCompleteListener(task -> {
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("points", 0);
                    for (DocumentSnapshot querySnapshot : task.getResult()) {
                        db.collection("students").document(querySnapshot.getId()).set(docData, SetOptions.merge());
                    }
                    updateSheet();

                })
                .addOnFailureListener(e -> Log.d("------error------"));
    }

    public void deleteRating(){
        db.collection("students")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(document.getId() + " => " + document.getData());
                            db.collection("students").document(document.getId()).delete();
                        }
                        updateSheet();
                    } else {
                        Log.d("Error getting documents: " + task.getException());
                    }
                });
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService;
        String spreadsheetId;

        MakeRequestTask(GoogleAccountCredential credential, String spreadsheetId) {
            this.spreadsheetId = spreadsheetId;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Rating Konus")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                Log.d(e.getMessage());
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            AddingRating addingRating;
            String range = "B4:D";
            Log.d("1");
            List<String> results = new ArrayList<>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (values != null) {
                results.add("ФИО, Группа");
                Log.d("2");
                for (List row : values) {
                    addingRating = new AddingRating(row.get(0).toString(), row.get(1).toString(), Integer.parseInt(row.get(2).toString()));
                    db.collection("students").add(addingRating);
                    results.add(row.get(0) + ", " + row.get(1));
                }
            }
            return results;
        }


        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(List<String> output) {
            utils.hideProgressDialog();
            updateSheet();
            if (output == null || output.size() == 0) {
                utils.showToast("Добавленых данных нет");
            } else {
                utils.showToast("Данные успешно добавлены");
                //output.add(0, "Данные успешно добавлены");
                //utils.showToast(TextUtils.join("\n", output));
            }
        }
    }

    private class TaskUpdate extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService;
        String spreadsheetId;
        List<List<Object>> values;

        TaskUpdate(GoogleAccountCredential credential, String spreadsheetId, List<List<Object>> values) {
            this.values = values;
            this.spreadsheetId = spreadsheetId;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Rating Konus")
                    .build();

        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                getDataFromApi();
            } catch (Exception e) {
                Log.d(e.getMessage());
            }
            return null;
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         *
         * @return List of names and majors
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            String range = "A2:D";

            ClearValuesRequest requestBody = new ClearValuesRequest();
            mService.spreadsheets().values().clear(spreadsheetId, range, requestBody).execute();

            ValueRange body = new ValueRange()
                    .setValues(values);
            UpdateValuesResponse result =
                    this.mService.spreadsheets().values().update(spreadsheetId, range, body)
                            .setValueInputOption("USER_ENTERED")
                            .execute();
            Log.d("%d cells updated. - " + result.getUpdatedCells());
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<String> output) {
            utils.hideProgressDialog();
            utils.showToast("OK");
        }
    }
}