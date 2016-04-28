package org.archive.crawler.db;

/**
 * Created by ccoder on 2016/4/27.
 */
public class SeedsTable extends Table {
    private int id;
    private String url;
    private int enable;

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

    @Override
    public String getInsertSql() {
        return null;
    }
}
