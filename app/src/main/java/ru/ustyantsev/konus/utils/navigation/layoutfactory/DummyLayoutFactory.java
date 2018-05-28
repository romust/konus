package ru.ustyantsev.konus.utils.navigation.layoutfactory;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class DummyLayoutFactory implements LayoutFactory {
    private final View view;

    public DummyLayoutFactory(View view) {
        this.view = view;
    }

    @Override
    public View produceLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        return view;
    }
}
