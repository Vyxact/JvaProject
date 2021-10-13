package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.UUID;

public class Withdrawal extends Login {
    private static final String with_query = "INSERT INTO withdrawals VALUES (?::uuid, ?::uuid, ?::uuid, ?::numeric, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";

    @FXML
    private TextField withdrw;

    @FXML
    void proceed () {
        Connection conn = connect();

        String transac_id = String.valueOf(UUID.randomUUID());
        String id = String.valueOf( UUID.randomUUID() );
        String status = "Success";
        String acc = "57cb1685-e6dd-43a5-9097-3205cf26aa44";
        String cust = "6ac9f75d-3720-4d48-aaeb-acf219524fc7";
        double with = Double.parseDouble( withdrw.getText() );
        double bal = 0;
        String message = "Money withdrawal: $" + with + " - Account: " + acc;

        final String balance_query = "SELECT * FROM accounts WHERE acc_id = '" + acc + "'";

        try ( Statement _fetch = conn.createStatement() ) {
            ResultSet res = _fetch.executeQuery(balance_query);
            while (res.next())
                bal = res.getDouble(5);

            try ( PreparedStatement stmt = conn.prepareStatement(with_query, Statement.RETURN_GENERATED_KEYS) ) {
                stmt.setString(1, transac_id);
                stmt.setString(2, acc);
                stmt.setString(3, cust);
                stmt.setDouble(4, with);
                stmt.setDouble(5, bal - with);
                stmt.executeUpdate();
            } catch (SQLException err) { err.getStackTrace(); }


            final String query = "UPDATE accounts SET balance = balance - '" + with + "' WHERE acc_id = '" + acc + "'";
            try ( Statement stmt = conn.createStatement() ) { stmt.executeUpdate(query); } catch (SQLException err) { err.printStackTrace(); }


            try ( PreparedStatement _history_ = conn.prepareStatement(hist_query, Statement.RETURN_GENERATED_KEYS) ) {
                _history_.setString(1, id);
                _history_.setString(2, acc);
                _history_.setString(3, message);
                _history_.setString(4, status);
                _history_.executeUpdate();
            } catch (SQLException err) { err.printStackTrace(); }

            System.out.println("Success");

        } catch (SQLException err) { err.printStackTrace(); }
    }
}
