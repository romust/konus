package ru.ustyantsev.konus.ui.Activities.utils;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public interface SheetsPermissions {
    GoogleAccountCredential checkPermissionSheets();
}
