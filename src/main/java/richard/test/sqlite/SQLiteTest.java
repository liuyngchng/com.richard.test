package richard.test.sqlite;

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
 * java -DSQLite.library.path=./ -Djava.library.path=./ -jar target/richard.test-1.0-SNAPSHOT-jar-with-all-dependencies.jar
 *
 * 由Taro L. Saito开发的SQLite JDBC驱动程序是Zentus SQLite JDBC驱动程序的扩展，
 * 使Java能够访问SQLite数据库文件。Xerial版本的SQLiteJDBC库不需要进行配置，在任何OS环境中均可运行,
 * Windows，Mac OS X，Linux和纯Java SQLite的所有本机库都被组装到一个JAR（Java存档）中文件。
 * Xerial版本的SQLiteJDBC相比较最初的Zentus的SQLite JDBC驱动程序，用户不需要使用命令行参数（例如-Djava.library.path =）
 * 来设置本机代码（dll，jnilib，so文件，它们是JNDI C程序）的路径等参数。

 */
public class SQLiteTest {
    private static String Drive="org.sqlite.JDBC";
    private static final Logger LOGGER = LogManager.getLogger(SQLiteTest.class);
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
                LOGGER.info("nodeId\tuserId");
                LOGGER.info("===========================");
                LOGGER.info("{}\t{}", rSet.getString("nodeId"), rSet.getString("userId"));
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
