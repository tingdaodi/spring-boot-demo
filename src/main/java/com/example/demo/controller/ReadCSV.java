package com.example.demo.controller;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * Description: 
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/28
 * <p>@time: 14:23
 *
 */
public class ReadCSV {

    private String file_path;

    ReadCSV() {
    }

    ReadCSV(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    /**
     * read()函数实现具体的读取CSV文件内容的方法
     *
     * @return
     */
    public List<String> read() {

        List<String> result = new ArrayList<>();

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(file_path);
            while (csvReader.readRecord()) {
                // 读取每一行数据，以逗号分开
                System.out.println(csvReader.getRawRecord());
                result.add(csvReader.getRawRecord());
            }
            csvReader.close();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }

}
