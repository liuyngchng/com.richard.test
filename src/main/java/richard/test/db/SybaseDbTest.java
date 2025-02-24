//package richard.test.db;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.Scanner;
//
//public class SybaseDbTest extends JdbcTest {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SybaseDbTest.class);
//    private static String USERNAME = "query";
//    private static String PASSWORD = "query_ps";
//    private static String DRVIER = "com.sybase.jdbc3.jdbc.SybDriver";
//    private static String URL = "jdbc:sybase:Tds:10.179.200.166:2638?ServiceName=query13";
//
//
//    public static void main(String[] args) {
//        SybaseDbTest oracleDbTest = new SybaseDbTest();
//        Connection connection = oracleDbTest.getConnection(DRVIER,URL,USERNAME, PASSWORD);
//        PreparedStatement pstm = null;
//        ResultSet rs = null;
//        while (true) {
//            LOGGER.info("please input sql");
//            Scanner scanner = new Scanner(System.in);
//            String sql = scanner.nextLine().trim(); //"SELECT  * FROM HSCMP.tProCertiInfo where CxBillNo = ? rownum <= 5";
//            if (null == sql || sql.trim().length() == 0) {
//                LOGGER.error("pls input right sql");
//                continue;
//            }
////            SybaseDbTest.queryData(connection, sql);
//        }
//
//    }
//
////    private static void queryData(Connection connection, String sql) {
////        PreparedStatement pstm;
////        ResultSet rs;
////        try {
////            pstm = connection.prepareStatement(sql);
////            LOGGER.info("start to execute sql: {}", sql);
////            rs = pstm.executeQuery();
////            ResultSetMetaData metaData = rs.getMetaData();
////            LOGGER.info("column increaseCount is {}", metaData.getColumnCount());
////            StringBuilder header = new StringBuilder();
////            for (int i = 0; i < metaData.getColumnCount(); i ++) {
////                header.append(metaData.getColumnName(i + 1) + "(" + metaData.getColumnType(i + 1) + ") | ");
////            }
////            LOGGER.info("data list is as following.");
////            System.out.println(header.toString());
////            while (rs.next()) {
////                StringBuilder sb = new StringBuilder();
////                for (int i = 0; i < metaData.getColumnCount(); i ++) {
////                    sb.append(rs.getString(i + 1) + " | ");
////                }
////                System.out.println(sb.toString());
////            }
////        } catch (Exception ex) {
////            LOGGER.error("error to execute sql {}", sql, ex);
////        }
//    }
//}
