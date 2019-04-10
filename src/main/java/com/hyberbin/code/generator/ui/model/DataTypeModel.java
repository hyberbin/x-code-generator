package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.DataTypeDo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class DataTypeModel extends DefaultTableModel {

  private List<DataTypeDo> datas = new ArrayList<>();

  public DataTypeModel() {
    super(new String[]{"dbType", "javaType", "jdbcType"}, 0);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return true;
  }

  public void addRow(DataTypeDo dataType) {
    this.addRow(new Object[]{dataType.getDbType(), dataType.getJavaType(),
        dataType.getJdbcType()});
    datas.add(dataType);
  }



  public List<DataTypeDo> getDatas() {
    return datas;
  }

  public void save(){
    for(int i=0;i<getRowCount();i++){
      datas.get(i).setDbType(getStringValue(i,0));
      datas.get(i).setJavaType(getStringValue(i,1));
      datas.get(i).setJdbcType(getStringValue(i,2));
    }
  }

  public String getStringValue(int r,int c){
    Object valueAt = getValueAt(r, c);
    return valueAt==null?"":valueAt+"";
  }
}
