package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.VarDo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class VarModel extends DefaultTableModel {

  private List<VarDo> datas = new ArrayList<>();

  public VarModel() {
    super(new String[]{"key", "note", "value", "canDel"}, 0);
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

  public void addRow(VarDo varDo) {
    this.addRow(new Object[]{varDo.getKey(), varDo.getNote(),
        varDo.getValue(), varDo.getCanDel()});
    datas.add(varDo);
  }



  public List<VarDo> getDatas() {
    return datas;
  }

  public void save(){
    for(int i=0;i<getRowCount();i++){
      datas.get(i).setKey(getStringValue(i,0));
      datas.get(i).setNote(getStringValue(i,1));
      datas.get(i).setValue(getStringValue(i,2));
      datas.get(i).setCanDel((Boolean) getValueAt(i,3));
    }
  }

  public String getStringValue(int r,int c){
    Object valueAt = getValueAt(r, c);
    return valueAt==null?"":valueAt+"";
  }
}
