package Models;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public interface LoginValidator {

    static void validate (String uname, String pass, TextField username, TextField password, Text message_status) {

        if ( uname.length() == 0 ) {
            username.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("username is required!");
            RegistrationAlertMessage.message(message_status);
        }
        else username.setStyle(null);

        if ( pass.length() < 3 ) {
            password.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("password is required!");
            RegistrationAlertMessage.message(message_status);
        }
        else password.setStyle(null);

    }
}
