package com.hyberbin.code.generator.ui.model;

import com.hyberbin.code.generator.domains.VarDo;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VarModel extends DefaultTableModel {

    private List<VarDo> datas = new ArrayList<>();

    public VarModel() {
        super(new String[]{"project", "key", "note", "value"}, 0);
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
        synchronized (this) {
            this.addRow(new Object[]{StringUtils.isEmpty(varDo.getProject()) ? "global" : varDo.getProject(), varDo.getKey(), varDo.getNote(),
                    varDo.getValue()});
            datas.add(varDo);
        }
    }

    public void deleteRow(String project, String key) {
        synchronized (this) {
            for (int i = 0; i < getRowCount(); i++) {
                VarDo varDo = datas.get(i);
                if (Objects.equals(varDo.getProject(), project) && Objects.equals(varDo.getKey(), key)) {
                    datas.remove(i);
                    removeRow(i);
                    i--;
                }
            }
        }
    }

    public Set<String> getProjects(){
        return datas.stream().map(varDo -> varDo.getProject()).collect(Collectors.toSet());
    }


    public List<VarDo> getDatas() {
        return datas;
    }

    public void save() {
        for (int i = 0; i < getRowCount(); i++) {
            datas.get(i).setProject(getStringValue(i, 0));
            datas.get(i).setKey(getStringValue(i, 1));
            datas.get(i).setNote(getStringValue(i, 2));
            datas.get(i).setValue(getStringValue(i, 3));
        }
    }

    public String getStringValue(int r, int c) {
        String valueAt = (String) getValueAt(r, c);
        return StringUtils.isEmpty(valueAt) ? c == 0 ? "global" : "" : valueAt + "";
    }
}
