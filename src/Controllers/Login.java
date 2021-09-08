package Controllers;

import Database.Connect;

import java.sql.SQLException;

public class Login extends Connect {

    void verify (String username) throws SQLException {

        String sql = "SELECT FROM users WHERE username = " + username;
    }

}
