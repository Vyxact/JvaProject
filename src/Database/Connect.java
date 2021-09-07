package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements Conf {
    Connection conn;

    @Override
    public void connect () {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("driver loaded..");
        }
        catch (ClassNotFoundException e) {
            System.out.println("sorry, the driver could not be found..");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_DSN, DB_USERNAME, DB_PASSWORD);
            System.out.println("you're connected to the database.");
        }
        catch (SQLException e) {
            System.out.println("could not established connection to the database.");
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect () throws SQLException {
        if ( conn != null && !conn.isClosed() )
            conn.close();
    }
}
