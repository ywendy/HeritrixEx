package org.archive.crawler.datamodel;

import org.archive.crawler.db.ConfigService;
import org.archive.crawler.db.ConfigTable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cyh on 2016/5/10.
 */
public class SysConfig {

    private static HashMap<String, String> configs = new HashMap<>();

    private SysConfig() {
    }

    /**
     * @return 属性配置，key均大写，value全部都是小写
     * @throws SQLException
     */
    private static HashMap<String, String> getConfig() throws SQLException {
        if (configs == null || configs.isEmpty()) {
            List<ConfigTable> all = ConfigService.getALL();
            if (all != null) {
                for (ConfigTable row : all) {
                    configs.put(row.getProperty().toUpperCase(), row.getValue().toLowerCase());
                }
            }

        }
        return configs;
    }

    /**
     * @return 最大爬行深度
     * @throws SQLException
     */
    public static int getDepth() throws SQLException {
        return Integer.valueOf(getConfig().get("DEPTH"));
    }

}
