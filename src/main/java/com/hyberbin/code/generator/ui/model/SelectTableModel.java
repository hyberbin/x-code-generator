package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.ClassModelMeta;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SelectTableModel extends DefaultTableModel {

  private List<ClassModelMeta> datas = new ArrayList<>();

  public SelectTableModel() {
    super(new String[]{"表名", "备注", "类名", "是否生成"}, 0);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 3) {
      return Boolean.class;
    }
    return String.class;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return true;
  }

  public void addRow(ClassModelMeta classModelMeta) {
    this.addRow(new Object[]{classModelMeta.getTableName(), classModelMeta.getComment(),
        classModelMeta.getClassName(), false});
    datas.add(classModelMeta);
  }

  public List<ClassModelMeta> getSelectedTables() {
    List<ClassModelMeta> selecteds = new ArrayList<>();
    int rowCount = getRowCount();
    for (int i = 0; i < rowCount; i++) {
      Boolean check = (Boolean) getValueAt(i, 3);
      if (check != null && check) {
        selecteds.add(datas.get(i));
      }
    }
    return selecteds;
  }

  public List<ClassModelMeta> getDatas() {
    return datas;
  }

  public void save(){
    for(int i=0;i<getRowCount();i++){
      datas.get(i).setClassName(getStringValue(i,2)+"");
      datas.get(i).setComment(getStringValue(i,1)+"");
    }
  }

  public String getStringValue(int r,int c){
    Object valueAt = getValueAt(r, c);
    return valueAt==null?"":valueAt+"";
  }

  public void clearData(){
    while (getRowCount()>0){
      removeRow(getRowCount()-1);
    }
    datas.clear();
  }
}
