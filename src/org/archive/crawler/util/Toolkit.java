package org.archive.crawler.util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cyh on 16-3-23.
 */
public class Toolkit {
    private static Logger logger =
            Logger.getLogger(Toolkit.class.getName());

    /**
     * 向名为filename的文件中追加内容content，并和之前的内容以换行隔开
     */
    public static void writeFile(String filename, String content) {
        writeFile(filename, "\n\t" + content, true);
    }

    /**
     * 向名为filename的文件中写入content
     * @param  filename 文件名
     * @param  content 追加的内容
     * @param  mode 如果是true的话即追加内容，false为清空后再写入
      */
    public static void writeFile(String filename, String content, boolean mode)
    {
        try
        {
            FileWriter writer = new FileWriter(filename, mode);
            writer.write(content);
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



    private final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

    /**
     * 获取链接的后缀名
     *
     * @return
     */
    public static String parseSuffix(String url) {

        if (url.endsWith("/"))
            return "";

        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        if (len == 0) {
            return "";
        }
        String endUrl = spUrl[len - 1];
        if (!endUrl.contains("."))
            return "";

        String[] exts = null;
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String[] spEndUrl = endUrl.split("\\?");
            exts = spEndUrl[0].split("\\.");
        } else {
            exts = endUrl.split("\\.");
        }

        return exts[exts.length - 1];
    }

    public static void main(String[] args) {
        System.out.println(parseSuffix("q.cnblog.com/"));
    }

}
