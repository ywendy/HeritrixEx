package org.archive.crawler.util;

import org.apache.commons.lang.StringUtils;
import org.archive.util.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
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
     *
     * @param filename 文件名
     * @param content  追加的内容
     * @param mode     如果是true的话即追加内容，false为清空后再写入
     */
    public static void writeFile(String filename, String content, boolean mode) {
        try {
            FileWriter writer = new FileWriter(filename, mode);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
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
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("配置文件" + filePath + "不存在");
                return null;
            }

            InputStream istreamFile = new BufferedInputStream(new FileInputStream(file));
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
     * @return 后缀，若检测不到为空
     */
    public static String parseSuffix(String url) {

        if (url == null || url.isEmpty())
            return "";

        url = trimSlash(url.replaceAll("http(s?)://", ""));

        int last = url.lastIndexOf('/');
        if (last != -1) {
            url = url.substring(last + 1);
        } else {
            return "";
        }

        int mark = url.lastIndexOf('?');
        if (mark != -1) {
            url = url.substring(0, mark);
        }

        String suffix = "";
        int point = url.lastIndexOf('.');
        if (point != -1) {
            suffix = url.substring(point + 1);
        }
        return suffix;
    }

    /**
     * @param source     要进行摘要算法的字符串
     * @param digestName 算法名字，md5，sha-1
     * @return
     */
    private static String encode(String source, String digestName) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            byte[] bytes = source.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance(digestName);
            messageDigest.update(bytes);
            byte[] updateBytes = messageDigest.digest();
            int len = updateBytes.length;
            char myChar[] = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte byte0 = updateBytes[i];
                myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
                myChar[k++] = hexDigits[byte0 & 0x0f];
            }
            return new String(myChar);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * md5摘要算法
     *
     * @param source 要进行md5摘要的字符串
     * @return
     */
    public static String md5Encode(String source) {
        return encode(source, "MD5");
    }

    /**
     * sha-1摘要算法
     *
     * @param source 要进行摘要算法的字符串
     * @return
     */
    public static String sha1Encode(String source) {
        return encode(source, "SHA-1");
    }

    /**
     * 复制文件
     *
     * @param sourcePath 源文件的路径
     * @param destPath   目标文件的路径
     * @throws IOException
     */
    public static void copyFile(String sourcePath, String destPath) throws IOException {
        FileUtils.copyFile(new File(sourcePath), new File(destPath));
    }

    /**
     * @param htmlPage 0代表header 1代表页面
     * @return
     */
    public static String getPageEncoding(String[] htmlPage) throws IOException {
        if (htmlPage.length != 2) {
            return null;
        }
        String result = null;
        result = getEncodingByHeader(htmlPage[0]);
        if (result == null) {
            result = getHtmlCharset(htmlPage[1]);

        }

        if (result == null) {
            result = "UTF-8";

        } else if (result.equals("gb2312")) {
            result = "GBK";
        }

        return result.toUpperCase();
    }


    /**
     * 借鉴nutch中从头部获取编码的方式
     *
     * @param contentType http头
     * @return 检测到的编码，如果没有则为null
     */
    public static String getEncodingByHeader(String contentType) {
        if (contentType == null)
            return (null);
        int start = contentType.indexOf("charset=");
        if (start < 0)
            return (null);
        String encoding = contentType.substring(start + 8);
        int end = encoding.indexOf(';');
        if (end >= 0)
            encoding = encoding.substring(0, end);

        end = encoding.indexOf('\n');
        if (end >= 0)
            encoding = encoding.substring(0, end);

        encoding = encoding.trim();
        if ((encoding.length() > 2) && (encoding.startsWith("\""))
                && (encoding.endsWith("\"")))
            encoding = encoding.substring(1, encoding.length() - 1);
        return (encoding.trim());
    }

    /**
     * 借鉴webmagic中从html正文获取编码的方式
     *
     * @param htmlContent 正文
     * @return 检测到的编码，如果没有则为null
     * @throws IOException
     */
    protected static String getHtmlCharset(String htmlContent) throws IOException {
        String charset = null;

        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(htmlContent.getBytes(), defaultCharset.name());

        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.contains("charset")) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                } else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        return charset;
    }

    private final static Pattern bodyPattern = Pattern.compile("(?is).*<body.*>(.*)</body>.*");

    /**
     * 获取html页面中的body部分
     *
     * @param pageContent 一个html页面
     * @return body正文，不带body标签
     */
    public static String getBody(String pageContent) {
        Matcher matcher = bodyPattern.matcher(pageContent);
        String result = "";
        while (matcher.find()) {
            result = matcher.group(0) + matcher.group(1);
            break;
        }
        return result.trim();
    }

    /**
     * @param url 网址
     * @return 去除url尾部多余的斜杠
     */
    public static String trimSlash(String url) {
        if (url == null || url.trim().isEmpty())
            return url;
        url = url.trim();
        int length = url.length();
        if (length == 1 && url.charAt(0) == '/')
            return url;

        while (url.charAt(length - 1) == '/' && length > 1)
            length--;
        return url.substring(0, length);
    }

    private static final String domainSuffix = "(org|net|gov|edu|com)(\\.cn)?" +
            "|cn|biz|info|cc|tv|me|xyz";
    private static final String domainRegex = "^http(s)?://(www\\.)?([^/]*\\." + domainSuffix + ")";
    private static final Pattern domainPattern = Pattern.compile(domainRegex);

    /**
     * 得到url的domain，如http://www.cs.whu.edu.cn -> cs.whu.edu.cn
     * http://baidu.com -> baidu.com
     *
     * @param url 该url要求比较严格，必须以http开头
     * @return domain
     */
    public static String getDomainForUrl(String url) {
        String domain = null;
        if (url == null) {
            return null;
        } else {
            Matcher m = domainPattern.matcher(url);
            while (m.find()) {
                domain = m.group(3);
                break;
            }
        }
        return domain == null ? "" : domain;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }

}
