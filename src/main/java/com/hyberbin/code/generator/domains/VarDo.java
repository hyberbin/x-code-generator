package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class VarDo extends BaseDo {

  private String key;
  private String value;
  private String note;
  private String project;
}
