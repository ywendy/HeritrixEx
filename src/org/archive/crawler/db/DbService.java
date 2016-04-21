package org.archive.crawler.db;

import org.archive.crawler.util.Mytools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by cyh on 2016/4/14.
 * 用于数据库管理的类
 */
public class DbService {
    private Connection connect;
    private static final String confName = "/conf/db.properties";


    /**
     * 在Data表中插入一行记录
     *
     * @param data
     */
    public void insertData(DataTable data) {
        List<Object> params = new ArrayList<Object>();
        params.add(data.getContent());
        params.add(data.getSeed());
        params.add(data.getUrl());
        params.add(data.getLevel());

        updateDb(data.getInsertSql(), params);
    }

    /*
   * 获取数据库的连接
    */
    private Connection getConnection() {
        try {
            if (connect == null || connect.isClosed()) {
                Properties props = Mytools.readConfFile(System.getProperty("user.dir") + confName);
                try {
                    Class.forName("com.mysql.jdbc.Driver");

                    String DB_URL = props.getProperty("ADDRESS") + ":" + props.getProperty("PORT") + "/" + props.getProperty("DBNAME")
                            + "?useUnicode=true&characterEncoding=UTF-8";

                    connect = DriverManager.getConnection(
                            DB_URL,
                            props.getProperty("USER"),
                            props.getProperty("PASSWORD")
                    );

                    System.out.println("数据库连接成功");
                } catch (ClassNotFoundException e) {
                    System.err.println("JDBC路径未找到");
                    System.exit(-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("数据库连接错误");
                    System.exit(-1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }

    /**
     * 执行一条insert语句或者update语句
     *
     * @param sql    sql语句
     * @param params 占位符对应的变量
     * @return 成功则返回影响的行数，失败则返回null
     */
    private Integer updateDb(String sql, List<Object> params) {
        Connection conn = getConnection();
        boolean result = true;
        PreparedStatement pstmt = null;
        Integer effectRows = null;

        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 1; i <= params.size(); i++) {
                pstmt.setObject(i, params.get(i - 1));
            }

            effectRows = pstmt.executeUpdate(); //执行sql语句
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
            }
        }

        return effectRows;
    }

    public static void main(String[] args) {
        DataTable db = new DataTable();
        db.setContent("123");
        db.setSeed("hello");
        db.setUrl("www.baidu.com");

        DbService ds = new DbService();
        ds.insertData(db);
        ds.insertData(db);
    }
}
