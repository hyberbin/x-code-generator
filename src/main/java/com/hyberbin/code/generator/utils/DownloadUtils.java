package com.hyberbin.code.generator.utils;

import org.jplus.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class DownloadUtils {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("下载参数不正确");
            return;
        }
        String httppath = args[0];
        String localpath = args[1];
        URL url = new URL(httppath);
        File file = new File(localpath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        FileCopyUtils.copy(url.openStream(), new FileOutputStream(localpath));
    }
}
