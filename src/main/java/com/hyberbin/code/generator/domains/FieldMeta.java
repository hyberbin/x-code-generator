package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class FieldMeta extends ColumnMeta {

  private String className;
  private String fieldName;

  public FieldMeta(){

  }
  public FieldMeta(ColumnMeta columnMeta){
    setCharMaxLength(columnMeta.getCharMaxLength());
    setColumnName(columnMeta.getColumnName());
    setComment(columnMeta.getComment());
    setDataType(columnMeta.getDataType());
    setIsPrimaryKey(columnMeta.getIsPrimaryKey());
    setTableName(columnMeta.getTableName());
  }
}
