package com.example.demo.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/***
 *
 * Description: 
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/29
 * <p>@time: 9:35
 *
 */
public class CSVUtil {
    /**
     * 生成为CVS文件
     *
     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map,
                                     String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                boolean result = file.mkdir();
                System.out.println("创建文件成功? " + result);
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv",
                    new File(outPutPath));
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GBK"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                    .hasNext(); ) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write((String) propertyEntry.getValue());
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                Object row = iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                        .hasNext(); ) {
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                            .next();
                    /*-------------------------------*/
                    //以下部分根据不同业务做出相应的更改
                    StringBuilder sbContext = new StringBuilder();
                    if (null != BeanUtils.getProperty(row, (String) propertyEntry.getKey())) {
                        if ("证件号码".equals(propertyEntry.getValue())) {
                            //避免：身份证号码 ，读取时变换为科学记数 - 解决办法：加 \t(用Excel打开时，证件号码超过15位后会自动默认科学记数)
                            sbContext.append(BeanUtils.getProperty(row, (String) propertyEntry.getKey())).append("\t");
                        } else {
                            sbContext.append(BeanUtils.getProperty(row, (String) propertyEntry.getKey()));
                        }
                    }
                    csvFileOutputStream.write(sbContext.toString());
                    /*-------------------------------*/
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert csvFileOutputStream != null;
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     *
     * @param csvFilePath 文件路径
     * @param fileName    文件名称
     */
    public static void exportFile(HttpServletRequest request,
                                  HttpServletResponse response, String csvFilePath, String fileName)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/csv;charset=GBK");

        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len;
            byte[] buffer = new byte[1024];
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e1) {
            System.out.println(e1);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     *
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                if (file1.isFile()) {
                    boolean result = file1.delete();
                    System.out.println(file1.getName() + " 文件删除成功? " + result);
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                if (file1.isFile()) {
                    if (file1.getName().equals(fileName)) {
                        boolean result = file1.delete();
                        System.out.println(file1.getName() + " 文件删除成功? " + result);
                        return;
                    }
                }
            }
        }
    }

}
