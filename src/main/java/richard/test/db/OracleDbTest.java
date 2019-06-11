package richard.test.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Scanner;

public class OracleDbTest extends JdbcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OracleDbTest.class);
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String DRVIER = "oracle.jdbc.driver.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@10.179.0.3:1234:hbcvsdb3";


    public static void main(String[] args) {
        OracleDbTest oracleDbTest = new OracleDbTest();
        Connection connection = oracleDbTest.getConnection(DRVIER,URL,USERNAME, PASSWORD);
        PreparedStatement pstm = null;
        ResultSet rs = null;
        while (true) {
            LOGGER.info("please input sql");
            Scanner scanner = new Scanner(System.in);
            String sql = scanner.nextLine().trim(); //"SELECT  * FROM HSCMP.tProCertiInfo where CxBillNo = ? rownum <= 5";
            if (null == sql || sql.trim().length() == 0) {
                LOGGER.error("pls input right sql");
                continue;
            }
            oracleDbTest.queryData(connection, sql);
        }

    }


}
