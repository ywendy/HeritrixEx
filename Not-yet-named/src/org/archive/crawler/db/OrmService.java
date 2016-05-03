package org.archive.crawler.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.archive.crawler.StartHeritrix;
import org.archive.crawler.util.Toolkit;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by ccoder on 2016/4/29.
 */
public class OrmService {
    private static final String confName =StartHeritrix.dbconfPath;
    private static JdbcConnectionSource connectionSource = null;
    public static JdbcConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            Properties props = Toolkit.readConfFile(confName);
            if(props == null)
            {
                System.err.println("配置文件为空");
                System.exit(-1);
            }

            String dbUrl = props.getProperty("ADDRESS") + ":" + props.getProperty("PORT") + "/" + props.getProperty("DBNAME")
                    + "?useUnicode=true&characterEncoding=UTF-8";

            try {
                connectionSource = new JdbcConnectionSource(dbUrl);
                connectionSource.setUsername(props.getProperty("USER"));
                connectionSource.setPassword(props.getProperty("PASSWORD"));
                System.out.println("连接数据库成功");
            } catch (SQLException e) {
                System.out.println("连接数据库失败");
                e.printStackTrace();
            }
        }
        return connectionSource;
    }
}
