package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.FieldMeta;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SelectColumnModel extends DefaultTableModel {

    private List<FieldMeta> datas = new ArrayList<>();

    public SelectColumnModel() {
        super(new String[]{"表名", "字段名", "fieldName", "备注"}, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 4) {
            return Boolean.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void addRow(FieldMeta fieldMeta) {
        this.addRow(
                new Object[]{fieldMeta.getTableName(), fieldMeta.getColumnName(), fieldMeta.getFieldName(),
                    fieldMeta.getComment()});
        datas.add(fieldMeta);
    }

    public List<FieldMeta> getDatas() {
        return datas;
    }

    public void save() {
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            datas.get(i).setFieldName(getStringValue(i, 2) + "");
            datas.get(i).setComment(getStringValue(i, 3) + "");
        }
    }

    public String getStringValue(int r, int c) {
        Object valueAt = getValueAt(r, c);
        return valueAt == null ? "" : valueAt + "";
    }
}
