/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyberbin.code.generator.ui.frames;

import com.hyberbin.code.generator.config.CodeGeneratorModule;
import com.hyberbin.code.generator.config.ConfigFactory;
import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.ClassModelMeta;
import com.hyberbin.code.generator.domains.ColumnMeta;
import com.hyberbin.code.generator.domains.DataTypeDo;
import com.hyberbin.code.generator.domains.FieldMeta;
import com.hyberbin.code.generator.generate.DataContext;
import com.hyberbin.code.generator.generate.FileGenerate;
import com.hyberbin.code.generator.ui.model.SelectColumnModel;
import com.hyberbin.code.generator.utils.StringUtils;
import com.hyberbin.code.generator.vo.FieldMetaVo;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import lombok.extern.slf4j.Slf4j;
import org.jplus.hyb.database.sqlite.SqliteUtil;

/**
 * @author Administrator
 */
@Slf4j
public class SelectColumnFrame extends javax.swing.JFrame {

    private SqliteDao sqliteDao;
    private FileGenerate fileGenerate;
    private List<ClassModelMeta> datas;
    private int index;
    private String dataSource;
    private String selectedEnv;

    /**
     * Creates new form SelectTableFrame
     */
    public SelectColumnFrame(List<ClassModelMeta> datas, int index,
            String dataSource) {
        this(datas, index, dataSource, null);
    }

    public SelectColumnFrame(List<ClassModelMeta> datas, int index,
            String dataSource, String selectedEnv) {
        this.fileGenerate = CodeGeneratorModule.getInstance(FileGenerate.class);
        this.sqliteDao = CodeGeneratorModule.getInstance(SqliteDao.class);
        this.datas = datas;
        this.index = index;
        this.dataSource = dataSource;
        initComponents();
        netxButton.setEnabled(index < datas.size() - 1);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getHeight()) / 2;
        setLocation(x, y);
        jTable2.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        loadColumns();
        this.selectedEnv = selectedEnv;
        loadProjectEnvs(selectedEnv);
    }

    private void loadProjectEnvs(String selected) {
        if(StringUtils.isEmpty(selected)){
            selected=SqliteUtil.getProperty("projectEnvs");
        }
        List<String> projects = sqliteDao.getProjects();
        DefaultComboBoxModel model = (DefaultComboBoxModel) projectEnvs.getModel();
        model.removeAllElements();
        for (String env : projects) {
            model.addElement(env);
        }
        model.setSelectedItem(selected);
    }

    private void loadColumns() {
        ClassModelMeta modelMeta = datas.get(index);
        SelectColumnModel selectTableModel = getTableModel();
        List<ColumnMeta> columnMetas = sqliteDao
                .getColumnMetas(modelMeta.getTableName(), dataSource);
        List<FieldMeta> formDb = sqliteDao
                .getList(FieldMeta.class, "tableName", modelMeta.getTableName());
        Map<String, FieldMeta> fieldMetaMap = new HashMap<>();
        for (FieldMeta fieldMeta : formDb) {
            fieldMetaMap.put(fieldMeta.getColumnName(), fieldMeta);
        }
        for (ColumnMeta columnMeta : columnMetas) {
            FieldMeta fieldMeta = fieldMetaMap.get(columnMeta.getColumnName());
            if (fieldMeta != null) {
                selectTableModel.addRow(fieldMeta);
            } else {
                selectTableModel.addRow(new FieldMeta(columnMeta));
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        netxButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        genButton = new javax.swing.JButton();
        projectEnvs = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable2.setModel(new SelectColumnModel());
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane2.setViewportView(jTable2);

        netxButton.setText("下一步");
        netxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netxButtonActionPerformed(evt);
            }
        });

        saveButton.setText("保存");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        upButton.setText("上一步");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        genButton.setText("生成");
        genButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genButtonActionPerformed(evt);
            }
        });

        projectEnvs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectEnvs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                projectEnvsItemStateChanged(evt);
            }
        });

        jLabel1.setText("环境变量");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectEnvs, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(upButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netxButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genButton)
                .addGap(58, 58, 58))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(390, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(upButton)
                    .addComponent(netxButton)
                    .addComponent(genButton)
                    .addComponent(projectEnvs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void saveButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        getTableModel().save();
        List<FieldMeta> datas = getTableModel().getDatas();
        for (FieldMeta fieldMeta : datas) {
            sqliteDao.saveDO(fieldMeta);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void netxButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netxButtonActionPerformed
        this.dispose();
        new SelectColumnFrame(datas, index + 1, dataSource, selectedEnv).setVisible(true);
    }//GEN-LAST:event_netxButtonActionPerformed

    private void upButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        this.dispose();
        if (index != 0) {
            new SelectColumnFrame(datas, index - 1, dataSource, selectedEnv).setVisible(true);
        } else {
            CodeGeneratorModule.getInstance(SelectTableFrame.class).setVisible(true);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void genButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genButtonActionPerformed
        DataContext dataContext = new DataContext();
        ClassModelMeta classModelMeta = datas.get(index);
        log.info("准备生成表{}对应的文件",classModelMeta.getTableName());
        dataContext.setClassModelMeta(classModelMeta);
        CodeGenUIFrame codeGenUIFrame = CodeGeneratorModule.getInstance(CodeGenUIFrame.class);
        dataContext.setTemplates(codeGenUIFrame.getAllSelectedNodes());
        List<FieldMeta> datas = getTableModel().getDatas();
        List<FieldMetaVo> fieldMetaVos = new ArrayList<>(datas.size());
        Map<String, DataTypeDo> dataTypeMapping = ConfigFactory.getDataTypeMapping();
        for (FieldMeta fieldMeta : datas) {
            FieldMetaVo fieldMetaVo = new FieldMetaVo(fieldMeta);
            DataTypeDo dataTypeDo = dataTypeMapping.get(fieldMeta.getDataType());
            if (dataTypeDo == null) {
                DataTypeDo dataType = new DataTypeDo();
                dataType.setDbType(fieldMeta.getDataType());
                sqliteDao.saveDO(dataType);
            } else {
                fieldMetaVo.setJavaType(dataTypeDo.getJavaType());
                fieldMetaVo.setJdbcType(dataTypeDo.getJdbcType());
            }
            fieldMetaVos.add(fieldMetaVo);
            if (StringUtils.isBlank(fieldMetaVo.getFieldName())) {
                fieldMetaVo.setFieldName(StringUtils.INSTANCE
                        .getCamelCaseString(fieldMetaVo.getColumnName(), false));
            }
        }
        dataContext.setFieldMetas(fieldMetaVos);
        dataContext.setSelectedEnv(selectedEnv);
        fileGenerate.generate(dataContext);

    }//GEN-LAST:event_genButtonActionPerformed

    private void projectEnvsItemStateChanged(
            java.awt.event.ItemEvent evt) {//GEN-FIRST:event_projectEnvsItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.selectedEnv = evt.getItem().toString();
            SqliteUtil.setProperty("projectEnvs",selectedEnv);
        }
    }//GEN-LAST:event_projectEnvsItemStateChanged

    private SelectColumnModel getTableModel() {
        return (SelectColumnModel) jTable2.getModel();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton genButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton netxButton;
    private javax.swing.JComboBox<String> projectEnvs;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
