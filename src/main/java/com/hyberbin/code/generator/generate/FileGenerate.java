package com.hyberbin.code.generator.generate;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.TreeNodeModel;
import com.hyberbin.code.generator.domains.VarDo;
import com.hyberbin.code.generator.utils.VelocityUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jplus.util.FileCopyUtils;
@Slf4j
public class FileGenerate {

  private SqliteDao sqliteDao;
  private List<VarDo> varDos;

  @Inject
  public FileGenerate(SqliteDao sqliteDao) {
    this.sqliteDao = sqliteDao;
  }

  public void generate(DataContext context) {
    varDos=null;
    List<TreeNodeModel> all = context.getTemplates();
    Map<String, TreeNodeModel> idMap = new HashMap();
    Map<String, List<TreeNodeModel>> parentMap = new HashMap();
    for (TreeNodeModel treeNodeModel : all) {
      idMap.put(treeNodeModel.getId(), treeNodeModel);
    }
    for (TreeNodeModel treeNodeModel : all) {
      List<TreeNodeModel> treeNodeModels = parentMap.get(treeNodeModel.getParentId());
      if (treeNodeModels == null) {
        treeNodeModels = new ArrayList<>();
        parentMap.put(treeNodeModel.getParentId(), treeNodeModels);
      }
      treeNodeModels.add(treeNodeModel);
    }
    generate("", "0", parentMap, context);
  }

  private void generate(String filePath, String nodeId,
      Map<String, List<TreeNodeModel>> parentMap, DataContext context) {
    List<TreeNodeModel> treeNodeModels = parentMap.get(nodeId);
    if (treeNodeModels != null) {
      for (TreeNodeModel model : treeNodeModels) {
        if (model.getType() == 2) {
          generate(filePath + model.getFileName(), context, model);
        } else {
          generate(filePath + model.getFileName(), model.getId(), parentMap, context);
        }
      }
    }
  }

  @SneakyThrows
  private void generate(String filePath, DataContext context, TreeNodeModel nodeModel) {
    HashMap vars = new HashMap();
    if(varDos==null){
      varDos = sqliteDao.getAll(VarDo.class);
    }
    varDos.forEach(v->{
      if(StringUtils.isEmpty(v.getProject())|| Objects.equals("global",v.getProject())){
        vars.put(v.getKey(), v.getValue());
      }
    });
    varDos.forEach(v->{
      if(Objects.equals(nodeModel.getProject(),v.getProject())){
        vars.put(v.getKey(), v.getValue());
      }
    });
    varDos.forEach(v->{
      if(Objects.equals(context.getSelectedEnv(),v.getProject())){
        vars.put(v.getKey(), v.getValue());
      }
    });
    vars.put("classModelMeta", context.getClassModelMeta());
    vars.put("fieldMetas", context.getFieldMetas());
    String path = VelocityUtils.evaluate("${basePath}" + filePath, vars);
    String fileName=path.substring(path.lastIndexOf("/")+1);
    String dir=path.substring(0,path.lastIndexOf("/")+1).replace(".","/");
    if(!new File(dir).exists()){
      new File(dir).mkdirs();
      log.info("新建文件夹:{}",dir);
    }
    log.info("当前环境变量:{}", JSON.toJSONString(vars));
    String content = VelocityUtils.evaluate(nodeModel.getTemplate(), vars);
    log.info("生成文件:{}",dir+fileName);
    FileCopyUtils.copy(content.getBytes("utf-8"), new FileOutputStream(dir+fileName));
  }
}
