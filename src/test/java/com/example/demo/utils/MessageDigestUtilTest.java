package com.example.demo.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/***
 *
 * Description: 
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/28
 * <p>@time: 17:12
 *
 */
public class MessageDigestUtilTest {
    private static String UPLOAD_FOLDER = "D://Workspaces/spring-boot-demo/src/main/resources/static/upload/";

    @Test
    public void fileToMd5() {


        String fileMd5 = MessageDigestUtil.fileToMd5(UPLOAD_FOLDER + "1abfec56-e862-4a3f-916b-744c7cb2d4e4.jpg");

        assert fileMd5 != null;
        System.out.println(fileMd5.toLowerCase());

    }
}