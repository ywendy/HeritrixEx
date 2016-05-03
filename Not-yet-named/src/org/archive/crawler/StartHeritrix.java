/**
 * Created by cyh on 16-3-14.
 */
package org.archive.crawler;

import org.archive.crawler.db.SeedsService;
import org.archive.crawler.db.SeedsTable;
import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.crawler.util.Toolkit;
import org.apache.commons.io.FileUtils;

import javax.management.InvalidAttributeValueException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StartHeritrix {
    public final static String basePath = System.getProperty("user.dir") + File.separator + ".." + File.separator;
    public final static String jobPath = basePath + "jobs"+File.separator;
    public final static String confPath = basePath + "config"+File.separator;

    public final static String orderFilePath = jobPath + "order.xml";
    public final static String seedsFilePath = jobPath + "seeds.txt";

    public final static String orderSamplePath = confPath + "order.xml";
    public final static String dbconfPath = confPath + "db.properties";

    public static Set<String> doneSeeds = new HashSet<>();

    public static void main(String[] args) {
        File file = null;   //order.xml文件
        CrawlStatusListener listener = null;//监听器
        XMLSettingsHandler handler = null;  //读取order.xml文件的处理器
        CrawlController controller = null;  //Heritrix的控制器

        try {
            loadJob();
        } catch (IOException e) {
            System.err.println("文件操作异常，载入任务失败");
            System.exit(-1);
        } catch (SQLException e) {
            System.err.println("数据库异常，载入任务失败");
            System.exit(-1);
        }

        System.out.println("载入任务初始数据成功!");
        try {
            //定位到jobs的文件夹
            file = new File(orderFilePath);
            //如果order.xml文件不存在
            if (!file.exists()) {
                System.err.println("order.xml文件不存在!!");
                System.exit(-1); //非正常退出
            }

            handler = new XMLSettingsHandler(file);

            handler.initialize();//读取order.xml中的各个配置

            controller = new CrawlController();//
            controller.initialize(handler);//从读取的order.xml中的各个配置来初始化控制器

            if (listener != null) {
                controller.addCrawlStatusListener(listener);//控制器添加监听器
            }

            controller.requestCrawlStart();//开始抓取

            // 如果Heritrix还一直在运行则等待
            while (true) {
                if (controller.isRunning() == false) {
                    break;
                }
                Thread.sleep(1000);
            }
            //如果Heritrix不再运行则停止
            controller.requestCrawlStop();
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        } catch (InitializationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入任务文件夹
     */
    public static void loadJob() throws IOException, SQLException {

        //载入任务前先清空
        FileUtils.cleanDirectory(new File(jobPath));
        System.out.println("清空旧任务的数据...");
        //将order.xml文件复制到任务文件夹
        Toolkit.copyFile(orderSamplePath, orderFilePath);
        System.out.println("载入order.xml文件...");
        putSeedsFromDb();
        System.out.println("从数据库中载入种子文件...");
    }

    /**
     * 将种子站点从数据库读入到seeds.txt文件中
     */
    public static void putSeedsFromDb() throws SQLException, IOException {

        List<SeedsTable> allSeeds = SeedsService.getAllSeeds();
        FileWriter writer = new FileWriter(seedsFilePath, false);

        for (SeedsTable item : allSeeds) {
            writer.write(item.getUrl() + System.getProperty("line.separator"));
        }

        writer.close();
    }

}
