package com.hyberbin.code.generator.ui.model;


import com.hyberbin.code.generator.domains.TreeNodeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class PathTreeBind {

  private TreeNodeModel model;
  private DefaultMutableTreeNode node;
  private DefaultMutableTreeNode parent;
  private String nodePath;

  public PathTreeBind(TreeNodeModel model, DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
    this.model = model;
    this.node = node;
    this.parent = parent;
    if(parent.isRoot()){
      nodePath=model.getFileName();
    }else {
      nodePath=((PathTreeBind)parent.getUserObject()).getNodePath()+model.getFileName();
    }
  }

  public TreeNodeModel getModel() {
    return model;
  }


  @Override
  public String toString() {
    return getModel().getPathName();
  }
}
