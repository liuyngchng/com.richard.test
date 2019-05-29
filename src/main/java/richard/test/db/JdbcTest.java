package richard.test.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Scanner;

public class JdbcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTest.class);


    public Connection getConnection(String driver, String url, String userName, String password) {
        LOGGER.info("start connect db {}", url);
        Connection connection = null;
        try {
            Class.forName(driver);
            LOGGER.info(driver + "|" + url + "|" + userName + "|" + password);
            connection = DriverManager.getConnection(url, userName, password);
            LOGGER.info("connect success for " + url + userName + password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        } catch (Exception ex) {
            LOGGER.error("error,", ex);
        }

        return connection;
    }
}
