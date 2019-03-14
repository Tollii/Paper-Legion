package Database;

import java.sql.Connection;

interface ConnectionPool {

    Connection getConnection();
    boolean releaseConnection(Connection con);
    String getUrl();
    String getUser();
    String getPassword();
}
