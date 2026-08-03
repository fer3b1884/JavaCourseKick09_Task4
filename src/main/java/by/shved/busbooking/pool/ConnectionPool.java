package by.shved.busbooking.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static ConnectionPool instance;
    private BlockingQueue<Connection> free = new LinkedBlockingQueue<>(8);
    private BlockingQueue<Connection> used = new LinkedBlockingQueue<>(8);

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
//            Class.forName("org.postgresql.Driver");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionPool() {
        // properties file
        String url = "jdbc:postgresql://localhost:5432/bus_ticket_booking";
        Properties prop = new Properties();
        prop.put("user", "postgres");
        prop.put("password", "postSQLgres2");

        for (int i = 0; i < 8; i++) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, prop);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            free.add(connection);
        }
    }

    public static ConnectionPool getInstance() {
        // todo thread-safe
        if (instance == null) instance = new ConnectionPool();
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = free.take();
            used.put(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        try {
            used.remove(connection);
            free.put(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
