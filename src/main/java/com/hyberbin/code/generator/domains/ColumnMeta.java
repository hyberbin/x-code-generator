package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class ColumnMeta extends BaseDo {

  private String tableName;
  private String columnName;
  private String comment;
  private String dataType;
  private Boolean isPrimaryKey;
  private Integer charMaxLength;
}
