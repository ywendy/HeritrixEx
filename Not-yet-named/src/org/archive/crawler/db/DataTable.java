package org.archive.crawler.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

/**
 * Created by cyh on 2016/4/18.
 */
@DatabaseTable(tableName = "data")
public class DataTable {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String url;
    @DatabaseField
    private String content;
    @DatabaseField
    private String seed;
    @DatabaseField
    private int level;
    @DatabaseField
    private Date time;
    @DatabaseField(index = true)
    private String signature;

    public DataTable() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
