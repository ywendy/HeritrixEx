package org.archive.crawler.util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by cyh on 16-3-23.
 */
public class Mytools {
    private static Logger logger =
            Logger.getLogger(Mytools.class.getName());
    /**
     * 向名为filename的文件中追加content,并和之前的内容以换行隔开
     * @param  filename 文件名
     * @param  content 追加的内容
      */
    public static void writeFile(String filename,String content)
    {
        try
        {
            FileWriter writer=new FileWriter(filename,true);
            writer.write("\n\t"+content);
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读取properties文件的配置
     *
     * @param filePath 配置文件路径
     * @return 配置文件的键值对
     */
    public static Properties readConfFile(String filePath) {
        Properties props = new Properties();
        try {
            InputStream istreamFile = new BufferedInputStream(new FileInputStream(filePath));
            props.load(istreamFile);
        } catch (IOException e) {
            logger.info("配置文件出错");
        }
        return props;
    }

}
