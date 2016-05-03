package org.archive.crawler.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ccoder on 2016/4/27.
 */
@DatabaseTable(tableName = "seeds")
public class SeedsTable {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String url;
    @DatabaseField
    private int enable; //0是开启，1是禁止

    public SeedsTable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
