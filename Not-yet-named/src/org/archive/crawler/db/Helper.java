package org.archive.crawler.db;

import org.archive.crawler.StartHeritrix;
import org.archive.crawler.util.Toolkit;
import org.archive.util.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

public class Helper {
    public static void main(String[] args) throws IOException, SQLException {

//        List seeds = FileUtils.readLines(new File(StartHeritrix.basePath+"seeds.txt"));
//        for (Object item: seeds) {
//            if(!SeedsService.isSeedsExits(item.toString()))
//            {
//                SeedsTable row = new SeedsTable();
//                row.setEnable(0);
//                row.setUrl(item.toString());
//                SeedsService.addone(row);
//            }
//        }
        //  SeedsService.markSeedDone("http://cs.whu.edu.cn/");
//        System.out.println(Toolkit.toUtf8("1234中国人"));

        // String html ="<html><head>\n</head><BODY 12=''>1232</body></html>";
        // String te=html.replaceAll("(?is)(<head>.*</head>)","");
//        String te = Toolkit.getBody("<body id=\"12\">"+System.lineSeparator()+"asddadsf</body>");
//        System.out.println(te);
        //  System.out.println(DataService.isUrlExist("http://econ.whut.edu.cn/"));

//        System.out.println(DataService.countLikeUrl("http://cs.whu.edu.cn/plus/list.php%"));
//        System.out.println(SysConfig.getDepth());
//        readSeedsToDb();

        System.out.println(Toolkit.getDomainForUrl("http://main.sgg.whu.edu.cn/"));
        String url = "http://cs.whu.edu.cn/12/";

//        System.out.println(Toolkit.parseSuffix(url));
//        String ext = URLConnection.guessContentTypeFromName("");
//        System.out.println(ext);

    }

    public static void readSeedsToDb() {
        String path = StartHeritrix.basePath + "add.txt";
        try {
            HashSet<String> sets = FileUtils.readFileAsSet(path);
            Iterator<String> it = sets.iterator();

            while (it.hasNext()) {
                String item = it.next();
                item = Toolkit.trimSlash(item);
                if (!item.isEmpty() && !SeedsService.isSeedsExits(item)) {
                    SeedsTable seed = new SeedsTable();
                    seed.setUrl(item);
                    seed.setEnable(0);
                    SeedsService.addone(seed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
