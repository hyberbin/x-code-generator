package com.hyberbin.code.generator.ui.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Function;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {

  private Function<MouseEvent, Void> callback;

  public CheckBoxTreeNodeSelectionListener(Function<MouseEvent, Void> function) {
    this.callback = function;
  }

  @Override
  public void mouseClicked(MouseEvent event) {
    try {
      callback.apply(event);
    }catch (Throwable e){

    }
    JTree tree = (JTree) event.getSource();
    int x = event.getX();
    int y = event.getY();
    int row = tree.getRowForLocation(x, y);
    TreePath path = tree.getPathForRow(row);
    if (path != null && x - tree.getRowBounds(row).x < 25) {
      Object lastPathComponent = path.getLastPathComponent();
      if (lastPathComponent instanceof CheckBoxTreeNode) {
        CheckBoxTreeNode node = (CheckBoxTreeNode) lastPathComponent;
        if (node != null && !node.isRoot()) {
          boolean isSelected = !node.isSelected();
          node.setSelected(isSelected);
          ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
        }
      }
    }
  }
}
