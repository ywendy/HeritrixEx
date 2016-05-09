package org.archive.crawler.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class DataService {
    private static Dao<DataTable, String> data = null;

    private DataService() {
    }

    public static Dao<DataTable, String> getTable() throws SQLException {

        if (data == null) {
            data = DaoManager.createDao(OrmService.getConnectionSource(), DataTable.class);
        }
        return data;
    }

    /**
     * 插入Data数据表一行记录
     *
     * @param item
     * @return
     */
    public static boolean addOne(DataTable item) {
        boolean result = false;
        try {
            getTable().create(item);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据签名检测某个URL是否存在
     *
     * @param url 链接
     * @return 存在为true
     * @throws SQLException
     */
    public static boolean isUrlExist(String url) throws SQLException {
        return getTable().queryBuilder().where().
                eq("url", url).countOf() > 0;
    }

    /**
     * 返回与指定的url like的个数
     * @param likeurl %url%
     * @return 个数
     * @throws SQLException
     */
    public static long countLikeUrl(String likeurl) throws SQLException {
        return getTable().queryBuilder().where().like("url", likeurl).countOf();
    }

    /**
     * 获取某个种子下的所有数据
     *
     * @param seed 种子站点
     * @return 爬虫抓下来的数据
     * @throws SQLException
     */
    public static List<DataTable> getDataBySeed(String seed) throws SQLException {
        QueryBuilder query = getTable().queryBuilder();
        query.selectColumns("level").where().eq("seed", seed);
        return query.query();
    }


    public static HashMap<String, List<DataTable>> groupDataBySeeds(int enable) throws SQLException {

        List<SeedsTable> seedsList; //所有的种子列表

        if (enable == 2) {
            seedsList = SeedsService.getAllSeeds();
        } else {
            seedsList = SeedsService.getSeeds(enable);
        }

        System.out.println("已获取所有的种子列表,数目为 " + seedsList.size());
        if (seedsList == null)
            return null;

        HashMap<String, List<DataTable>> result = new HashMap<>();

        for (SeedsTable seed : seedsList) {
            List<DataTable> item = getDataBySeed(seed.getUrl());
            result.put(seed.getUrl(), item);
        }

        return result;
    }

}
