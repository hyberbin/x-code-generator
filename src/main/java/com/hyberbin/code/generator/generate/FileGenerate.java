package com.hyberbin.code.generator.generate;

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
import lombok.SneakyThrows;
import org.jplus.util.FileCopyUtils;

public class FileGenerate {

  private SqliteDao sqliteDao;
  private List<VarDo> varDos;

  @Inject
  public FileGenerate(SqliteDao sqliteDao) {
    this.sqliteDao = sqliteDao;
  }

  public void generate(DataContext context) {
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
    if(!nodeModel.getSelected()){
      return;
    }
    HashMap vars = new HashMap();
    if(varDos==null){
      varDos = sqliteDao.getAll(VarDo.class);
    }
    for (VarDo varDo : varDos) {
      vars.put(varDo.getKey(), varDo.getValue());
    }
    vars.put("classModelMeta", context.getClassModelMeta());
    vars.put("fieldMetas", context.getFieldMetas());
    String path = VelocityUtils.evaluate("${basePath}" + filePath, vars);
    String dir=path.substring(0,path.lastIndexOf("/")+1);
    if(!new File(dir).exists()){
      new File(dir).mkdirs();
    }
    String content = VelocityUtils.evaluate(nodeModel.getTemplate(), vars);
    FileCopyUtils.copy(content.getBytes(), new FileOutputStream(path));
  }
}
