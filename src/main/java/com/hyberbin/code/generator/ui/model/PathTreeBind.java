package com.hyberbin.code.generator.ui.model;


import com.hyberbin.code.generator.domains.TreeNodeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import lombok.Data;

@Data
public class PathTreeBind {

  private TreeNodeModel model;
  private DefaultMutableTreeNode node;
  private DefaultMutableTreeNode parent;
  private String nodePath;

  public PathTreeBind(TreeNodeModel model, DefaultMutableTreeNode node) {
    this.model = model;
    this.node = node;
  }

  public TreeNodeModel getModel() {
    return model;
  }

  public void setSelected(boolean selected) {
    model.setSelected(selected);
  }

  @Override
  public String toString() {
    return getModel().getPathName();
  }
}
