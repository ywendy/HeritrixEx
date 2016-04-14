package org.archive.crawler.db;

import org.archive.crawler.util.Mytools;

import java.sql.*;
import java.util.Properties;

/**
 * Created by cyh on 2016/4/14.
 * 用于数据库管理的类
 */
public class DataService {
    private Connection connect;
    private static final String confName = "/conf/db.properties";

    /*
    * 获取数据库的连接，单例模式
    *
     */
    public Connection getConnection() {
        if (connect == null) {
            Properties props = Mytools.readConfFile(System.getProperty("user.dir") + confName);
            try {
                Class.forName("com.mysql.jdbc.Driver");

                String DB_URL = props.getProperty("ADDRESS") + ":" + props.getProperty("PORT") + "/" + props.getProperty("DBNAME");
                System.out.println(DB_URL);
                connect = DriverManager.getConnection(
                        DB_URL,
                        props.getProperty("USER"),
                        props.getProperty("PASSWORD")
                );

                System.out.println("数据库成功连接");
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC路径未找到");
                System.exit(-1);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("数据库连接错误");
                System.exit(-1);
            }
        }
        return connect;
    }

    public int getDataLen() {
        Connection conn = getConnection();
        Statement stmt = null;
        int count = 1;
        try {
            stmt = conn.createStatement();
            String sql = "select count(*)  from data";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void main(String[] args) {
        DataService ds = new DataService();
        System.out.println(ds.getDataLen());

    }


}
