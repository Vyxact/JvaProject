package Controllers;

import Database.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Register extends Connect {
    Parent root;
    Scene scene;
    Stage stage;

    private static final String SQL = "INSERT INTO users(firstname, lastname, username, email, password, age, gender) VALUES(?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::int, ?::character)";

    @FXML
    private TextField firstname, lastname, username, email, password, age, gender;

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
            stmt.setString(4, mail);
            stmt.setString(5, pass);
            stmt.setInt(6, ag);
            stmt.setString(7, gdr);

            stmt.executeUpdate();

            clicked();

        } catch (SQLException | IOException err) {
            err.printStackTrace();
        }

    }

    private void clicked () throws IOException {
        Stage stage = new Stage();
        stage.setTitle("E-Rent");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
