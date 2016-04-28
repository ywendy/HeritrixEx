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

import javax.management.InvalidAttributeValueException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StartHeritrix {
    private final static String orderFilePath = System.getProperty("user.dir") + "/jobs/CMDjob/order.xml";
    private final static String seedsFilePath = System.getProperty("user.dir") + "/jobs/CMDjob/seeds.txt";

    public static void main(String[] args) {
        File file = null;   //order.xml文件
        CrawlStatusListener listener = null;//监听器
        XMLSettingsHandler handler = null;  //读取order.xml文件的处理器
        CrawlController controller = null;  //Heritrix的控制器

        if (putSeedsFromDb())  //载入种子
        {
            System.out.println("载入种子成功！");
        } else {
            System.err.println("载入种子失败，请检查程序");
            System.exit(-1);
        }


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
     * 将种子站点从数据库读入到seeds.txt文件中
     */
    public static boolean putSeedsFromDb() {
        boolean result = false;
        try {
            List<SeedsTable> allSeeds = SeedsService.getAllSeeds();
            FileWriter writer = new FileWriter(seedsFilePath, false);

            for (SeedsTable item : allSeeds) {
                writer.write(item.getUrl() + System.getProperty("line.separator"));
            }

            writer.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
