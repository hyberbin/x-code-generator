package com.hyberbin.code.generator.utils;

import javax.swing.tree.DefaultMutableTreeNode;

public class CopyContext {

    public static DefaultMutableTreeNode copy=null;

    public static void copy(DefaultMutableTreeNode copy){
        CopyContext.copy=copy;
    }

    public static DefaultMutableTreeNode paste(){
        DefaultMutableTreeNode paste=copy;
        copy=null;
        return paste;
    }
}
