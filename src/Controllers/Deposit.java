package Controllers;

import Public.LoginScreen;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class Deposit extends Login {
    private static final String dep_query = "INSERT INTO deposits VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private TextField depot;

    @FXML
    void accountBtn () throws IOException { LoginScreen.switcher("/Views/account.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { LoginScreen.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transferBtn () throws IOException { LoginScreen.switcher("/Views/transfer.fxml"); }

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

        String transac_id = String.valueOf( UUID.randomUUID() );
        String id = String.valueOf(UUID.randomUUID());
        String status = "Success";
        String acc = _accountID;
        String cust = _customerID;
        double dep = Double.parseDouble( depot.getText() );
        double bal = 0;
        String message = "Money deposit: $" + dep;

        final String balance_query = "SELECT * FROM accounts WHERE acc_id = '" + acc + "'";

        try ( Statement _fetch = conn.createStatement() ) {
            ResultSet res = _fetch.executeQuery(balance_query);
            while ( res.next() )
                bal = res.getDouble(5);

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

            depot.clear();
            System.out.println("Success"); // delete later

        } catch (SQLException err) { err.getStackTrace(); }
    }
}
