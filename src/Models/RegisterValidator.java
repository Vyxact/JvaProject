package Models;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public interface RegisterValidator {

    static void validate (String fname, String lname, String user, String pass, String cty, String contct, TextField firstname, TextField lastname, TextField username, TextField password, TextField city, TextField contact, Text message_status) {

        fname(fname, lname, firstname, lastname, message_status);

        if ( user.length() == 0 ) {
            username.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("field cannot be empty.");
            AlertMessage.message(message_status);
        }
        else username.setStyle(null);

        if ( pass.length() < 8 ) {

            if ( pass.length() == 0 ) {
                password.setStyle("-fx-border-color: red; -fx-border-width: 2px");

                message_status.setStyle("-fx-fill: #f66262;");
                message_status.setText("field cannot be empty.");
                AlertMessage.message(message_status);
            }
            else password.setStyle(null);

            password.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("password must be more than 8 characters.");
            AlertMessage.message(message_status);
        }
        else password.setStyle(null);

        fname(cty, contct, city, contact, message_status);

    }

    private static void fname (String fname, String lname, TextField firstname, TextField lastname, Text message_status) {
        if ( fname.length() == 0 ) {
            firstname.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("field cannot be empty.");
            AlertMessage.message(message_status);
        }
        else firstname.setStyle(null);

        if ( lname.length() == 0 ) {
            lastname.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("field cannot be empty.");
            RegistrationAlertMessage.message(message_status);
        }
        else lastname.setStyle(null);
    }

}
