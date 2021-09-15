package Controllers;

import Database.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.*;

public class Login extends Connect {
    private static final String SQL = "SELECT FROM users WHERE username = ?::character OR email = ?::varchar AND password = ?::varchar";

    @FXML
    private TextField user_mail, password, email;

    @FXML
    void login (ActionEvent e) {

        String uname = user_mail.getText();
        String mail = user_mail.getText();
        String pass = password.getText();

        try ( Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS) ) {
            stmt.setString(1, uname);
            stmt.setString(2, pass);
            stmt.setString(3, mail);

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
