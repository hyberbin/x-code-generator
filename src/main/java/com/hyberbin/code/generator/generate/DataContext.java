package com.hyberbin.code.generator.generate;

import com.hyberbin.code.generator.domains.ClassModelMeta;
import com.hyberbin.code.generator.domains.FieldMeta;
import com.hyberbin.code.generator.domains.TreeNodeModel;
import java.util.List;
import lombok.Data;

@Data
public class DataContext {

  private ClassModelMeta classModelMeta;
  private List<? extends FieldMeta> fieldMetas;
  private List<TreeNodeModel> templates;
  private String selectedEnv;
}
