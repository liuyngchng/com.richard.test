package richard.test.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Scanner;

public class MySqlDbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDbTest.class);
    private static String USERNAME = "root";
    private static String PASSWORD = "0000";
    private static String DRVIER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/test";


    public static Connection getConnection() {
        LOGGER.info("start connect db {}", URL);
        Connection connection = null;
        try {
            Class.forName(DRVIER);
            LOGGER.info(DRVIER + "|" + URL + "|" + USERNAME + "|" + PASSWORD);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            LOGGER.info("connect success for " + URL + USERNAME + PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        } catch (Exception ex) {
            LOGGER.error("error,", ex);
        }

        return connection;
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        while (true) {
            LOGGER.info("please input sql");
            Scanner scanner = new Scanner(System.in);
            String sql = scanner.nextLine().trim(); //"SELECT  * FROM HSCMP.tProCertiInfo where rownum <= 5";
            if (null == sql || sql.trim().length() == 0) {
                LOGGER.error("pls input right sql");
                continue;
            }
            try {
                pstm = connection.prepareStatement(sql);
                LOGGER.info("start to execute sql: {}", sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    LOGGER.info("rs count {}", rs.getString(1));
                }
            } catch (Exception ex) {
                LOGGER.error("error to execute sql {}", sql, ex);
            }
        }

    }
}
