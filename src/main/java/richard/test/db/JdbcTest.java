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

    public void queryData(Connection connection, String sql) {
        PreparedStatement pstm;
        ResultSet rs;
        try {
            pstm = connection.prepareStatement(sql);
            LOGGER.info("start to execute sql: {}", sql);
            rs = pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            LOGGER.info("column count is {}", metaData.getColumnCount());
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < metaData.getColumnCount(); i ++) {
                header.append(metaData.getColumnName(i + 1) + "(" + metaData.getColumnType(i + 1) + ") | ");
            }
            LOGGER.info("data list is as following.");
            System.out.println(header.toString());
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < metaData.getColumnCount(); i ++) {
                    sb.append(rs.getString(i + 1) + " | ");
                }
                System.out.println(sb.toString());
            }
        } catch (Exception ex) {
            LOGGER.error("error to execute sql {}", sql, ex);
        }
    }
}
