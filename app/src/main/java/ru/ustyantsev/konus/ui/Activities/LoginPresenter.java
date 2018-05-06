package ru.ustyantsev.konus.ui.Activities;


import ru.ustyantsev.konus.LoginModel;

public class LoginPresenter {
    private IloginView view;
    private LoginModel model;

    public LoginPresenter(IloginView view){
        this.view = view;
        model = new LoginModel();
    }
    public void onGetButtonClicked(){
        model.fireStore();
        view.showToast(model.toPlusTwo(view.getName()));
    }
}
