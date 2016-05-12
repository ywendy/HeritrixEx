package org.archive.crawler.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cyh on 2016/5/10.
 */
@DatabaseTable(tableName = "config")
public class ConfigTable {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String property;
    @DatabaseField
    private String value;
    @DatabaseField
    private int enable;

    public ConfigTable() {
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
