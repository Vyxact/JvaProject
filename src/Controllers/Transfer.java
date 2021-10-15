package Controllers;

import Public.LoginScreen;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class Transfer extends Login {
    private static final String transfer_query = "INSERT INTO transfers VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private TextField acc_to, amount;

    @FXML
    void accountBtn () throws IOException { LoginScreen.switcher("/Views/account.fxml"); }

    @FXML
    void depositBtn () throws IOException { LoginScreen.switcher("/Views/deposit.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { LoginScreen.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { LoginScreen.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { LoginScreen.switcher("/Views/settings.fxml"); }

    @FXML
    void logoutBtn () throws IOException {
        _depositID = _deposit = _depositBalance = _depositDate = _depositTime = null;
        _withdrawalID = _withdrawal = _withdrawalBalance = _withdrawalDate = _withdrawalTime = null;
        _transferID =  _accountFrom =  _accountTo =  _transferAmount =  _transferBalance =  _transferDate =  _transferTime = null;
        _historyMessage = _historyStatus =  _historyDate =  _historyTime = null;

        LoginScreen.switcher("/Views/login.fxml");
    }

    @FXML
    void proceed () {
        Connection conn = connect();

        String transfer_id = String.valueOf( UUID.randomUUID() );
        String id = String.valueOf( UUID.randomUUID() );
        String status = "Success";
        String from = _accountID;
        String to = acc_to.getText();
        double amnt = Double.parseDouble( amount.getText() );
        double bal = 0;
        String message = "Money transfer : $" + amnt + " to Account Number: " + to;

        final String balance_query = "SELECT * FROM accounts WHERE acc_id = '" + from + "'";

        try ( Statement fetch = conn.createStatement(); ResultSet res = fetch.executeQuery(balance_query) ) {
            while ( res.next() ) bal = res.getDouble(5);

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

            acc_to.clear(); amount.clear();
            System.out.println("Success"); // delete later

        } catch (SQLException err) { err.printStackTrace(); }
    }
}
