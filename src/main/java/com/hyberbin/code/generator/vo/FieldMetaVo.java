package com.hyberbin.code.generator.vo;

import com.hyberbin.code.generator.domains.FieldMeta;
import lombok.Data;

@Data
public class FieldMetaVo extends FieldMeta {
  private String javaType;
  private String jdbcType;

  public FieldMetaVo() {
  }

  public FieldMetaVo(FieldMeta fieldMeta){
    setCharMaxLength(fieldMeta.getCharMaxLength());
    setColumnName(fieldMeta.getColumnName());
    setComment(fieldMeta.getComment());
    setDataType(fieldMeta.getDataType());
    setIsPrimaryKey(fieldMeta.getIsPrimaryKey());
    setTableName(fieldMeta.getTableName());
    setClassName(fieldMeta.getClassName());
    setFieldName(fieldMeta.getFieldName());
  }
}
