/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyberbin.code.generator.ui.frames;

import com.google.inject.Inject;
import com.hyberbin.code.generator.config.ConfigFactory;
import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.DataSource;
import com.hyberbin.code.generator.domains.DataTypeDo;
import com.hyberbin.code.generator.domains.VarDo;
import com.hyberbin.code.generator.ui.model.DataTypeModel;
import com.hyberbin.code.generator.ui.model.PathTreeBind;
import com.hyberbin.code.generator.ui.model.VarModel;
import com.hyberbin.code.generator.utils.StringUtils;
import com.hyberbin.code.generator.vo.DataSourceVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.hyb.database.transaction.IDbManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Slf4j
public class ConfigFrame extends javax.swing.JFrame {

    private CodeGenUIFrame codeGenUIFrame;
    private SqliteDao sqliteDao;
    private Map<String, DataSourceVo> allDataSources = new ConcurrentHashMap<>();

    /**
     * Creates new form ConfigFrame
     */
    @Inject
    public ConfigFrame(CodeGenUIFrame codeGenUIFrame, SqliteDao sqliteDao) {
        this.codeGenUIFrame = codeGenUIFrame;
        this.sqliteDao = sqliteDao;
        initComponents();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getHeight()) / 2;
        setLocation(x, y);
        varTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        loadAllDataSource();
        loadAllVars();
        loadAllDataTypeMapping();
        String useLocalVars = SqliteUtil.getProperty("useLocalVars");
        if (org.apache.commons.lang3.StringUtils.isBlank(useLocalVars)) {
            SqliteUtil.setProperty("useLocalVars", "true");
        }
        SqliteUtil.bindJCheckBoxField(this.useLocalVars, "useLocalVars");
        boolean remoteDbEnable = SqliteUtil.getBoolProperty(ConfigFactory.REMOTE_DB_CONFIG_ENABLE);
        enableRemoteConfig.setSelected(remoteDbEnable);
        enableRemoteConfig.addItemListener(e -> {
            SqliteUtil.setProperty(ConfigFactory.REMOTE_DB_CONFIG_ENABLE, "" + enableRemoteConfig.isSelected());
            if (!Objects.equals(remoteDbEnable, enableRemoteConfig.isSelected())) {
                new Thread(() -> {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "修改此配置需要重启应用");
                    System.exit(0);
                }).start();
            }
        });
    }

    private void loadAllDataSource() {
        allDataSources.clear();
        List<DataSource> all = checkDataSource(sqliteDao.getAll(DataSource.class, ConfigFactory.SQLITE_MANAGER),ConfigFactory.SQLITE_MANAGER);
        allDataSources.putAll(all.stream().collect(Collectors.toMap(DataSource::getDatasource, v -> new DataSourceVo(v, true), (v1, v2) -> v2)));
        if (SqliteUtil.getBoolProperty(ConfigFactory.REMOTE_DB_CONFIG_ENABLE)) {
            try {
                List<DataSource> dataSourceList = checkDataSource(sqliteDao.getAll(DataSource.class, ConfigFactory.getRemoteManage()),ConfigFactory.getRemoteManage());
                all.addAll(dataSourceList);
                allDataSources.putAll(dataSourceList.stream().collect(Collectors.toMap(DataSource::getDatasource, v -> new DataSourceVo(v, false), (v1, v2) -> v2)));
            } catch (Throwable e) {
                log.warn("读取远程配置中心数据库失败，请检查数据源配置");
            }
        }
        DefaultComboBoxModel model = (DefaultComboBoxModel) datasources.getModel();
        model.removeAllElements();
        for (DataSource source : all) {
            model.addElement(source.getDatasource());
        }
    }

    /**
     * 对数据源中重复的对象进行删除
     * @param dataSources
     * @param dbManager
     * @return
     */
    private List<DataSource> checkDataSource(List<DataSource> dataSources,IDbManager dbManager){
        Set<String> datasourceSet = new HashSet<>();
        List<DataSource> dataSourceList=new ArrayList<>();
        dataSources.forEach(d->{
            if(!datasourceSet.add(d.getDatasource())){
                sqliteDao.deleteOne(DataSource.class,"id",d.getId(),dbManager);
            }else {
                dataSourceList.add(d);
            }
        });
        return dataSourceList;
    }

    private void loadAllVars() {
        List<VarDo> all = sqliteDao.getAll(VarDo.class, getDbManager());
        VarModel model = getVarModel();
        for (VarDo varDo : all) {
            model.addRow(varDo);
        }
    }

    private void toggleRemoteButtons(boolean isLocal) {
        jButton2.setEnabled(isLocal);
        jButton3.setEnabled(isLocal);
    }

    private void loadAllDataTypeMapping() {
        List<DataTypeDo> all = sqliteDao.getAll(DataTypeDo.class);
        DataTypeModel model = (DataTypeModel) dataTypeTable.getModel();
        for (DataTypeDo typeModel : all) {
            model.addRow(typeModel);
        }
    }

    public IDbManager getDbManager() {
        return useLocalVars.isSelected() ? ConfigFactory.SQLITE_MANAGER : ConfigFactory.getSimpleManage();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dburl = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        datasource = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        datasources = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        useLocalVars = new javax.swing.JCheckBox();
        enableRemoteConfig = new javax.swing.JCheckBox();
        refreshButton = new javax.swing.JButton();
        validateButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        dataTypeTable = new javax.swing.JTable();
        saveButton1 = new javax.swing.JButton();
        addButton1 = new javax.swing.JButton();
        delButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        varTable = new javax.swing.JTable();
        saveButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();
        reloadButton = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("数据库连接");

        dburl.setColumns(50);

        jLabel2.setText("用户名");

        jLabel3.setText("密码");

        jLabel4.setText("数据源名称");

        datasource.setColumns(50);

        jLabel5.setText("数据源");

        datasources.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                datasourcesItemStateChanged(evt);
            }
        });

        jButton2.setText("保存");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("删除");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        useLocalVars.setSelected(true);
        useLocalVars.setText("使用本地变量");

        enableRemoteConfig.setText("启用远程配置中心");

        refreshButton.setText("刷新");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        validateButton.setText("验证");
        validateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel5))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(datasource)
                                                                .addComponent(dburl)
                                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel3)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(jPasswordField1)))
                                                        .addComponent(datasources, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(useLocalVars)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(validateButton)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(refreshButton)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton3)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton2))
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addComponent(enableRemoteConfig)
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(datasources, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(datasource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(dburl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(useLocalVars)
                                        .addComponent(enableRemoteConfig))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(refreshButton)
                                        .addComponent(validateButton))
                                .addContainerGap(71, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("应用配置", jPanel3);

        dataTypeTable.setModel(new DataTypeModel());
        jScrollPane3.setViewportView(dataTypeTable);

        saveButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/commit.png"))); // NOI18N
        saveButton1.setToolTipText("保存");
        saveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButton1ActionPerformed(evt);
            }
        });

        addButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/methodDefined@2x.png"))); // NOI18N
        addButton1.setToolTipText(" 添加");
        addButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButton1ActionPerformed(evt);
            }
        });

        delButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/methodNotDefined@2x.png"))); // NOI18N
        delButton1.setToolTipText("删除");
        delButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(411, Short.MAX_VALUE)
                                .addComponent(addButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(saveButton1)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(addButton1)
                                                .addComponent(delButton1)))
                                .addContainerGap(322, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                        .addContainerGap()))
        );

        jTabbedPane1.addTab("数据类型映射", jPanel2);

        varTable.setModel(new VarModel());
        jScrollPane2.setViewportView(varTable);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/commit.png"))); // NOI18N
        saveButton.setToolTipText("保存");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/methodDefined@2x.png"))); // NOI18N
        addButton.setToolTipText(" 添加");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/methodNotDefined@2x.png"))); // NOI18N
        delButton.setToolTipText("删除");
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        reloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/refresh.png"))); // NOI18N
        reloadButton.setToolTipText("获取模板中所有变量");
        reloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadButtonActionPerformed(evt);
            }
        });

        downloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/download.png"))); // NOI18N
        downloadButton.setToolTipText("下载所有远程变量");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(335, Short.MAX_VALUE)
                                .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(downloadButton)
                                        .addComponent(reloadButton)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(saveButton)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(addButton)
                                                        .addComponent(delButton))))
                                .addContainerGap(322, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                        .addContainerGap()))
        );

        jTabbedPane1.addTab("环境变量配置", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jTabbedPane1)
                                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DataSource dataSource = new DataSource();
        dataSource.setDatasource(this.datasource.getText());
        dataSource.setUrl(dburl.getText());
        dataSource.setUsername(username.getText());
        dataSource.setPassword(new String(jPasswordField1.getPassword()));
        boolean validate = sqliteDao.validateDbConfig(dataSource);
        if (validate) {
            DataSourceVo old = allDataSources.get(dataSource.getDatasource());
            if (old != null) {
                if (!old.isLocal()) {
                    JOptionPane.showMessageDialog(this, "远程配置不允许修改，请联系管理员");
                    return;
                }
                dataSource.setId(old.getId());
            }
            sqliteDao.saveDO(dataSource, ConfigFactory.SQLITE_MANAGER);
            if (!Objects.equals(old, dataSource)
                    && Objects.equals(dataSource.getDatasource(), ConfigFactory.REMOTE_DB_CONFIG_NAME)
                    && SqliteUtil.getBoolProperty(ConfigFactory.REMOTE_DB_CONFIG_ENABLE)) {
                new Thread(() -> {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "修改此配置需要重启应用");
                    System.exit(0);
                }).start();
            }
            loadAllDataSource();
        } else {
            JOptionPane.showMessageDialog(this, "数据库连接出错");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void datasourcesItemStateChanged(
            java.awt.event.ItemEvent evt) {//GEN-FIRST:event_datasourcesItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            DataSourceVo datasource = allDataSources.get(datasources.getSelectedItem().toString());
            if (datasource != null) {
                toggleRemoteButtons(datasource.isLocal());
                this.datasource.setText(datasource.getDatasource());
                this.dburl.setText(datasource.getUrl());
                this.username.setText(datasource.getUsername());
                this.jPasswordField1.setText(datasource.getPassword());
            }
        }
    }//GEN-LAST:event_datasourcesItemStateChanged

    private void jButton3ActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DataSourceVo dataSourceVo = allDataSources.get(datasources.getSelectedItem().toString());
        if (!dataSourceVo.isLocal()) {
            JOptionPane.showMessageDialog(this, "远程配置不允许删除");
            return;
        }
        if (!Objects.equals(dataSourceVo.getDatasource(), ConfigFactory.REMOTE_DB_CONFIG_NAME)) {
            int datasource = sqliteDao
                    .deleteOne(DataSource.class, "datasource", datasources.getSelectedItem().toString(), ConfigFactory.SQLITE_MANAGER);
            if (datasource == 1) {
                loadAllDataSource();
            }
        } else {
            JOptionPane.showMessageDialog(this, ConfigFactory.REMOTE_DB_CONFIG_NAME + "不允许删除只能修改");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void addButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        getVarModel().addRow(new VarDo());
    }//GEN-LAST:event_addButtonActionPerformed

    private void delButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
        int selectedRow = varTable.getSelectedRow();
        VarDo varDo = getVarModel().getDatas().get(selectedRow);
        getVarModel().removeRow(selectedRow);
        getVarModel().getDatas().remove(selectedRow);
        sqliteDao.deleteOne(VarDo.class, "key", varDo.getKey(), getDbManager());
    }//GEN-LAST:event_delButtonActionPerformed

    private void saveButtonActionPerformed(
            java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        VarModel model = getVarModel();
        model.save();
        List<VarDo> datas = model.getDatas();
        for (VarDo varDo : datas) {
            sqliteDao.saveDO(varDo, getDbManager());
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void saveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButton1ActionPerformed
        DataTypeModel model = (DataTypeModel) dataTypeTable.getModel();
        model.save();
        List<DataTypeDo> datas = model.getDatas();
        for (DataTypeDo dataTypeDo : datas) {
            sqliteDao.saveDO(dataTypeDo);
        }
    }//GEN-LAST:event_saveButton1ActionPerformed

    private void addButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButton1ActionPerformed
        ((DataTypeModel) dataTypeTable.getModel()).addRow(new DataTypeDo());
    }//GEN-LAST:event_addButton1ActionPerformed

    private void delButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButton1ActionPerformed
        DataTypeModel model = (DataTypeModel) dataTypeTable.getModel();
        int selectedRow = dataTypeTable.getSelectedRow();
        DataTypeDo dataTypeDo = model.getDatas().get(selectedRow);
        model.removeRow(selectedRow);
        model.getDatas().remove(selectedRow);
        sqliteDao.deleteOne(DataTypeDo.class, "dbType", dataTypeDo.getDbType());
    }//GEN-LAST:event_delButton1ActionPerformed

    private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadButtonActionPerformed
        Set<String> templateVars = new HashSet<>();
        templateVars.add("basePath");//默认的变量
        DefaultMutableTreeNode selectedProject = codeGenUIFrame.getSelectedProject();
        if (selectedProject == null) {
            return;
        }
        String project = ((PathTreeBind) selectedProject.getUserObject()).getModel().getProject();
        List<PathTreeBind> allSelectedNodes = codeGenUIFrame.getAllProjectNodes();
        for (PathTreeBind model : allSelectedNodes) {
            if (Objects.equals(2, model.getModel().getType())) {
                templateVars.addAll(StringUtils.getAllVars(model.getNodePath()));
                if (model.getModel().getFileName().endsWith("java")) {
                    templateVars.addAll(StringUtils.getAllVars(model.getModel().getTemplate()));
                } else if (model.getModel().getFileName().endsWith("xml")) {
                    templateVars.addAll(StringUtils.getKeyVars(model.getModel().getTemplate(), "groupId", "artifactId"));
                }
            }
        }
        VarModel varModel = getVarModel();
        Map<String, VarDo> oldVars = varModel.getDatas().stream().filter(v -> Objects.equals(project, v.getProject())).collect(Collectors.toMap(k -> k.getKey(), v -> v, (a, b) -> a));
        Set<String> newVars = templateVars.stream().filter(v -> !oldVars.containsKey(v)).collect(Collectors.toSet());
        Set<String> needDeleteVars = oldVars.keySet().stream().filter(v -> !templateVars.contains(v)).collect(Collectors.toSet());
        log.info("当前需要新增的变量有{}个，{}", newVars.size(), newVars);
        log.info("当前需要删除的变量有{}个，{}", needDeleteVars.size(), needDeleteVars);
        if (CollectionUtils.isNotEmpty(newVars)) {
            newVars.forEach(v -> {
                varModel.addRow(new VarDo(v, "", "", project));
            });
        }
        if (CollectionUtils.isNotEmpty(needDeleteVars)) {
            int confirmDialog = JOptionPane.showConfirmDialog(this, "需要删除无用变量吗?", "请选择", JOptionPane.YES_NO_OPTION);
            if (Objects.equals(JOptionPane.YES_OPTION, confirmDialog)) {
                needDeleteVars.forEach(v -> varModel.deleteRow(project, v));
            }
        }
    }//GEN-LAST:event_reloadButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        VarModel varModel = getVarModel();
        Map<String, VarDo> oldVars = varModel.getDatas().stream().collect(Collectors.toMap(k -> k.getProject() + "." + k.getKey(), v -> v, (a, b) -> a));
        List<VarDo> remoteVars = sqliteDao.getAll(VarDo.class);
        log.info("远程变量有{}个，{}", remoteVars.size(), remoteVars);
        if (CollectionUtils.isNotEmpty(remoteVars)) {
            remoteVars.forEach(v -> {
                if (!oldVars.containsKey(v.getProject() + "." + v.getKey())) {
                    varModel.addRow(new VarDo(v.getKey(), v.getValue(), v.getNote(), v.getProject()));
                }
            });
        }
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        loadAllDataSource();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void validateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateButtonActionPerformed
        DataSource dataSource = new DataSource();
        dataSource.setDatasource(this.datasource.getText());
        dataSource.setUrl(dburl.getText());
        dataSource.setUsername(username.getText());
        dataSource.setPassword(new String(jPasswordField1.getPassword()));
        boolean validate = sqliteDao.validateDbConfig(dataSource);
        JOptionPane.showMessageDialog(this, validate ? "连接成功" : "数据库连接出错");
    }//GEN-LAST:event_validateButtonActionPerformed

    public VarModel getVarModel() {
        return (VarModel) varTable.getModel();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton addButton1;
    private javax.swing.JTable dataTypeTable;
    private javax.swing.JTextField datasource;
    private javax.swing.JComboBox<String> datasources;
    private javax.swing.JTextField dburl;
    private javax.swing.JButton delButton;
    private javax.swing.JButton delButton1;
    private javax.swing.JButton downloadButton;
    private javax.swing.JCheckBox enableRemoteConfig;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton reloadButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveButton1;
    private javax.swing.JCheckBox useLocalVars;
    private javax.swing.JTextField username;
    private javax.swing.JButton validateButton;
    private javax.swing.JTable varTable;
    // End of variables declaration//GEN-END:variables
}
