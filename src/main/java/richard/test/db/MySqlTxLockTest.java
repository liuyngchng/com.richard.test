package richard.test.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * CREATE TABLE `a` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT,
 *   `name` varchar(45) DEFAULT NULL,
 *   `address` varchar(45) DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=523 DEFAULT CHARSET=utf8
 */
public class MySqlTxLockTest extends JdbcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlTxLockTest.class);

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>(10);

        for (int i = 0; i < 100; i ++) {
            Thread t = new Thread(new MySqlTxLockTest.MysqlJob());
            t.setName("Thread" + i);
            threadList.add(t);
        }
        LOGGER.info("all thread is OK");
        for (int i = 0; i < threadList.size(); i++) {
            threadList.get(i).start();
//            LOGGER.info("{} is running", threadList.get(i).getName());
        }

    }

    public static class MysqlJob implements Runnable {

        private static final Logger LOGGER = LoggerFactory.getLogger(richard.test.db.MySqlTxLockTest.MysqlJob.class);
        private static String USERNAME = "root";
        private static String PASSWORD = "0000";
        private static String DRVIER = "com.mysql.cj.jdbc.Driver";
        private static String URL = "jdbc:mysql://localhost:3306/test";

        @Override
        public void run() {
            MySqlTxLockTest mySqlDbTest = new MySqlTxLockTest();
            Connection connection = mySqlDbTest.getConnection(DRVIER, URL,USERNAME, PASSWORD);
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("error", e);
            }
            ResultSet rs = null;
            PreparedStatement pstm1;
            PreparedStatement pstm2;
            PreparedStatement pstm;
            String address = Thread.currentThread().getName();
//            String sql = "update test.a set address='"+ address + "' where id  = '1'";
            String sql1 = "select id from test.a where name  = 'test'";
            String sql2 = "insert into test.a (name, address) values ('test', '"+ address + "')";
            try {
                pstm1 = connection.prepareStatement(sql1);
                rs = pstm1.executeQuery();
                LOGGER.info("prepared for {}", sql1);
                while (rs.next()) {
                    String sql = "delete from test.a where id  = '"+ rs.getString(1)+"'";
                    pstm = connection.prepareStatement(sql);
                    LOGGER.info("prepared for {}", sql);
                    pstm.execute();
                }
//                pstm2 = connection.prepareStatement(sql2);
//                pstm2.execute();
//                LOGGER.info("prepared for {}", sql2);
                Random random = new Random();
                long s = random.nextInt(5);
                LOGGER.info("sleep for {} s", s);
                Thread.sleep(s);
                connection.commit();
                LOGGER.info("commit for {}", sql1);
            } catch (Exception ex) {
                LOGGER.error("error to execute sql {}", sql1, ex);
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    LOGGER.error("error", e);
                }
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("error", e);
                }
            }
        }
    }

}
