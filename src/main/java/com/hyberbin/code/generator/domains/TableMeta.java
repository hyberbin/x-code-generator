package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class TableMeta extends BaseDo {

  private String dbName;
  private String tableName;
  private String comment;
}
