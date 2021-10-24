package Controllers;

import Database.Connect;
import Models.LoginValidator;
import Models.RegistrationAlertMessage;
import Public.Switcher;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Login extends Connect {

    protected static String _accountID, _customerID, _cardNumber, _accountType, _accountBalance, _accountDate, _firstname, _lastname,
            _username, _password, _city, _contact, _depositID, _deposit, _depositBalance, _depositDate, _depositTime,
            _withdrawalID, _withdrawal, _withdrawalBalance, _withdrawalDate, _withdrawalTime, _transferID,
            _accountFrom, _accountTo, _transferAmount, _transferBalance, _transferDate, _transferTime,
            _historyMessage, _historyStatus, _historyDate, _historyTime;

    protected static double CASH_IN, CASH_OUT, WITHDRW, TRANS;

    @FXML
    private TextField username, password;

    @FXML
    void registerBtn () throws IOException { Switcher.switcher("/Views/register.fxml"); }

    @FXML
    private Text message_status;

    @FXML
    void login () {

        Connection conn = connect();
        String uname = username.getText();
        String pass = password.getText();

        LoginValidator.validate (uname, pass, username, password, message_status);

        if ( pass.length() >= 3 ) {

            final String SQL = "SELECT * FROM accounts a, customers c WHERE a.customer_id = c.customer_id AND username = '" + uname + "' AND password = '" + pass + "'";

            try ( Statement stmt = conn.createStatement() ) {
                ResultSet res = stmt.executeQuery(SQL);

                while ( res.next() ) {
                    _accountID = res.getString("acc_id");
                    _customerID = res.getString("customer_id");
                    _cardNumber = res.getString("card_number");
                    _accountType = res.getString("acc_type");
                    _accountBalance = res.getString("balance");
                    _accountDate = res.getString("creation_date");
                    _firstname = res.getString("firstname");
                    _lastname = res.getString("lastname");
                    _username = res.getString("username");
                    _password = res.getString("password");
                    _city = res.getString("city");
                    _contact = res.getString("contact");
                }

                if ( uname.equals(_username) && pass.equals(_password) ) Switcher.switcher("/Views/account.fxml");

                else { message_status.setStyle("-fx-fill: #f66262;"); message_status.setText("username or password is wrong or both."); RegistrationAlertMessage.message(message_status); }

            } catch (SQLException | IOException err) { err.getStackTrace(); }

        }

        else { message_status.setStyle("-fx-fill: #f66262;"); message_status.setText("password should be more than 3 characters long."); RegistrationAlertMessage.message(message_status); }

    }

}
