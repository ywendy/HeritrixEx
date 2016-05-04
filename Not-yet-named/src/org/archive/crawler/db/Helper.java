package org.archive.crawler.db;

import org.archive.crawler.util.*;


import java.io.IOException;
import java.sql.SQLException;

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
        String te = Toolkit.getBody("");
        System.out.println(te);
      //  System.out.println(DataService.isUrlExist("http://econ.whut.edu.cn/"));

    }
}
