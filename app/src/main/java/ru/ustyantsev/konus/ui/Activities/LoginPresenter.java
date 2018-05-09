package ru.ustyantsev.konus.ui.Activities;


import ru.ustyantsev.konus.LoginModel;

public class LoginPresenter {
    private LoginView view;
    private LoginModel model;

    public LoginPresenter(LoginView view){
        this.view = view;
        model = new LoginModel();
    }
    public void onGetButtonClicked(){
        model.fireStore();
        //view.showToast(model.toPlusTwo(view.getName()));
    }
}
