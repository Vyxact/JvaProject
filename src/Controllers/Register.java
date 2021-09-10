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
    private static final String SQL = "INSERT INTO users(firstname, lastname, username, email, password, age, gender) VALUES(?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::int, ?::character)";

    @FXML
    private TextField firstname, lastname, username, password, age, email, gender;

    @FXML
    void register (ActionEvent e) {

        String fname = firstname.getText();
        String lname = lastname.getText();
        String uname = username.getText();
        String mail = email.getText();
        String pass = password.getText();
        int ag = Integer.parseInt( age.getText() );
        String gdr = gender.getText();

        try ( Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS) ) {
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, uname);
            stmt.setString(4, pass);
            stmt.setString(5, mail);
            stmt.setInt(6, ag);
            stmt.setString(7, gdr);

            stmt.executeUpdate();
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
