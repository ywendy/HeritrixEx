package org.archive.crawler.db;

/**
 * Created by cyh on 2016/4/18.
 */
public class DataTable extends Table {
    private int id;
    private String url;
    private String content;
    private String seed;
    private int level;


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

    @Override
    public String getInsertSql() {
        return "insert into data (content,seed,url,level) values (?,?,?,?)";
    }
}
