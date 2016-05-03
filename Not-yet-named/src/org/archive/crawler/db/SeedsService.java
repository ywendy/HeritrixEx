package org.archive.crawler.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by ccoder on 2016/4/29.
 */
public class SeedsService {
    private static Dao<SeedsTable, String> seeds = null;

    private SeedsService() {
    }

    public static Dao<SeedsTable, String> getTable() throws SQLException {

        if (seeds == null) {
            seeds = DaoManager.createDao(OrmService.getConnectionSource(), SeedsTable.class);
        }
        return seeds;
    }

    /**
     * 获取所有的种子站点
     *
     * @return
     * @throws SQLException
     */
    public static List<SeedsTable> getAllSeeds() throws SQLException {

        return getTable().queryForEq("enable", 0);
    }

    /**
     * 添加一个种子站点
     *
     * @param item
     * @throws SQLException
     */
    public static void addone(SeedsTable item) throws SQLException {
        getTable().create(item);
    }

    /**
     * 种子站点url是否存在
     *
     * @param seedUrl url
     * @return true存在，false不存在
     */
    public static boolean isSeedsExits(String seedUrl) throws SQLException {
        return getTable().queryBuilder().where().eq("url", seedUrl).countOf() > 0;
    }

    /**
     * 获取表中的记录个数
     *
     * @return
     */
    public static long count() throws SQLException {
        return getTable().countOf();
    }

    /**
     * 根据seedUrl取出一条记录
     * @param seedUrl 种子站点url
     * @return
     * @throws SQLException
     */
    public static SeedsTable get(String seedUrl) throws SQLException {
        return getTable().queryForEq("url", seedUrl).get(0);
    }

    /**
     * 将对应种子URl的状态置为已完成(当抓取10层时即已完成)
     * @param seedUrl
     * @throws SQLException
     */
    public static void markSeedDone(String seedUrl) throws SQLException {
        UpdateBuilder<SeedsTable, String> updateBuilder = getTable().updateBuilder();
        updateBuilder.where().eq("url",seedUrl);
        updateBuilder.updateColumnValue("enable",1);
        updateBuilder.update();
    }

}
