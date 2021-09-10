package Controllers;

import Database.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Register extends Connect {
    private static final String SQL = "INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?)";

    @FXML
    private TextField firstname, lastname, username, age, gender, email,  password;

    @FXML
    void register (ActionEvent e) {

        String fname = firstname.getText(),
                lname = lastname.getText(),
                uname = username.getText(),
                ag = age.getText(),
                gdr = gender.getText(),
                mail = email.getText(),
                pass = password.getText();

        try ( Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS) ) {
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, uname);
            stmt.setString(4, ag);
            stmt.setString(5, gdr);
            stmt.setString(6, mail);
            stmt.setString(7, pass);
            stmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
        }

    }

    private void printSQLException (SQLException errors) {
        for (Throwable error : errors) {
            if (error instanceof SQLException) {
                error.printStackTrace(System.err);
                System.err.println( "SQLState: " + ( (SQLException) error).getSQLState() );
                System.err.println( "Error Code: " + ( (SQLException) error).getErrorCode() );
                System.err.println( "Message: " + error.getMessage() );
                Throwable t = errors.getCause();

                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
