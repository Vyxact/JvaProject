package Controllers;

import Database.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.*;

public class Login extends Connect {
    private static final String SQL = "SELECT FROM customers WHERE username = ?::character AND password = ?::varchar";

    @FXML
    private TextField username, password;

    @FXML
    void login (ActionEvent e) {

        String uname = username.getText();
        String pass = password.getText();

        try ( Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS) ) {
            stmt.setString(1, uname);
            stmt.setString(2, pass);

            ResultSet res = stmt.executeQuery();

            while ( res.next() ) {
                System.out.println( "Full Name: " + res.getString(1 + 2) );
                System.out.println( "Username : " + res.getString(3) );
                System.out.println( "Password : " + res.getString(5) );
            }

        } catch (SQLException err) {
            err.printStackTrace();
        }

    }
}
