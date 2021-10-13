package Controllers;

import Database.Connect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;


public class Register extends Connect {
    Parent root;
    Scene scene;
    Stage stage;

    StringBuilder account_number () {
        Random account_number = new Random();
        StringBuilder card = new StringBuilder("ACC");

        for (int i = 0; i < 14; i++) {
            int n = account_number.nextInt(10);
            card.append(n);
        }

        return card;
    }

    @Override
    public void initialize () {
        acc_type.getItems().addAll(account_type);
    }

    private String get_type () {
        return acc_type.getValue();
    }

    private void clicked () throws IOException {
        stage = new Stage();
        stage.setTitle("E-Rent");

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/login.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private final String[] account_type = { "Savings", "Regular" };
    private static final String cust_query = "INSERT INTO customers VALUES (?::uuid, ?::uuid, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar)";
    private static final String acc_query = "INSERT INTO accounts VALUES (?::uuid, ?::uuid, ?::text, ?::varchar, ?::numeric)";
    private static final String hist_query = "INSERT INTO history VALUES (?::uuid, ?::uuid)";
    private static final String fetch_query = "SELECT * FROM branches LIMIT 1";

    @FXML
    private TextField balance, firstname, lastname, username, password, city, contact;

    @FXML
    private ChoiceBox<String> acc_type;

    @FXML
    void register () throws IOException {
        Connection conn = connect();

        String branch_id;

        //        ------------------------------------------------------
        String cust = String.valueOf( UUID.randomUUID() );
        String fname = firstname.getText();
        String lname = lastname.getText();
        String user = username.getText();
        String pass = password.getText();
        String cty = city.getText();
        String contct = contact.getText();

        try ( Statement _fetch = conn.createStatement() ) {
            ResultSet res = _fetch.executeQuery(fetch_query);
            branch_id = "48024cdd-4d98-4db3-954d-8ffa5f0ad4c8";

            try ( PreparedStatement _stmt = conn.prepareStatement(cust_query, Statement.RETURN_GENERATED_KEYS) ) {
                _stmt.setString(1, cust);
                _stmt.setString(2, branch_id);
                _stmt.setString(3, fname);
                _stmt.setString(4, lname);
                _stmt.setString(5, user);
                _stmt.setString(6, pass);
                _stmt.setString(7, cty);
                _stmt.setString(8, contct);
                _stmt.executeUpdate();
            } catch (SQLException err) { err.getStackTrace(); }

        } catch (SQLException err) { err.getStackTrace(); }

        //        ------------------------------------------------------        //
        //        ------------------------------------------------------        //
        String acc = String.valueOf( UUID.randomUUID() );
        String card = String.valueOf( account_number() );
        String type = get_type();
        double amount = Double.parseDouble( balance.getText() );

        try ( PreparedStatement stmt = conn.prepareStatement(acc_query, Statement.RETURN_GENERATED_KEYS) ) {
            stmt.setString(1, acc);
            stmt.setString(2, cust);
            stmt.setString(3, card);
            stmt.setString(4, type);
            stmt.setDouble(5, amount);
            stmt.executeUpdate();
        } catch (SQLException err) { err.getStackTrace(); }
        //        ------------------------------------------------------

        String id = String.valueOf( UUID.randomUUID() );
//        String message = "Account creation. Full Name: " + lname + " " + fname + ". Account Number: " + acc + ". Account Type: " + type;
//        String status = "Success";

        try (PreparedStatement _hist_ = conn.prepareStatement(hist_query, Statement.RETURN_GENERATED_KEYS) ) {
            _hist_.setString(1, id);
            _hist_.setString(2, acc);
//            _hist_.setString(3, message);
//            _hist_.setString(4, status);
        } catch (SQLException err) { err.getStackTrace(); }

        clicked();
    }
}
