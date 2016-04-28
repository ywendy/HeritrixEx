package org.archive.crawler.writer;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.db.DataService;
import org.archive.crawler.db.DataTable;
import org.archive.crawler.util.Toolkit;
import org.archive.net.UURI;

import java.sql.Date;

/**
 * Created by cyh on 2016/4/16.
 */
public class MyWriterProcessor extends MirrorWriterProcessor {

    /**
     * @param name
     */
    public MyWriterProcessor(String name) {
        super(name);
    }

    protected void innerProcess(CrawlURI curi) {
        if (!curi.isSuccess()) {
            return;
        }
        UURI uuri = curi.getUURI(); // Current URI

        // Only http and https schemes are supported.
        String scheme = uuri.getScheme();
        if ((!"http".equalsIgnoreCase(scheme)
                && !"https".equalsIgnoreCase(scheme))
                || isRobots(curi)) {
            return;
        }


        //构建DataTable对象
        DataTable data = new DataTable();
        data.setContent(curi.getPageContent());
        data.setSeed(curi.getSeedSource());
        data.setUrl(curi.toString());
        data.setLevel(curi.getLevel());
        data.setTime(new Date(System.currentTimeMillis()));
        // 插入数据库中
//        Toolkit.getDbConnect().insertData(data);
        DataService.addOne(data);
    }
}
