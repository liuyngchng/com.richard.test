package richard.test.sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by richard on 5/22/18.
 * sqlite可以直接导入csv文件到创建的表中.
 * create table test (id1, id2, id3);
 * .separator ";"
 */
public class SqlliteTest {
    private static String Drive="org.sqlite.JDBC";
    private static final Logger LOGGER = LogManager.getLogger(SqlliteTest.class);
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Class.forName(Drive);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db/test.db");//连接数据库zhou.db,不存在则创建
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();   //创建连接对象，是Java的一个操作数据库的重要接口

            statement.executeUpdate("DROP TABLE IF EXISTS usertoken");//判断是否有表tables的存在。有则删除
            statement.executeUpdate(getCreateSql());            //创建数据库
//            statement.execute(createIndexSql);
            statement.executeUpdate("insert into usertoken (nodeId, userId, token, cmphost, cmpport) values(0,'156546','123456','123.456.789','8080')");//向数据库中插入数据
            ResultSet rSet = statement.executeQuery("select * from usertoken");//搜索数据库，将搜索的放入数据集ResultSet中
            while (rSet.next()) {            //遍历这个数据集
                System.out.println("nodeId：" + rSet.getString("nodeId"));//依次输出 也可以这样写 rSet.getString(“name”)
                System.out.println("userId：" + rSet.getString("userId"));
            }
            rSet.close();//关闭数据集
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
    }

    public static String getCreateSql() {
        String createTableSql = "CREATE TABLE `usertoken` (" +
            "  `nodeId` int(11) DEFAULT NULL," +
            "  `userId` varchar(36) UNIQUE NOT NULL DEFAULT ''," +
            "  `token` varchar(150) DEFAULT NULL," +
            "  `cmphost` varchar(30) DEFAULT NULL," +
            "  `cmpport` varchar(4) DEFAULT NULL" +
            ");";
//            String createIndexSql = "CREATE  UNIQUE INDEX `userId` ON usertoken (`userId`);";
        LOGGER.info("createTableSql is {}", createTableSql);
        return createTableSql;
    }
}
