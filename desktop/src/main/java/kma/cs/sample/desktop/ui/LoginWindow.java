package kma.cs.sample.desktop.ui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.desktop.exception.LoginException;
import kma.cs.sample.desktop.services.UserService;

public class LoginWindow {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField messageField;

    public void processLogin() {
        System.out.println("Process login");

        try {
            GlobalContext.AUTHENTICATED_USER = UserService.login(loginField.getText(), passwordField.getText());
            messageField.setText("Logged in successfully");
        } catch (final LoginException ex) {
            messageField.setText(ex.getErrorResponse().getMessage());
        }
    }

}
