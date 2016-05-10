package org.archive.crawler.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by cyh on 2016/5/10.
 */
public class ConfigService {
    private static Dao<ConfigTable, String> config = null;

    private ConfigService() {
    }

    public static Dao<ConfigTable, String> getTable() throws SQLException {

        if (config == null) {
            config = DaoManager.createDao(OrmService.getConnectionSource(), ConfigTable.class);
        }
        return config;
    }

    /**
     * @return 最大抓取深度
     * @throws SQLException
     */
    public String getDepth() throws SQLException {
        return getTable().queryForEq("property", "depth").get(0).getValue();
    }

    /**
     *
     * @return 获取所有开启的配置属性
     * @throws SQLException
     */
    public static List<ConfigTable> getALL() throws SQLException {
        return getTable().queryForEq("enable", 0);
    }
}
