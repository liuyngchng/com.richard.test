package richard.test.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Scanner;

public class MySqlDbTest extends JdbcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDbTest.class);
    private static String USERNAME = "root";
    private static String PASSWORD = "0000";
    private static String DRVIER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/test";



    public static void main(String[] args) {
        MySqlDbTest mySqlDbTest = new MySqlDbTest();
        Connection connection = mySqlDbTest.getConnection(DRVIER, URL,USERNAME, PASSWORD);
        PreparedStatement pstm = null;
        ResultSet rs = null;
        while (true) {
            LOGGER.info("please input sql");
            Scanner scanner = new Scanner(System.in);
            String sql = scanner.nextLine().trim();
            if (null == sql || sql.trim().length() == 0) {
                LOGGER.error("pls input right sql");
                continue;
            }
            try {
                pstm = connection.prepareStatement(sql);
                LOGGER.info("start to execute sql: {}", sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    LOGGER.info("rs increaseCount {}", rs.getString(1));
                }
            } catch (Exception ex) {
                LOGGER.error("error to execute sql {}", sql, ex);
            }
        }

    }
}
