package by.shved.busbooking.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static final String DATABASE_PROPERTIES = "database.properties";
    private static final String URL_PROPERTY = "db.url";
    private static final String USERNAME_PROPERTY = "db.username";
    private static final String PASSWORD_PROPERTY = "db.password";
    private static final String POOL_SIZE_PROPERTY = "db.pool.size";
    private static final int DEFAULT_POOL_SIZE = 8;
    private static ConnectionPool instance;
    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private final BlockingQueue<Connection> freeConnections;
    private final List<Connection> allConnections;

    private ConnectionPool() {
        Properties properties = loadProperties();
        String url = properties.getProperty(URL_PROPERTY);
        String username = properties.getProperty(USERNAME_PROPERTY);
        String password = properties.getProperty(PASSWORD_PROPERTY);
        int poolSize = getPoolSize(properties);
        if (url == null || username == null || password == null) {
            throw new ExceptionInInitializerError(
                    "Database properties are not configured correctly"
            );
        }
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            logger.info("PostgreSQL driver registered");
        } catch (SQLException e) {
            logger.error("Failed to register PostgreSQL driver", e);
            throw new ExceptionInInitializerError(e);
        }
        freeConnections = new LinkedBlockingQueue<>(poolSize);
        allConnections = new ArrayList<>(poolSize);
        initializePool(url, username, password, poolSize);
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            INSTANCE_LOCK.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return instance;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     ConnectionPool.class
                             .getClassLoader()
                             .getResourceAsStream(DATABASE_PROPERTIES)) {
            if (inputStream == null) {
                throw new IllegalStateException("Cannot find " + DATABASE_PROPERTIES);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load database properties", e);
            throw new ExceptionInInitializerError(e);
        }
        return properties;
    }

    private int getPoolSize(Properties properties) {
        String poolSizeProperty = properties.getProperty(POOL_SIZE_PROPERTY);
        if (poolSizeProperty == null) {
            return DEFAULT_POOL_SIZE;
        }
        try {
            int poolSize = Integer.parseInt(poolSizeProperty);
            if (poolSize <= 0) {
                throw new IllegalArgumentException(
                        "Pool size must be greater than zero"
                );
            }
            return poolSize;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid pool size: " + poolSizeProperty,
                    e
            );
        }
    }

    private void initializePool(String url, String username, String password, int poolSize) {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        try {
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, properties);
                freeConnections.put(connection);
                allConnections.add(connection);
            }
            logger.info("Connection pool initialized. Size: {}", poolSize);
        } catch (SQLException e) {
            logger.error("Failed to initialize connection pool", e);
            closeAllConnections();
            throw new ExceptionInInitializerError(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while initializing connection pool", e);
            closeAllConnections();
            throw new ExceptionInInitializerError(e);
        }
    }

    public Connection getConnection() throws InterruptedException {
        Connection connection = freeConnections.take();
        logger.debug("Connection obtained. Free connections: {}", freeConnections.size());
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (!freeConnections.contains(connection)) {
                freeConnections.put(connection);
                logger.debug(
                        "Connection returned to pool. Free connections: {}",
                        freeConnections.size()
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while returning connection", e);
        }
    }

    public void destroyPool() {
        logger.info("Destroying connection pool");
        closeAllConnections();
        try {
            DriverManager.deregisterDriver(new org.postgresql.Driver());
            logger.info("PostgreSQL driver deregistered");
        } catch (SQLException e) {
            logger.error("Failed to deregister PostgreSQL driver", e);
        }
    }

    private void closeAllConnections() {
        for (Connection connection : allConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection", e);
            }
        }
        allConnections.clear();
        freeConnections.clear();
    }
}