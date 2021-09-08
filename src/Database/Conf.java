package Database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Conf  {
    String
            DB_DRIVER = "com.mysql.cj.jdbc.Driver",
            DB_DSN = "jdbc:postgresql/localhost:3398/jproj/ssl=true",
            DB_USERNAME = "root",
            DB_PASSWORD = "";

    Connection connect();
    void disconnect() throws SQLException;
}
