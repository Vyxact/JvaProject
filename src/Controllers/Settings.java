package Controllers;

import Models.Initializer;
import Models.RegistrationAlertMessage;
import Public.Switcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Settings extends Login {

    @FXML
    private Text message_status;

    @FXML
    private Label account_fullname, account_id, account_card, account_user, account_balance, account_creation;

    @FXML
    private TextField account_username, account_password;

    @FXML
    void accountBtn () throws IOException { Switcher.switcher("/Views/account.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { Switcher.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { Switcher.switcher("/Views/settings.fxml"); }

    @FXML
    private void logoutBtn () throws IOException, SQLException { disconnect(); Switcher.switcher("/Views/login.fxml"); }

    public void initialize () throws SQLException {
        account_user.setText(_username);
        account_fullname.setText(_firstname + " " + _lastname);
        account_id.setText(_accountID);
        account_card.setText(_cardNumber);

        Initializer.init_settings ( account_username, account_password, connect() );

        account_balance.setText("$" + _accountBalance);
        account_creation.setText(_accountDate);
    }

    @FXML
    void save_changes () throws SQLException {
        Connection conn = connect();
        String user = account_username.getText();
        String pass = account_password.getText();

        final String sql = "UPDATE customers SET username = '" + user + "', password = '" + pass + "' WHERE customer_id = '" + _customerID + "'";

        try ( Statement stmt = conn.createStatement() ) { stmt.executeUpdate(sql); } catch (SQLException err) { err.getStackTrace(); }

        Initializer.init_settings ( account_username, account_password, connect() );

        message_status.setStyle("-fx-fill: #125f25;");
        message_status.setText("Saved changes. \tusername : " + user + "\t\tpassword : " + pass);
        RegistrationAlertMessage.message(message_status);
    }
}
