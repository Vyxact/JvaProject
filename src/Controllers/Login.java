package Controllers;

import Database.Connect;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Login extends Connect {
    ResultSet session = null;
    private static final String SQL = "SELECT * FROM customers";

    @FXML
    private TextField username, password;

    @FXML
    ResultSet login () {

        String uname = username.getText();
        String pass = password.getText();

        try (Connection conn = connect(); Statement stmt = conn.createStatement() ) {
            ResultSet res = stmt.executeQuery(SQL);

            if ( res.getString("username").equals(uname) && res.getString("password").equals(pass) ) {
                System.out.println("Success");
                session = res;
            }
            else System.out.println("Wrong username or password");

        } catch (SQLException err) { err.getStackTrace(); }

        return session;
    }
}
