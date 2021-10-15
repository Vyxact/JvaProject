package Controllers;

import Database.Connect;
import Public.LoginScreen;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Login extends Connect {
    static String _accountID, _customerID, _cardNumber, _accountType, _accountBalance, _firstname, _lastname,
            _username, _city, _contact, _depositID, _deposit, _depositBalance, _depositDate, _depositTime,
            _withdrawalID, _withdrawal, _withdrawalBalance, _withdrawalDate, _withdrawalTime, _transferID,
            _accountFrom, _accountTo, _transferAmount, _transferBalance, _transferDate, _transferTime,
            _historyMessage, _historyStatus, _historyDate, _historyTime;

    static ArrayList<String> _history = new ArrayList<>();
    static ArrayList<String> _transactions = new ArrayList<>();

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    void registerBtn () throws IOException { LoginScreen.switcher("/Views/register.fxml"); }

    @FXML
    void loginBtn () throws IOException { LoginScreen.switcher("/Views/login.fxml"); }

    @FXML
    void login () {
        Connection conn = connect();
        String uname = username.getText();
        String pass = password.getText();

        final String SQL = "SELECT * FROM accounts a, customers c WHERE a.customer_id = c.customer_id AND username = '" + uname + "' AND password = '" + pass + "'";

        try (Statement stmt = conn.createStatement() ) {
            ResultSet res = stmt.executeQuery(SQL);

            while ( res.next() ) {
                _accountID = res.getString("acc_id");
                _customerID = res.getString("customer_id");
                _cardNumber = res.getString("card_number");
                _accountType = res.getString("acc_type");
                _accountBalance = res.getString("balance");
                _firstname = res.getString("firstname");
                _lastname = res.getString("lastname");
                _username = res.getString("username");
                _city = res.getString("city");
                _contact = res.getString("contact");
            }

            final String _SQL = "SELECT d.transac_id D_ID, d.deposit DEP, d.balance D_BALANCE, d.date D_DATE, d.time D_TIME, w.transac_id W_ID, " +
                                "w.withdrawal WITHD, w.balance W_BALANCE, w.date W_DATE, w.time W_TIME, t.transfer_id T_ID, t.acc_from T_FROM, t.acc_to T_TO, " +
                                "t.amount T_AMOUNT, t.balance T_BALANCE, t.date T_DATE, t.time T_TIME FROM deposits d FULL JOIN withdrawals w ON d.acc_id = w.acc_id " +
                                "FULL JOIN transfers t on d.acc_id = t.acc_from WHERE d.acc_id = '" + _accountID + "'";

            try ( Statement _stmt = conn.createStatement() ) {
                ResultSet rs = _stmt.executeQuery(_SQL);

                while ( rs.next() ) {
                    _depositID = rs.getString("D_ID");
                    _deposit = rs.getString("DEP");
                    _depositBalance = rs.getString("D_BALANCE");
                    _depositDate = rs.getString("D_DATE");
                    _depositTime = rs.getString("D_TIME");

                    _withdrawalID = rs.getString("W_ID");
                    _withdrawal = rs.getString("WITHD");
                    _withdrawalBalance = rs.getString("W_BALANCE");
                    _withdrawalDate = rs.getString("W_DATE");
                    _withdrawalTime = rs.getString("W_TIME");

                    _transferID = rs.getString("T_ID");
                    _accountFrom = rs.getString("T_FROM");
                    _accountTo = rs.getString("T_TO");
                    _transferAmount = rs.getString("T_AMOUNT");
                    _transferBalance = rs.getString("T_BALANCE");
                    _transferDate = rs.getString("T_DATE");
                    _transferTime = rs.getString("T_TIME");

                    String dep = "Deposit: $" + _deposit + " \tdate: " + _depositDate + "\ttime: " + _depositTime;
                    String with = "Withdrawal: $" + _withdrawal + " \tdate: " + _withdrawalDate + "\ttime: " + _withdrawalTime;
                    String trans = "Transferred $" + _transferAmount + " to " + _accountTo + " . current balance: " + _transferBalance;

                    if (_deposit != null) _transactions.addAll( Collections.singleton(dep) );
                    if (_withdrawal != null) _transactions.addAll( Collections.singleton(with) );
                    if (_transferAmount != null) _transactions.addAll( Collections.singleton(trans) );

                }

                final String __SQL = "SELECT * FROM history WHERE acc_id = '" + _accountID + "'";

                try {
                    Statement __stmt = conn.createStatement(); ResultSet r = __stmt.executeQuery(__SQL);

                    while ( r.next() ) {
                        _historyMessage = r.getString("message");
                        _historyStatus = r.getString("status");
                        _historyDate = r.getString("date");
                        _historyTime = r.getString("time");
                        _history.addAll( Collections.singleton(_historyMessage) );
                    }
                } catch (SQLException error) { error.printStackTrace(); }
            }

            LoginScreen.switcher("/Views/account.fxml");

        } catch (SQLException | IOException err) { err.getStackTrace(); }
    }
}
