package com.hyberbin.code.generator.ui.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {

  @Override
  public void mouseClicked(MouseEvent event) {
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
          node.setSelected(isSelected,true);
          ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
        }
      }
    }
  }
}
