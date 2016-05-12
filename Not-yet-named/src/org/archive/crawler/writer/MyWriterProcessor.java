package org.archive.crawler.writer;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.db.DataService;
import org.archive.crawler.db.DataTable;

import org.archive.crawler.util.Toolkit;
import org.archive.net.UURI;

import java.sql.Date;


/**
 * Created by cyh on 2016/4/16.
 *
 * @Author cyh
 * @Date 2016/4/16
 */
public class MyWriterProcessor extends MirrorWriterProcessor {

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


        String pageContent = Toolkit.getBody(curi.getPageContent());

        if (isImputy(pageContent) || pageContent.isEmpty()) {
            System.err.println("网页内容为空");
            return;
        }

        //构建DataTable对象
        DataTable data = new DataTable();
        data.setParent(Toolkit.trimSlash(curi.getParent()));
        data.setContent(pageContent);
        data.setSeed(Toolkit.trimSlash(curi.getSeedSource()));
        data.setUrl(Toolkit.trimSlash(curi.toString()));
        data.setLevel(curi.getLevel());
        data.setTime(new Date(System.currentTimeMillis()));
        data.setSignature(Toolkit.md5Encode(curi.toString()));

        // 插入数据库中
        DataService.addOne(data);
    }

    public boolean isImputy(String pageContent) {
        if (pageContent.contains("禁止盗链， 请从本网站上下载! ")) {
            return true;
        }
        return false;
    }
}
