package org.archive.crawler.db;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ccoder on 2016/5/4.
 *
 * @Author ccoder
 * @Date 2016/5/4
 */
public class ShowData {
    public static void main(String[] args) {
        try {
            List<SeedsTable> seeds = SeedsService.getAllSeeds();
            System.out.println("共开启 " + seeds.size() + " 个种子站点");
            int count = 1;

            for (SeedsTable seed : seeds) {
                System.out.println("正在获取第 " + count + " 个种子： [" + seed.getUrl() + "]");

                List<DataTable> dt = DataService.getDataBySeed(seed.getUrl());
                int maxLevel = getMaxLevelOfData(dt);

                System.out.println("共爬取：" + dt.size());
                System.out.println("最大层数：" + maxLevel);
                System.out.println();
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxLevelOfData(List<DataTable> datas) {
        int result = -1;
        for (DataTable data : datas) {
            result = Math.max(result, data.getLevel());
        }
        return result;
    }
}
