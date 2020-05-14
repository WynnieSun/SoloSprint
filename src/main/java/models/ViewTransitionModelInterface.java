package models;

import views.MainController;

public interface ViewTransitionModelInterface {

	public void showPersonInfo();
	public void showBPlistView(ViewTransitionModelInterface vm);
	public void showEmptyBPView();
	public void logout();
	public void showLoginPage(MainController cont);

}
