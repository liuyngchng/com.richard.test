package richard.test;

import java.sql.*;

/**
 * 11.
 */
public class KwDbTest {

    public static void main(String[] args) throws Exception {
        try {
            Class.forName("com.kaiwudb.Driver");
//            Connection conn = DriverManager.getConnection("jdbc:kaiwudb://11.53.58.77:26257/defaultdb?user=test&password=test");


            Connection conn = DriverManager.getConnection("jdbc:kaiwudb://127.0.0.1:26257/defaultdb?sslmode=disable&user=root");
            // 创建statement
            Statement stmt = conn.createStatement();
            // 创建时序数据库
//            stmt.executeUpdate("create ts database ts_db ");
            // 使用数据库
            stmt.executeUpdate("use ts_db");


            // 创建时序表   CREATE TABLE if not exists sensor_data
            stmt.executeUpdate("CREATE TABLE sensor_data  (k_timestamp TIMESTAMPTZ NOT NULL, temperature FLOAT8 NOT NULL, humidity FLOAT8) TAGS (sensor_id INT4 NOT NULL, sensor_type INT4 NOT NULL) PRIMARY TAGS(sensor_id) ");
            // 按照指定的列顺序插入数据
            int rows1 = stmt.executeUpdate("insert into sensor_data (k_timestamp, temperature, humidity, sensor_id, sensor_type) values ('2023-07-20 16:12:12.123', 23.34, 55.20, 1,1)");
            System.out.println("specify column name insert " + rows1 + " rows data.");

            // 按照默认的列顺序插入数据
            int rows2 = stmt.executeUpdate("insert into sensor_data values ('2024-10-28 09:12:12.123', 23.34, 55.20, 1,1)");
            System.out.println("not specify column name insert " + rows2 + " rows data.");
            // 删除时序表
//            stmt.executeUpdate("drop table ts_table");

            // 删除数据库
//            stmt.executeUpdate("drop database ts_db cascade");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        }
}
