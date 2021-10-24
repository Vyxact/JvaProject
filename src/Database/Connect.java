package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements Conf {
    Connection conn;

    @Override
    public Connection connect () {
        try { Class.forName(DB_DRIVER); conn = DriverManager.getConnection(DB_DSN, DB_USERNAME, DB_PASSWORD); }
        catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
        return conn;
    }

    @Override
    public Connection disconnect () throws SQLException {
        if ( conn != null && !conn.isClosed() ) conn.close();
        return conn;
    }

    @Override
    public void initialize () throws SQLException {}
}