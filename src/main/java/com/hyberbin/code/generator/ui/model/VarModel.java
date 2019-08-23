package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.VarDo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;

public class VarModel extends DefaultTableModel {

  private List<VarDo> datas = new ArrayList<>();

  public VarModel() {
    super(new String[]{"project","key", "note", "value"}, 0);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return true;
  }

  public void addRow(VarDo varDo) {
    this.addRow(new Object[]{StringUtils.isEmpty(varDo.getProject())?"global":varDo.getProject(),varDo.getKey(), varDo.getNote(),
        varDo.getValue()});
    datas.add(varDo);
  }



  public List<VarDo> getDatas() {
    return datas;
  }

  public void save(){
    for(int i=0;i<getRowCount();i++){
      datas.get(i).setProject(getStringValue(i,0));
      datas.get(i).setKey(getStringValue(i,1));
      datas.get(i).setNote(getStringValue(i,2));
      datas.get(i).setValue(getStringValue(i,3));
    }
  }

  public String getStringValue(int r,int c){
    String valueAt = (String)getValueAt(r, c);
    return StringUtils.isEmpty(valueAt)?c==0?"global":"":valueAt+"";
  }
}
