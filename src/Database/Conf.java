package Database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Conf  {
    String
            DB_DRIVER = "org.postgresql.Driver",
            DB_DSN = "jdbc:postgresql://localhost:5432/jproj",
            DB_USERNAME = "kv.kn",
            DB_PASSWORD = "";


    Connection connect ();
    Connection disconnect () throws SQLException;
    void initialize() throws SQLException;
}
