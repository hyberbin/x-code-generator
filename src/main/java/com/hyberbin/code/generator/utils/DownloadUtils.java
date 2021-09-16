package com.hyberbin.code.generator.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Slf4j
public class DownloadUtils {
    /**
     * 下载新版本
     *
     * @param httppath  http路径
     * @param localpath 本地存放路径
     * @return 当前版本文件所在路径
     * @throws Exception
     */
    public static String downloadNewVersion(String httppath, String localpath) throws Exception {
        URL url = new URL(httppath);
        File file = new File(localpath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        String oldFilePath = DownloadUtils.class.getResource("").getFile();
        log.info("准备下载文件:{}", httppath);
        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
             FileOutputStream fos = new FileOutputStream(file)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        log.info("下载完成，等待重启");
        return oldFilePath.split("!")[0].replace("file:/", "");
    }

}
