package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.UUID;

public class Deposit extends Login {
    private static final String dep_query = "INSERT INTO deposits VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private TextField depot;

    @FXML
    void proceed () {
        Connection conn = connect();

        String transac_id = String.valueOf(UUID.randomUUID());
        String id = String.valueOf(UUID.randomUUID());
        String status = "Success";
        String acc = "3ad79dcc-429b-4eb0-9c54-ad70ee1aa690";
        String cust = "3350e647-d23f-4f65-b067-f8b21f524777";
        double dep = Double.parseDouble( depot.getText() );
        double bal = 0;
        String message = "Money deposit: $" + dep + " - Account: " + acc;

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

            System.out.println("Success");

        } catch (SQLException err) { err.getStackTrace(); }
    }
}
