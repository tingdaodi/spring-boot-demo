package com.example.demo.support;

import com.example.demo.controller.ProgressEntity;
import com.sun.javaws.progress.Progress;
import org.springframework.stereotype.Component;
import org.apache.commons.fileupload.ProgressListener;

import javax.servlet.http.HttpSession;

/***
 *
 * Description: 
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/29
 * <p>@time: 13:04
 *
 */
@Component
public class UploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session){
        this.session=session;
        ProgressEntity status = new ProgressEntity();
        session.setAttribute("status", status);
    }

    /**
     * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
     */
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        ProgressEntity status = (ProgressEntity) session.getAttribute("status");
        status.setpBytesRead(pBytesRead);
        status.setpContentLength(pContentLength);
        status.setpItems(pItems);
    }
}
