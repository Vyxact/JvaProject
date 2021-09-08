package Controllers;

import Database.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends Connect {
    private static final String SQL = "INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?)";

    @FXML
    private TextField firstname, lastname, username, age, gender, email,  password;

    @FXML
    private Button register;

    @FXML
    void register (ActionEvent e) throws SQLException {

        String firstname = firstname.getText(),
                lastname = lastname.getText(),
                username = username.getText(),
                age = age.getText(),
                gender = gender.getText(),
                email = email.getText(),
                password = password.getText();

        try {
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, username);
            stmt.setString(4, age);
            stmt.setString(5, gender);
            stmt.setString(6, email);
            stmt.setString(7, password);
        } catch (SQLException err) {
            printSQLException(err);
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
