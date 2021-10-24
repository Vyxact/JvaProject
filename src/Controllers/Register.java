package Controllers;

import Database.Connect;
import Models.AlertMessage;
import Models.RegisterValidator;
import Public.Switcher;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.UUID;


public class Register extends Connect {
    static String _branchID = null;
    static int count;

    StringBuilder account_number () {
        Random account_number = new Random();
        StringBuilder card = new StringBuilder("ACC");

        for (int i = 0; i < 14; i++) {
            int n = account_number.nextInt(10);
            card.append(n);
        }

        return card;
    }

    public void initialize () {
        acc_type.getItems().addAll(account_type);
    }

    private String get_type () {
        return acc_type.getValue();
    }

    private final String[] account_type = { "Savings", "Regular" };
    private static final String cust_query = "INSERT INTO customers VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar)";
    private static final String acc_query = "INSERT INTO accounts VALUES (?::uuid, ?::uuid, ?::text, ?::varchar, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar)";
    private static final String fetch_query = "SELECT * FROM branches LIMIT 1";

    @FXML
    private Text message_status;

    @FXML
    private TextField balance, firstname, lastname, username, password, city, contact;

    @FXML
    private ChoiceBox<String> acc_type;

    @FXML
    void loginBtn () throws IOException { Switcher.switcher("/Views/login.fxml"); }

    @FXML
    void register () {
        Connection conn = connect();

        //        ------------------------------------------------------
        String cust = String.valueOf( UUID.randomUUID() );
        String fname = firstname.getText();
        String lname = lastname.getText();
        String user = username.getText();
        String pass = password.getText();
        String cty = city.getText();
        String contct = contact.getText();

        final String SQL = "SELECT COUNT(*) AS count FROM customers WHERE username = '" + user + "'";

        RegisterValidator.validate(fname, lname, pass, cty, contct, firstname, lastname, password, city, contact, message_status);

        if ( user.length() == 0 ) {
            username.setStyle("-fx-border-color: red; -fx-border-width: 2px");

            message_status.setStyle("-fx-fill: #f66262;");
            message_status.setText("field cannot be empty.");
            AlertMessage.message(message_status);
        }
        else {
            username.setStyle(null);

            try ( Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SQL) ) {

                while ( rs.next() ) count = rs.getInt("count");

                if (count > 0) {
                    message_status.setStyle("-fx-fill: #f66262;");
                    message_status.setText("This username is already taken.");
                    AlertMessage.message(message_status);
                }
                else {
                    try ( Statement _fetch = conn.createStatement() ) {
                        ResultSet res = _fetch.executeQuery(fetch_query);
                        while ( res.next() ) _branchID = res.getString(1);

                        try ( PreparedStatement _stmt = conn.prepareStatement(cust_query, Statement.RETURN_GENERATED_KEYS) ) {
                            _stmt.setString(1, cust);
                            _stmt.setString(2, _branchID);
                            _stmt.setString(3, fname);
                            _stmt.setString(4, lname);
                            _stmt.setString(5, user);
                            _stmt.setString(6, pass);
                            _stmt.setString(7, cty);
                            _stmt.setString(8, contct);
                            _stmt.executeUpdate();

                            //================================================================================================================================//
                            //================================================================================================================================//
                            //================================================================================================================================//
                            //================================================================================================================================//

                            String acc = String.valueOf( UUID.randomUUID() );
                            String card = String.valueOf( account_number() );
                            String type = get_type();
                            double amount = Double.parseDouble( balance.getText() );

                            try ( PreparedStatement stmt_ = conn.prepareStatement(acc_query, Statement.RETURN_GENERATED_KEYS) ) {
                                stmt_.setString(1, acc);
                                stmt_.setString(2, cust);
                                stmt_.setString(3, card);
                                stmt_.setString(4, type);
                                stmt_.setDouble(5, amount);
                                stmt_.executeUpdate();

                                //================================================================================================================================//
                                //================================================================================================================================//
                                //================================================================================================================================//
                                //================================================================================================================================//

                                String id = String.valueOf( UUID.randomUUID() );
                                String status = "Success";
                                String message = "Account creation.\n Full Name: " + fname + " " + lname + "\nAccount Type: " + type + "\nOpening balance: $" + amount;

                                try ( PreparedStatement _history_ = conn.prepareStatement(hist_query, Statement.RETURN_GENERATED_KEYS) ) {
                                    _history_.setString(1, id);
                                    _history_.setString(2, acc);
                                    _history_.setString(3, message);
                                    _history_.setString(4, status);
                                    _history_.executeUpdate();
                                }

                            } catch (SQLException err) { err.getStackTrace(); }

                        } catch (SQLException err) { err.getStackTrace(); }

                    } catch (SQLException err) { err.getStackTrace(); }

                    Switcher.switcher("/Views/login.fxml");
                }

            } catch (SQLException | IOException error) { error.printStackTrace(); }

        }

    }
}
