package org.archive.crawler.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cyh on 16-3-23.
 */
public class Mytools {
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
}
