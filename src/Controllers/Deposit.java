package Controllers;

import Models.AlertMessage;
import Models.Initializer;
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

public class Deposit extends Login {
    private static final String dep_query = "INSERT INTO deposits (transac_id, acc_id, cust_id, deposit, balance) VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private Label account_user;

    @FXML
    private Text account_balance, balance_deposit, balance_withdrawal, message_status, message_account, message_deposit, message_balance, message_date, message_time;

    @FXML
    private TextField depot;

    @FXML
    void accountBtn () throws IOException { Switcher.switcher("/Views/account.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { Switcher.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transferBtn () throws IOException { Switcher.switcher("/Views/transfer.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { Switcher.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { Switcher.switcher("/Views/settings.fxml"); }

    @FXML
    private void logoutBtn () throws IOException, SQLException { disconnect(); Switcher.switcher("/Views/login.fxml"); }

    @Override
    public void initialize () throws SQLException { Initializer.init ( account_user, account_balance, balance_deposit, balance_withdrawal, connect() ); }

    @FXML
    void proceed () {
        Connection conn = connect();

        String transac_id = String.valueOf( UUID.randomUUID() );
        String id = String.valueOf(UUID.randomUUID());
        String status = "Success";
        String acc = _accountID;
        String cust = _customerID;
        double dep = Double.parseDouble( depot.getText() );
        double bal = 0;
        String message = "Money deposit: \t$" + dep;

        final String balance_query = "SELECT * FROM accounts WHERE acc_id = '" + acc + "'";

        if ( depot.getText().length() == 0 ) depot.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        else depot.setStyle(null);

        try ( Statement _fetch = conn.createStatement() ) {
            ResultSet res = _fetch.executeQuery(balance_query);
            while ( res.next() )
                bal = res.getDouble("balance");

            if ( dep > 0 ) {

                try ( PreparedStatement stmt = conn.prepareStatement(dep_query, Statement.RETURN_GENERATED_KEYS) ) {
                    stmt.setString(1, transac_id);
                    stmt.setString(2, acc);
                    stmt.setString(3, cust);
                    stmt.setDouble(4, dep);
                    stmt.setDouble(5, bal + dep);
                    stmt.executeUpdate();
                } catch (SQLException err) { err.getStackTrace(); }


                final String acc_query = "UPDATE accounts SET balance = balance + '" + dep + "' WHERE acc_id = '" + acc + "'";
                try ( Statement _stmt = conn.createStatement() ) { _stmt.executeUpdate(acc_query); } catch (SQLException err) { err.getStackTrace(); }


                try ( PreparedStatement _history_ = conn.prepareStatement(hist_query, Statement.RETURN_GENERATED_KEYS) ) {
                    _history_.setString(1, id);
                    _history_.setString(2, acc);
                    _history_.setString(3, message);
                    _history_.setString(4, status);
                    _history_.executeUpdate();
                } catch (SQLException err) { err.getStackTrace(); }

                Initializer.init ( account_user, account_balance, balance_deposit, balance_withdrawal, connect() );

                DateFormat date_format = new SimpleDateFormat("dd/MM/yy");
                Calendar cal1 = Calendar.getInstance();
                String date = date_format.format( cal1.getTime() );

                DateFormat time_format = new SimpleDateFormat("HH:mm:ss");
                Calendar cal2 = Calendar.getInstance();
                String time = time_format.format( cal2.getTime() );

                message_status.setStyle("-fx-fill: #60ee88;");

                message_status.setText("\t\t\t\tSuccess !");
                message_account.setText("\t\tAccount Number : \t" + acc);
                message_deposit.setText("\t\tDeposit : \t$" + dep);
                bal += dep;
                Withdrawal.message_alert (bal, date, time, message_balance, message_date, message_time, message_status, message_account, message_deposit);

            }
            else {
                message_status.setStyle("-fx-fill: #f66262;");
                message_status.setText("\t\t\t\tInvalid amount, please try again !");
                AlertMessage.message(message_status);
            }

            depot.clear();

        } catch (SQLException err) { err.getStackTrace(); }

    }

}
