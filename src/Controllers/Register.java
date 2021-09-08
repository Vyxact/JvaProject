package Controllers;

import Database.Connect;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Statement;

public class Register extends Connect {
    @FXML
    private TextField firstname;

    @FXML
    private TextField firstname;

    @FXML
    private TextField firstname;

    @FXML
    private TextField firstname;

    @FXML
    private TextField firstname;

    @FXML
    void register () {
        connect();

        String sql = "";
        Statement stmt;

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
