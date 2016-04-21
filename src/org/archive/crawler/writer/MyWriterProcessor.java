package org.archive.crawler.writer;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.db.DataTable;
import org.archive.crawler.util.Mytools;
import org.archive.io.RecordingInputStream;
import org.archive.io.ReplayInputStream;
import org.archive.net.UURI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        UURI uuri = curi.getUURI(); // Current URI.

        // Only http and https schemes are supported.
        String scheme = uuri.getScheme();
        if ((!"http".equalsIgnoreCase(scheme)
                && !"https".equalsIgnoreCase(scheme))
                || isRobots(curi)) {
            return;
        }


        RecordingInputStream recis = curi.getHttpRecorder().getRecordedInput();
        if (0L == recis.getResponseContentLength()) {
            return;
        }

        //构建DataTable对象
        DataTable data = new DataTable();
        try {
            //提取网页内容
            ReplayInputStream replayis = recis.getContentReplayInputStream();
            ByteArrayOutputStream sos = new ByteArrayOutputStream();
            replayis.readFullyTo(sos);
            data.setContent(sos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.setUrl(curi.toString());
        data.setLevel(curi.getPathFromSeed() == null ? 0 : curi.getPathFromSeed().length());
//        Mytools.writeFile("debug.txt",curi.getPathFromSeed() == null?"":curi.getPathFromSeed());
//        Mytools.writeFile("debug.txt",data.getContent());
        try {
            data.setSeed(uuri.getAuthority());
        } catch (URIException e) {
            e.printStackTrace();
        }
        // 插入数据库中
        Mytools.getDbConnect().insertData(data);
    }
}
