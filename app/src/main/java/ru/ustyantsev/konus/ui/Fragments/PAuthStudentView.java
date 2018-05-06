package ru.ustyantsev.konus.ui.Fragments;

import android.content.Context;



public class PAuthStudentView {
    AuthStudentView view;
    public interface OnFragment1DataListener { //1.1 реализуем интерфейс с методом, который будет использовать активити
        void onFragment1DataListener();
    }
    private OnFragment1DataListener mListener; //1.2 создаем объект интерфейса
    public PAuthStudentView(AuthStudentView view){
        this.view = view;
    }

    public void onAttac(Context context) { //1.3 этап жизненного цикла фрагмента, в момент присоединения его к activity

        if (context instanceof OnFragment1DataListener) {//1.4 если activity имплементит интерфейс фрагмента
            mListener = (OnFragment1DataListener) context; //1.5 то используем объект интерфейса взятый у activity
        } else {
            throw new RuntimeException(context.toString() //1.6 иначе вызываем исключение и закрываем приложение
                    + " must implement OnFragment1DataListener");
        }

    }
    public void onButtonClicked(){
        mListener.onFragment1DataListener();//1.7 вызываем метод интерфейса, в activity
    }
}
