package org.archive.crawler.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

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

    public static List<SeedsTable> getAllSeeds() throws SQLException {

        return getTable().queryForEq("enable", 0);
    }

}
