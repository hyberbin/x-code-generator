package com.hyberbin.code.generator;

import com.hyberbin.code.generator.config.CodeGeneratorModule;
import com.hyberbin.code.generator.ui.frames.CodeGenUIFrame;
import javax.swing.UIManager;
import lombok.SneakyThrows;

import java.io.File;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                    .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                UIManager.put("RootPane.setupButtonVisible", false);
            }
        } catch (Throwable ex) {
            java.util.logging.Logger.getLogger(CodeGenUIFrame.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @SneakyThrows
            public void run() {
                try {
                    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
                } catch (Exception e) {
                }
                CodeGenUIFrame CoderQueenUIFrame = CodeGeneratorModule.getInstance(CodeGenUIFrame.class);
                CoderQueenUIFrame.setVisible(true);
                /**
                 * 第一次启动时删除老版本文件
                 */
                if (args != null && args.length > 0) {
                    new File(args[0]).delete();
                    System.exit(-1);
                }
            }
        });
    }
}
