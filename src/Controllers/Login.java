package Controllers;

import Database.Connect;
import Models.LoginValidator;
import Models.RegistrationAlertMessage;
import Models.TransactionsHistory;
import Public.Switcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Login extends Connect {
    protected static String _accountID, _customerID, _cardNumber, _accountType, _accountBalance, _accountDate, _firstname, _lastname,
            _username, _password, _city, _contact, _depositID, _deposit, _depositBalance, _depositDate, _depositTime,
            _withdrawalID, _withdrawal, _withdrawalBalance, _withdrawalDate, _withdrawalTime, _transferID,
            _accountFrom, _accountTo, _transferAmount, _transferBalance, _transferDate, _transferTime,
            _historyMessage, _historyStatus, _historyDate, _historyTime;

    static double CASH_IN, CASH_OUT, WITH, TRANS;
    static ArrayList<String> _history = new ArrayList<>();
    protected static ArrayList<String> _transactions = new ArrayList<>();
    public static ObservableList<TransactionsHistory> HistoryList = FXCollections.observableArrayList();

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

        LoginValidator.validate(uname, pass, username, password, message_status);

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

                if ( uname.equals(_username) && pass.equals(_password) ) {

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

                        if (_deposit != null) {
                            _transactions.addAll( Collections.singleton(dep) );

                            HistoryList.add ( new TransactionsHistory( _depositID, "Deposit", _deposit, _depositBalance, _depositDate, _depositTime) );
                        }

                        if (_withdrawal != null) {
                            _transactions.addAll( Collections.singleton(with) );

                            HistoryList.add ( new TransactionsHistory( _withdrawalID, "Withdrawal", _withdrawal, _withdrawalBalance, _withdrawalDate, _withdrawalTime) );
                        }
                        if (_transferAmount != null) {
                            _transactions.addAll( Collections.singleton(trans) );

                            HistoryList.add ( new TransactionsHistory( _transferID, "Transfer", _transferAmount, _transferBalance, _transferDate, _transferTime) );
                        }

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


                    final String cash_in = "SELECT SUM(deposit) AS CASH_IN FROM deposits WHERE acc_id = '" + _accountID + "'";
                    try ( ResultSet r = conn.createStatement().executeQuery(cash_in) ) { while ( r.next() ) CASH_IN = r.getDouble("CASH_IN"); }
                    catch (SQLException error) { error.printStackTrace(); }

                    final String w = "SELECT SUM(withdrawal) AS WITH FROM withdrawals WHERE acc_id = '" + _accountID + "'";
                    try ( ResultSet r = conn.createStatement().executeQuery(w) ) { while ( r.next() ) WITH = r.getDouble("WITH"); }
                    catch (SQLException error) { error.printStackTrace(); }

                    final String t = "SELECT SUM(amount) AS TRANS FROM transfers WHERE acc_from = '" + _accountID + "'";
                    try ( ResultSet r = conn.createStatement().executeQuery(t) ) { while ( r.next() ) TRANS = r.getDouble("TRANS"); }
                    catch (SQLException error) { error.printStackTrace(); }

                    CASH_OUT = WITH + TRANS;
                }

                Switcher.switcher("/Views/account.fxml");

                }
                else {
                    message_status.setStyle("-fx-fill: #f66262;");
                    message_status.setText("username or password is wrong or both.");
                    RegistrationAlertMessage.message(message_status);
                }

            } catch (SQLException | IOException err) { err.getStackTrace(); }

        }
        else {
            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("password should be more than 3 characters long.");
            RegistrationAlertMessage.message(message_status);
        }

    }
}
