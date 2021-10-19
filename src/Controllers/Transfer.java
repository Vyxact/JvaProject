package Controllers;

import Models.AlertMessage;
import Models.RegistrationAlertMessage;
import Public.Switcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Transfer extends Login {
    private static final String transfer_query = "INSERT INTO transfers VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private TextField acc_to, amount;

    @FXML
    private Label account_user;

    @FXML
    private Text account_balance, balance_deposit, balance_withdrawal, message_status, message_account, mess_transfer_to, message_deposit, message_balance, message_date, message_time;

    @FXML
    void accountBtn () throws IOException { Switcher.switcher("/Views/account.fxml"); }

    @FXML
    void depositBtn () throws IOException { Switcher.switcher("/Views/deposit.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { Switcher.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { Switcher.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { Switcher.switcher("/Views/settings.fxml"); }

    @FXML
    void logoutBtn () throws IOException {
        _depositID = _deposit = _depositBalance = _depositDate = _depositTime = null;
        _withdrawalID = _withdrawal = _withdrawalBalance = _withdrawalDate = _withdrawalTime = null;
        _transferID =  _accountFrom =  _accountTo =  _transferAmount =  _transferBalance =  _transferDate =  _transferTime = null;
        _historyMessage = _historyStatus =  _historyDate =  _historyTime = null;

        Switcher.switcher("/Views/login.fxml");
    }

    @Override
    public void initialize () {
        account_user.setText(_username);
        account_balance.setText("$" + _accountBalance);
        balance_deposit.setText("+$" + CASH_IN);
        balance_withdrawal.setText("-$" + CASH_OUT);
    }

    @FXML
    void proceed () {
        Connection conn = connect();

        String d = "dd/MM/yy";
        Calendar _c;
        DateFormat df = new SimpleDateFormat(d);
        _c = Calendar.getInstance();
        String dt;
        dt = df.format( _c.getTime() );

        DateFormat time_format = new SimpleDateFormat("HH:mm:ss");
        Calendar cal2 = Calendar.getInstance();
        String time = time_format.format( cal2.getTime() );

        String transfer_id = String.valueOf( UUID.randomUUID() );
        String id = String.valueOf( UUID.randomUUID() );
        String status = "Success";
        String from = _accountID;
        String to = acc_to.getText();
        double amnt = Double.parseDouble( amount.getText() );
        double bal = 0;
        String message = "Money transfer : $" + amnt + " to Account Number: " + to;

        final String balance_query = "SELECT * FROM accounts WHERE acc_id = '" + from + "'";

        if ( to.length() == 0 ) {
            acc_to.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("this field is required!");
            RegistrationAlertMessage.message(message_status);
        }
        else acc_to.setStyle(null);

        try ( Statement fetch = conn.createStatement(); ResultSet res = fetch.executeQuery(balance_query) ) {
            while ( res.next() ) bal = res.getDouble(5);

            if ( amnt > 0 && bal > 0 && bal >= amnt ) {

                try ( PreparedStatement stmt = conn.prepareStatement(transfer_query, Statement.RETURN_GENERATED_KEYS) ) {
                    stmt.setString(1, transfer_id);
                    stmt.setString(2, from);
                    stmt.setString(3, to);
                    stmt.setDouble(4, amnt);
                    stmt.setDouble(5, bal - amnt);
                    stmt.executeUpdate();
                } catch (SQLException error) { error.printStackTrace(); }


                final String deduction = "UPDATE accounts SET balance = balance - '" + amnt + "' WHERE acc_id = '" + from + "'";
                try ( Statement stmt = conn.createStatement() ) { stmt.executeUpdate(deduction); } catch (SQLException err) { err.printStackTrace(); }

                final String addition = "UPDATE accounts SET balance = balance + '" + amnt + "' WHERE acc_id = '" + to + "'";
                try { Statement stmt = conn.createStatement(); stmt.executeUpdate(addition); } catch (SQLException err) { err.printStackTrace(); }


                try ( PreparedStatement _history_ = conn.prepareStatement(hist_query, Statement.RETURN_GENERATED_KEYS) ) {
                    _history_.setString(1, id);
                    _history_.setString(2, from);
                    _history_.setString(3, message);
                    _history_.setString(4, status);
                    _history_.executeUpdate();
                } catch (SQLException err) { err.getCause(); }

                message_status.setStyle("-fx-fill: #60ee88;");

                message_status.setText("\t\t\t\tSuccess !");
                message_account.setText("\t\tAccount Number : " + from);
                mess_transfer_to.setText("\t\tTransfer to : " + to);
                message_deposit.setText("\t\tDeposit : $" + amnt);
                AlertMessage.message(mess_transfer_to);
                bal -= amnt;
                Withdrawal.message_alert(bal, dt, time, message_balance, message_date, message_time, message_status, message_account, message_deposit);

            }
            else {
                message_status.setStyle("-fx-fill: #f66262;");
                message_status.setText("\t\t\t\tSorry, you don't have sufficient funds to proceed with this transaction !");
                AlertMessage.message(message_status);
            }

            acc_to.clear(); amount.clear();

        } catch (SQLException err) { err.printStackTrace(); }
    }
}
